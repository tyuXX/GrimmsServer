package org.gsdistance.grimmsServer.Data.Player;

public enum PlayerRank {
    DEFAULT("Default", 1),
    VIP("Vip", 2),
    MVP("MVP", 3),
    MODERATOR("Moderator", 4),
    ADMIN("Admin", 5),
    OWNER("Owner", 99),
    ;

    public final String name;
    public final Integer weight;

    PlayerRank(String name, Integer weight) {
        this.name = name;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return name;
    }

    public static PlayerRank fromWeight(int weight) {
        for (PlayerRank rank : values()) {
            if (rank.weight == weight) {
                return rank;
            }
        }
        return DEFAULT; // Default to DEFAULT if no match found
    }

    public static PlayerRank fromString(String name) {
        for (PlayerRank rank : values()) {
            if (rank.name.equalsIgnoreCase(name)) {
                return rank;
            }
        }
        return DEFAULT; // Default to DEFAULT if no match found
    }
}
