package com.backbase.kalah.ai;

import java.util.*;

public class Node {

    private Node parent;
    private final List<Node> children;
    private final GameState gameState;

    public Node(GameState gameState) {
        this.gameState = gameState;
        this.children = new ArrayList<>();
    }

    public Node(Node node) {
        this.children = new ArrayList<>();
        this.gameState = new GameState(node.getGameState());
        if (node.getParent() != null) {
            this.parent = node.getParent();
        }
        List<Node> children = node.getChildren();
        for (Node child : children) {
            this.children.add(new Node(child));
        }
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Node getRandomChildNode() {
        Random r = new Random();
        return this.children.get(r.nextInt(this.children.size()));
    }

    public Node getChildWithMaxScore() {
        return Collections.max(this.children, Comparator.comparing(c -> c.getGameState().getNumVisits()));
    }

}
