package com.backbase.kalah.control;

import java.util.Map;

public class GameMoveResponse {

    private final String id;
    private final String uri;
    private final Map<Integer, Integer> status;
    private final String message;
    private final String nextPlayer;

    public GameMoveResponse(String id, String uri, Map<Integer, Integer> status, String message, String nextPlayer) {
        this.id = id;
        this.uri = uri;
        this.status = status;
        this.message = message;
        this.nextPlayer = nextPlayer;
    }

    public String getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public Map<Integer, Integer> getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getNextPlayer() {
        return nextPlayer;
    }

}
