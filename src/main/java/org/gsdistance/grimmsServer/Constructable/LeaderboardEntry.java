package org.gsdistance.grimmsServer.Constructable;

public class LeaderboardEntry {
    private String playerName;
    private Number statValue;

    public LeaderboardEntry(String playerName, Number statValue) {
        this.playerName = playerName;
        this.statValue = statValue;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Number getStatValue() {
        return statValue;
    }

    public void setStatValue(Number statValue) {
        this.statValue = statValue;
    }
}
