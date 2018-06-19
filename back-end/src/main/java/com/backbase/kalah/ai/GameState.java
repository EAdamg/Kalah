package com.backbase.kalah.ai;

import com.backbase.kalah.game.Board;
import com.backbase.kalah.game.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameState {

    private Board board;
    private Player player;
    private Player nextPlayer;
    private int numVisits;
    private int score;
    private int pitPlayed;

    public GameState() {}

    public GameState(Board board, Player player) {
        this.board = new Board(board);
        this.player = player;
    }

    public GameState(GameState gameState) {
        this.board = new Board(gameState.getBoard());
        this.player = gameState.getPlayer();
        this.nextPlayer = gameState.getNextPlayer();
        this.numVisits = gameState.getNumVisits();
        this.score = gameState.getScore();
        this.pitPlayed = gameState.pitPlayed;
    }

    public List<GameState> getAllPossibleStates() {
        List<GameState> possibleStates = new ArrayList<>();
        List<Integer> legalPits = this.board.getLegalPits(this.getNextPlayer());
        legalPits.forEach(lp -> {
            GameState newState = new GameState(this.board, this.nextPlayer);
            newState.pitPlayed = lp;
            Player player = newState.getBoard().makeMove(newState.getPlayer(), lp);
            newState.setNextPlayer(player);
            possibleStates.add(newState);
        });
        return possibleStates;
    }

    public void addScore(double score) {
        if (this.score != Integer.MIN_VALUE)
            this.score += score;
    }

    public void incrementVisit() {
        ++this.numVisits;
    }

    public Player chooseRandomMove() {
        List<Integer> legalPits = this.board.getLegalPits(this.getPlayer());
        Random r = new Random();
        return this.board.makeMove(this.player, legalPits.get(r.nextInt(legalPits.size())));
    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getNumVisits() {
        return numVisits;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPitPlayed() {
        return pitPlayed;
    }

    public Player getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(Player nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

}
