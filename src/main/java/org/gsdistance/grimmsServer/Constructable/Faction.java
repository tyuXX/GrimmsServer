package org.gsdistance.grimmsServer.Constructable;

import org.bukkit.OfflinePlayer;
import org.gsdistance.grimmsServer.Data.FactionRank;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Faction {
    public String name;
    public final String id;
    public final UUID uuid = UUID.randomUUID();
    public final List<Data<UUID, FactionRank>> members;
    public Faction(String id, List<Data<UUID, FactionRank>> members) {
        this.id = id;
        this.members = members;
    }

    public Faction loadFromFile(){
        return GrimmsServer.pds.retrieveData("","", Faction.class);
    }
}
