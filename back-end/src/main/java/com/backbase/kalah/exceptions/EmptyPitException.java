package com.backbase.kalah.exceptions;

import com.backbase.kalah.game.PitIdentifier;

public class EmptyPitException extends Exception {

    public EmptyPitException(PitIdentifier pitIdentifier) {
        super(String.format("Pit #%d of %s is empty", pitIdentifier.getPitId(), pitIdentifier.getPlayer().getName()));
    }
}
