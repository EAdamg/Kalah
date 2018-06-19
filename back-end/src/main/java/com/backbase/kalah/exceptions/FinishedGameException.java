package com.backbase.kalah.exceptions;

public class FinishedGameException extends Exception {

    public FinishedGameException(String gameId) {
        super(String.format("Game with id %s has finished", gameId));
    }
}
