package com.backbase.kalah.game;

import com.backbase.kalah.ai.WinStatus;

import java.util.*;

public class Board {

    private final Map<PitIdentifier, Integer> pitsMap;
    private final Map<Player, Integer> pitsStonesMap;
    private final Map<Player, Integer> kalahStonesMap;

    public Board(int numStones) {
        this.pitsMap = new HashMap<>();
        this.pitsStonesMap = new HashMap<>();
        this.kalahStonesMap = new HashMap<>();
        for (Player player : Player.values()) {
            for (int i = 1; i < 7; i++) {
                this.pitsMap.put(PitIdentifier.getPitIdentifier(player, i), numStones);
            }
            this.pitsMap.put(PitIdentifier.getPitIdentifier(player, 7), 0);
        }
        pitsStonesMap.put(Player.PLAYER1, 6*numStones);
        pitsStonesMap.put(Player.PLAYER2, 6*numStones);
        kalahStonesMap.put(Player.PLAYER1, 0);
        kalahStonesMap.put(Player.PLAYER2, 0);
    }

    public Board(Board board) {
        this.pitsMap = new HashMap<>(board.pitsMap);
        this.pitsStonesMap = new HashMap<>(board.pitsStonesMap);
        this.kalahStonesMap = new HashMap<>(board.kalahStonesMap);
    }

    public Player makeMove(Player player, int pitId) {
        return makeMove(PitIdentifier.getPitIdentifier(player,pitId));
    }

    public Player makeMove(PitIdentifier pitIdentifier) {
        Player player = pitIdentifier.getPlayer();
        int numStones = emptyPit(pitIdentifier);

        PitIdentifier currentPit = pitIdentifier;

        while (numStones != 0) {
            PitIdentifier nextPit = getNextPit(player, currentPit);
            addStoneToPit(nextPit);
            adjustNumStones(player, nextPit);
            --numStones;
            currentPit = nextPit;
        }

        if (currentPit.getPitId() == 7) {
            return player;
        }

        if (1 == getNumStonesInPit(currentPit) && player == currentPit.getPlayer()) {
            PitIdentifier oppositePit = getOppositePit(currentPit);
            if (!isEmptyPit(oppositePit)) {
                moveStonesToKalah(currentPit, player);
                moveStonesToKalah(oppositePit, player);
            }
        }
        return player.getOtherPlayer();
    }

    public void terminate() {
        pitsMap.keySet()
                .forEach(
                        pitIdentifier -> {
                            if (pitIdentifier.getPitId() != 7) {
                                moveStonesToKalah(pitIdentifier, pitIdentifier.getPlayer());
                            }
                        }
                );
    }

    public int getNumPitStonesForPlayer(Player player) {
        return this.pitsStonesMap.get(player);
    }

    public int getNumKalahStonesPlayer(Player player) {
        return this.kalahStonesMap.get(player);
    }

    public Map<Integer, Integer> getBoardStatus() {
        Map<Integer, Integer> boardStatus = new TreeMap<>();
        this.pitsMap.forEach((key, value) -> boardStatus.put(key.getPlayer() == Player.PLAYER2 ? key.getPitId() + 7 : key.getPitId(), value));
        return boardStatus;
    }

    public List<Integer> getLegalPits(Player player) {
        List<Integer> legalPits = new ArrayList<>();
        for (int i = 1; i < 7; ++i) {
            if (!isEmptyPit(PitIdentifier.getPitIdentifier(player,i))) {
                legalPits.add(i);
            }
        }
        return legalPits;
    }

    public boolean isEmptyPit(PitIdentifier pitIdentifier) {
        return getNumStonesInPit(pitIdentifier) == 0;
    }

    public WinStatus getWinStatus() {
        if (this.getNumPitStonesForPlayer(Player.PLAYER1) != 0 && this.getNumPitStonesForPlayer(Player.PLAYER2) != 0) {
            return WinStatus.UNFINISHED;
        }

        terminate();

        if (this.getNumKalahStonesPlayer(Player.PLAYER1) > this.getNumKalahStonesPlayer(Player.PLAYER2)) {
            return WinStatus.PLAYER_1;
        }
        if (this.getNumKalahStonesPlayer(Player.PLAYER1) < this.getNumKalahStonesPlayer(Player.PLAYER2)) {
            return WinStatus.PLAYER_2;
        }
        return WinStatus.DRAW;
    }

    private int getNumStonesInPit(PitIdentifier pitIdentifier) {
        return this.pitsMap.get(pitIdentifier);
    }

    private int emptyPit(PitIdentifier pitIdentifier) {
        int numStonesInPit = this.pitsMap.get(pitIdentifier);
        this.pitsMap.put(pitIdentifier, 0);
        return numStonesInPit;
    }

    private void adjustNumStones(Player player, PitIdentifier pitIdentifier) {
        if (7 == pitIdentifier.getPitId()) {
            if (Player.PLAYER1 == pitIdentifier.getPlayer()) {
                changePitStonesAmount(Player.PLAYER1, -1);
                changeKalahStonesAmount(Player.PLAYER1, 1);
            } else {
                changePitStonesAmount(Player.PLAYER2, -1);
                changeKalahStonesAmount(Player.PLAYER2, 1);
            }
        } else if (pitIdentifier.getPlayer() != player) {
            changePitStonesAmount(player, -1);
            changePitStonesAmount(player.getOtherPlayer(), 1);
        }
    }

    private void addStonesToPit(PitIdentifier pitIdentifier, int numStones) {
        this.pitsMap.put(pitIdentifier, this.pitsMap.get(pitIdentifier) + numStones);
    }

    private void addStoneToPit(PitIdentifier pitIdentifier) {
        addStonesToPit(pitIdentifier, 1);
    }

    private boolean isKalah(PitIdentifier pitIdentifier) {
        return pitIdentifier.getPitId() % 7 == 0;
    }

    private PitIdentifier getNextPit(Player player, PitIdentifier pitIdentifier) {
        if (pitIdentifier.getPitId() < 6 || pitIdentifier.getPitId() == 6 && player == pitIdentifier.getPlayer()) {
            return PitIdentifier.getPitIdentifier(pitIdentifier.getPlayer(), pitIdentifier.getPitId() + 1);
        }
        return PitIdentifier.getPitIdentifier(pitIdentifier.getPlayer().getOtherPlayer(), 1);
    }

    private PitIdentifier getOppositePit(PitIdentifier pitIdentifier) {
        return PitIdentifier.getPitIdentifier(pitIdentifier.getPlayer().getOtherPlayer(), 7 - pitIdentifier.getPitId());
    }

    private void changePitStonesAmount(Player player, int change) {
        this.pitsStonesMap.put(player, this.pitsStonesMap.get(player) + change);
    }

    private void changeKalahStonesAmount(Player player, int change) {
        this.kalahStonesMap.put(player, this.kalahStonesMap.get(player) + change);
    }

    private void moveStonesToKalah(PitIdentifier pitIdentifier, Player player) {
        int numStones = emptyPit(pitIdentifier);
        addStonesToPit(PitIdentifier.getPitIdentifier(player, 7), numStones);
        changePitStonesAmount(pitIdentifier.getPlayer(), -numStones);
        changeKalahStonesAmount(player, numStones);
    }

}
