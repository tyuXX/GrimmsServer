package org.gsdistance.grimmsServer.Data;

public enum FactionRank {
    LEADER("Leader"),
    OFFICER("Officer"),
    MEMBER("Member"),
    RECRUIT("Recruit"),
    NONE("Not a member");

    private final String displayName;

    FactionRank(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
