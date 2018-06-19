package com.backbase.kalah.game;

import java.util.HashMap;
import java.util.Map;

public enum PitIdentifier {

    PLAYER1_1(Player.PLAYER1, 1),
    PLAYER1_2(Player.PLAYER1, 2),
    PLAYER1_3(Player.PLAYER1, 3),
    PLAYER1_4(Player.PLAYER1, 4),
    PLAYER1_5(Player.PLAYER1, 5),
    PLAYER1_6(Player.PLAYER1, 6),
    PLAYER1_7(Player.PLAYER1, 7),

    PLAYER2_1(Player.PLAYER2, 1),
    PLAYER2_2(Player.PLAYER2, 2),
    PLAYER2_3(Player.PLAYER2, 3),
    PLAYER2_4(Player.PLAYER2, 4),
    PLAYER2_5(Player.PLAYER2, 5),
    PLAYER2_6(Player.PLAYER2, 6),
    PLAYER2_7(Player.PLAYER2, 7);

    private Player player;
    private int pitId;

    private final static Map<Player, Map<Integer, PitIdentifier>> pitsMap = new HashMap<>();

    static {
        pitsMap.put(Player.PLAYER1, new HashMap<>());
        pitsMap.put(Player.PLAYER2, new HashMap<>());
        for (PitIdentifier pitIdentifier : PitIdentifier.values()) {
            pitsMap.get(pitIdentifier.getPlayer()).put(pitIdentifier.getPitId(), pitIdentifier);
        }
    }

    PitIdentifier(Player player, int pitId) {
        this.player = player;
        this.pitId = pitId;
    }

    public Player getPlayer() {
        return player;
    }

    public int getPitId() {
        return pitId;
    }

    public static PitIdentifier getPitIdentifier(Player player, int pitId) {
        return PitIdentifier.pitsMap.get(player).get(pitId);
    }

}
