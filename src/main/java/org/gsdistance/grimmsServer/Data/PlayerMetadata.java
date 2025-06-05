package org.gsdistance.grimmsServer.Data;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Location;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerMetadata {
    public Map<String, Location>  homes;
    public String nickname;
    public String lastKnownName;
    public UUID uuid;
    public Location exitLocation;
    public String[] titles;
    public PlayerMetadata(Player player) {
        this.nickname = player.getDisplayName();
        this.lastKnownName = player.getName();
        this.uuid = player.getUniqueId();
        this.exitLocation = new Location(player.getLocation());
        homes = new HashMap<>();
        titles = new String[0];
    }

    public void softSave() {
        PerSessionDataStorage.dataStore.put("metadata-" + uuid, Map.of(this, PlayerMetadata.class));
    }

    public void saveToPDS() {
        softSave();
        GrimmsServer.pds.saveData(this, PlayerMetadata.class, uuid.toString() + ".json", "playerMetadata");
    }

    public static PlayerMetadata getPlayerMetadata(Player player) {
        if (PerSessionDataStorage.dataStore.containsKey("metadata-" + player.getUniqueId())) {
            return (PlayerMetadata) PerSessionDataStorage.dataStore.get("metadata-" + player.getUniqueId()).keySet().toArray()[0];
        }
        else if (GrimmsServer.pds.exists(player.getUniqueId().toString() + ".json", "playerMetadata")) {
            PlayerMetadata metadata = (PlayerMetadata) GrimmsServer.pds.retrieveData(player.getUniqueId().toString() + ".json", PlayerMetadata.class, "playerMetadata");
            if (metadata == null) {
                metadata = new PlayerMetadata(player);
            }
            PerSessionDataStorage.dataStore.put("metadata-" + player.getUniqueId(), Map.of(metadata, PlayerMetadata.class));
            return metadata;
        }
        else {
            PlayerMetadata metadata = new PlayerMetadata(player);
            PerSessionDataStorage.dataStore.put("metadata-" + player.getUniqueId(), Map.of(metadata, PlayerMetadata.class));
            return metadata;
        }
    }
}

