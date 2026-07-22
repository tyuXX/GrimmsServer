package org.gsdistance.grimmsServer.Data;

public enum FactionRank {
    LEADER("Leader", 4),
    OFFICER("Officer", 3),
    MEMBER("Member", 2),
    RECRUIT("Recruit", 1),
    NONE("Not a member", 0);

    private final String displayName;
    public final Integer weight;

    FactionRank(String displayName, Integer weight) {
        this.displayName = displayName;
        this.weight = weight;
    }

    public String toString() {
        return this.displayName;
    }

    public static FactionRank fromString(String name) {
        for (FactionRank rank : values()) {
            if (rank.displayName.equalsIgnoreCase(name)) {
                return rank;
            }
        }

        return NONE;
    }
}
