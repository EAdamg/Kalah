package com.backbase.kalah.exceptions;

public class InvalidPitIdException extends Exception {

    public InvalidPitIdException(int pitId) {
        super(String.format("Pit Id %d is invalid. Please provide a Pit Id between 1 and 14", pitId));
    }
}
