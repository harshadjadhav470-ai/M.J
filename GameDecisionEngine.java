package com.mj.natasha;

public class GameDecisionEngine {

    public String decide(
            boolean enemyVisible,
            boolean healthLow,
            boolean safeZoneNear) {

        if (healthLow) {
            return "FIND_COVER";
        }

        if (safeZoneNear) {
            return "MOVE_TO_SAFE_ZONE";
        }

        if (enemyVisible) {
            return "ASSESS_SITUATION";
        }

        return "EXPLORE";
    }
}
