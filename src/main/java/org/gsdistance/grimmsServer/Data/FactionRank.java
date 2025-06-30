package org.gsdistance.grimmsServer.Data;

public enum FactionRank {
    LEADER("Leader",4),
    OFFICER("Officer",3),
    MEMBER("Member",2),
    RECRUIT("Recruit",1),
    NONE("Not a member",0);

    private final String displayName;
    public final Integer weight;

    FactionRank(String displayName, Integer weight) {
        this.displayName = displayName;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
