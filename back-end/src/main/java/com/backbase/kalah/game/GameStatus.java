package com.backbase.kalah.game;

import java.util.Map;

public class GameStatus {

    private final Player player;
    private final boolean winner;
    private final Map<Integer, Integer> boardStatus;

    public GameStatus(Player player, boolean winner, Map<Integer, Integer> boardStatus) {
        this.player = player;
        this.winner = winner;
        this.boardStatus = boardStatus;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isWinner() {
        return winner;
    }

    public Map<Integer, Integer> getBoardStatus() {
        return boardStatus;
    }

}
