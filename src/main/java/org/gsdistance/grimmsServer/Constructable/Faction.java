package org.gsdistance.grimmsServer.Constructable;

import org.gsdistance.grimmsServer.Data.FactionRank;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.List;
import java.util.UUID;

public class Faction {
    public String name;
    public final String id;
    public final UUID uuid = UUID.randomUUID();
    public final List<Data<UUID, FactionRank>> members;
    public final List<Location> claims = List.of(); // Assuming Location is a class you have defined elsewhere

    public Faction(String id, List<Data<UUID, FactionRank>> members) {
        this.id = id;
        this.members = members;
    }

    public static Faction loadFromFile(UUID uuid) {
        return GrimmsServer.pds.retrieveData(uuid.toString() + ".json", "factions", Faction.class);
    }

    public void saveToFile() {
        GrimmsServer.pds.saveData(this, Faction.class, uuid.toString() + ".json", "factions");
    }

    public void addMember(UUID memberId, FactionRank rank) {
        members.add(new Data<>(memberId, rank));
        saveToFile();
    }

    public void removeMember(UUID memberId) {
        members.removeIf(data -> data.key.equals(memberId));
        saveToFile();
    }

    public FactionRank getMemberRank(UUID memberId) {
        return members.stream()
                .filter(data -> data.key.equals(memberId))
                .map(data -> data.value)
                .findFirst()
                .orElse(FactionRank.NONE);
    }

    public boolean isMember(UUID memberId) {
        return getMemberRank(memberId) != FactionRank.NONE;
    }
}
