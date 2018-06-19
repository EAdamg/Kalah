package com.backbase.kalah.game;

import com.backbase.kalah.ai.MonteCarlo;
import com.backbase.kalah.exceptions.EmptyPitException;
import com.backbase.kalah.exceptions.InvalidPitForPlayerException;
import com.backbase.kalah.exceptions.InvalidPitIdException;

public class Game {

    private final Board board;
    private final MonteCarlo monteCarlo;

    private Player currentPlayer;

    private boolean finished;

    public Game(int numStonesPerPit, int difficulty) {
        this.board = new Board(numStonesPerPit);
        int timePerMove = 1 == difficulty ? 1000: 2 == difficulty ? 5000 : 10000;
        this.monteCarlo = new MonteCarlo(timePerMove);
        this.currentPlayer = Player.PLAYER1;
        this.finished = false;
    }

    public GameStatus makeMove(int pitId) throws InvalidPitIdException, EmptyPitException, InvalidPitForPlayerException {
        if (pitId < 1 || pitId > 14) {
            throw new InvalidPitIdException(pitId);
        }

        boolean player1Pit = pitId <= 7;
        if (Player.PLAYER1 == this.currentPlayer && !player1Pit || Player.PLAYER2 == this.currentPlayer && player1Pit) {
            throw new InvalidPitForPlayerException(String.format("Pit %d does not belong to %s", pitId, this.currentPlayer.getName()));
        }

        if (pitId == 7 || pitId == 14) {
            throw new InvalidPitForPlayerException(String.format("Pit %d is a Kalah, and may not be chosen for a move", pitId));
        }

        if (pitId > 7) {
            pitId -= 7;
        }

        PitIdentifier pitIdentifier = PitIdentifier.getPitIdentifier(this.currentPlayer, pitId);

        if (this.board.isEmptyPit(pitIdentifier)) {
            throw new EmptyPitException(pitIdentifier);
        }

        this.currentPlayer = this.board.makeMove(pitIdentifier);

        if (this.board.getNumPitStonesForPlayer(Player.PLAYER1) == 0 ||
                this.board.getNumPitStonesForPlayer(Player.PLAYER2) == 0) {
            this.board.terminate();
            this.finished = true;
            return this.board.getNumKalahStonesPlayer(Player.PLAYER1) >
                    this.board.getNumKalahStonesPlayer(Player.PLAYER2) ? new GameStatus(Player.PLAYER1, true, this.board.getBoardStatus()): new GameStatus(Player.PLAYER2, true, this.board.getBoardStatus());
        }

        return new GameStatus(this.currentPlayer, false, this.board.getBoardStatus());
    }

    public GameStatus makeAiMove() throws EmptyPitException, InvalidPitForPlayerException, InvalidPitIdException {
        int pitToPlay = monteCarlo.makeMove(this.currentPlayer, this.board);
        if (Player.PLAYER2 == this.currentPlayer) {
            pitToPlay += 7;
        }
        return makeMove(pitToPlay);
    }

    public boolean isFinished() {
        return finished;
    }

}
