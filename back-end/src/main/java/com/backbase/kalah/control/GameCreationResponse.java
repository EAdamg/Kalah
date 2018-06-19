package com.backbase.kalah.control;

public class GameCreationResponse {

    private final String id;
    private final String uri;

    public GameCreationResponse(String id, String uri) {
        this.id = id;
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

}
