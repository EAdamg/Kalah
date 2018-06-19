package com.backbase.kalah.ai;

import com.backbase.kalah.game.Board;
import com.backbase.kalah.game.Player;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MonteCarlo {

    private static final int WIN_SCORE = 10;

    private final int timePerMove;

    public MonteCarlo(int timePerMove) {
        this.timePerMove = timePerMove;
    }

    public int makeMove(Player player, Board board) {
        long start = System.currentTimeMillis();
        long end = start + this.timePerMove;

        Node rootNode = new Node(new GameState(board, player.getOtherPlayer()));
        rootNode.getGameState().setNextPlayer(player);

        while (System.currentTimeMillis() < end) {
            // Phase 1 - Selection
            Node node = selectNode(rootNode);
            // Phase 2 - Expansion
            if (node.getGameState().getBoard().getWinStatus() == WinStatus.UNFINISHED)
                expandNode(node);

            // Phase 3 - Simulation
            Node nodeToExplore = node;
            if (node.getChildren().size() > 0) {
                nodeToExplore = node.getRandomChildNode();
            }
            WinStatus winStatus = randomPlay(nodeToExplore, player);
            // Phase 4 - Update
            Player winningPlayer = WinStatus.PLAYER_1 == winStatus ? Player.PLAYER1 : WinStatus.PLAYER_2 == winStatus ? Player.PLAYER2 : null;
            update(nodeToExplore, winningPlayer);
        }

        rootNode = rootNode.getChildWithMaxScore();
        return rootNode.getGameState().getPitPlayed();
    }

    private Node selectNode(Node rootNode) {
        Node node = rootNode;
        while (node.getChildren().size() != 0) {
            node = getNodeWithHighestUcr(node);
        }
        return node;
    }

    private void expandNode(Node node) {
        List<GameState> possibleStates = node.getGameState().getAllPossibleStates();
        possibleStates.forEach(state -> {
            Node newNode = new Node(state);
            newNode.setParent(node);
            node.getChildren().add(newNode);
        });
    }

    private WinStatus randomPlay(Node node, Player player) {
        Node tempNode = new Node(node);
        GameState tempState = tempNode.getGameState();
        WinStatus winStatus = tempState.getBoard().getWinStatus();


        if (WinStatus.PLAYER_1 == winStatus && Player.PLAYER2 == player || WinStatus.PLAYER_2 == winStatus && Player.PLAYER1 == player) {
            tempNode.getParent().getGameState().setScore(Integer.MIN_VALUE);
            return winStatus;
        }

        Player nextPlayer = tempNode.getGameState().getNextPlayer();

        while (WinStatus.UNFINISHED == winStatus) {
            tempState.setPlayer(nextPlayer);
            nextPlayer = tempState.chooseRandomMove();
            winStatus = tempState.getBoard().getWinStatus();
        }

        return winStatus;
    }

    private void update(Node node, Player player) {
        Node tempNode = node;
        while (tempNode != null) {
            tempNode.getGameState().incrementVisit();
            if (tempNode.getGameState().getPlayer() == player)
                tempNode.getGameState().addScore(WIN_SCORE);
            tempNode = tempNode.getParent();
        }
    }

    private static double getUct(int totalVisits, double nodeScore, int nodeVisits) {
        if (nodeVisits == 0) {
            return Integer.MAX_VALUE;
        }
        return (nodeScore / (double) nodeVisits) + Math.sqrt(2) * Math.sqrt(Math.log(totalVisits) / (double) nodeVisits);
    }

    private static Node getNodeWithHighestUcr(Node node) {
        return Collections.max(
                node.getChildren(),
                Comparator.comparing(c -> getUct(node.getGameState().getNumVisits(), c.getGameState().getScore(), c.getGameState().getNumVisits())));
    }

}
