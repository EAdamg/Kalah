package com.backbase.kalah.control;

public class GameCreationRequest {

    private int numStonesPerPit;
    private int difficulty;

    public int getNumStonesPerPit() {
        return numStonesPerPit;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setNumStonesPerPit(int numStonesPerPit) {
        this.numStonesPerPit = numStonesPerPit;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
