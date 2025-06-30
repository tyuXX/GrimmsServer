package org.gsdistance.grimmsServer.Data;

public enum PlayerRank {
    DEFAULT("Default",0),
    VIP("Vip",1),
    MVP("MVP",2),
    MODERATOR("Moderator",3),
    ADMIN("Admin",4),
    OWNER("Owner",99),;

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
}
