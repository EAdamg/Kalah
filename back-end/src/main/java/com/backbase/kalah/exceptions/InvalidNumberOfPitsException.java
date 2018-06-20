package com.backbase.kalah.exceptions;

public class InvalidNumberOfPitsException extends Exception {

    public InvalidNumberOfPitsException(int numPits) {
        super(String.format("Invalid number of stones per pit - %d. Please enter a value between 3 and 6", numPits));
    }

}
