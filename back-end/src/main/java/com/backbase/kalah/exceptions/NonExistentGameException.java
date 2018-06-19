package com.backbase.kalah.exceptions;

public class NonExistentGameException extends Exception {

    public NonExistentGameException(String gameId) {
        super(String.format("There is no game with id %s", gameId));
    }

}
