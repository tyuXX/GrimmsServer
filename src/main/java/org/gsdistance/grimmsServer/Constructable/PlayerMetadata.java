package org.gsdistance.grimmsServer.Constructable;

import com.google.gson.Gson;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerMetadata {
    public final Map<String, Location> homes;
    public String nickname;
    public List<String> lastKnownNames;
    public final UUID uuid;
    public Location exitLocation;
    public String[] titles;
    public final String timestamp;

    public PlayerMetadata(Player player) {
        this.nickname = player.getDisplayName();
        this.lastKnownNames = List.of(player.getName());
        this.uuid = player.getUniqueId();
        this.exitLocation = new Location(player.getLocation());
        homes = new HashMap<>();
        titles = new String[0];
        timestamp = LocalDateTime.now().toString();
    }

    public void logMetadata() {
        GrimmsServer.logger.info("Player Metadata for " + uuid + ":" + new Gson().toJson(this));
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
        PlayerMetadata metadata = GrimmsServer.pds.retrieveData(player.getUniqueId() + ".json", "playerMetadata", PlayerMetadata.class);
        if (metadata == null) {
            metadata = new PlayerMetadata(player);
            GrimmsServer.logger.info("Created new PlayerMetadata for " + player.getName());
        } else {
            GrimmsServer.logger.info("Retrieved PlayerMetadata for " + player.getName());
        }
        PerSessionDataStorage.dataStore.put("metadata-" + player.getUniqueId(), Map.of(metadata, PlayerMetadata.class));
        // Set display name to nickname if different
        if (metadata.nickname != null && !metadata.nickname.equals(player.getDisplayName())) {
            player.setDisplayName(metadata.nickname);
        }
        metadata.logMetadata();
        return metadata;
    }
}