package com.backbase.kalah.exceptions;

public class InvalidDifficultyException extends Exception {

    public InvalidDifficultyException(int difficulty) {
        super(String.format("Invalid difficulty - %d. Please enter a value between 1 (easy) and 3 (hard)", difficulty));
    }

}
