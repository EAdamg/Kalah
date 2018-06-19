package com.backbase.kalah.game;

public enum Player {

    PLAYER1("Player 1"),
    PLAYER2("Player 2");

    private final String name;

    Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Player getOtherPlayer() {
        if (PLAYER1 == this) {
            return PLAYER2;
        }
        return PLAYER1;
    }

}
