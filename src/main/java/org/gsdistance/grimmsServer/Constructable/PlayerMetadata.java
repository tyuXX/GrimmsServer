package org.gsdistance.grimmsServer.Constructable;

import com.google.gson.Gson;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.Data.PlayerCapability;
import org.gsdistance.grimmsServer.Data.PlayerRank;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.time.LocalDateTime;
import java.util.*;

public class PlayerMetadata {
    public final Map<String, Location> homes;
    public String nickname;
    public List<String> lastKnownNames;
    public final UUID uuid;
    public Location exitLocation;
    public String[] titles;
    public final String timestamp;
    public UUID factionUUID = null; // Faction UUID, if the player is in a faction
    public double offlineMoney = 0.0; // Offline gained money.
    public boolean firstJoin = true;
    public PlayerRank rank = PlayerRank.DEFAULT;
    public Map<PlayerCapability, Integer> capabilities;

    public PlayerMetadata(Player player) {
        this.nickname = player.getDisplayName();
        this.lastKnownNames = List.of(player.getName());
        this.uuid = player.getUniqueId();
        this.exitLocation = new Location(player.getLocation());
        homes = new HashMap<>();
        titles = new String[0];
        timestamp = LocalDateTime.now().toString();
        capabilities = new HashMap<>();
    }

    public void logMetadata() {
        GrimmsServer.logger.info("Player Metadata for " + uuid + ":" + new Gson().toJson(this));
    }

    public void softSave() {
        PerSessionDataStorage.dataStore.put("metadata-" + uuid, Data.of(this, PlayerMetadata.class));
    }

    public void saveToPDS() {
        softSave();
        GrimmsServer.pds.saveData(this, PlayerMetadata.class, uuid.toString() + ".json", "playerMetadata");
    }

    public static PlayerMetadata getPlayerMetadata(Player player) {
        if (PerSessionDataStorage.dataStore.containsKey("metadata-" + player.getUniqueId())) {
            return (PlayerMetadata) PerSessionDataStorage.dataStore.get("metadata-" + player.getUniqueId()).key;
        }
        PlayerMetadata metadata = GrimmsServer.pds.retrieveData(player.getUniqueId() + ".json", "playerMetadata", PlayerMetadata.class);
        if (metadata == null) {
            metadata = new PlayerMetadata(player);
            GrimmsServer.logger.info("Created new PlayerMetadata for " + player.getName());
        } else {
            GrimmsServer.logger.info("Retrieved PlayerMetadata for " + player.getName());
        }
        PerSessionDataStorage.dataStore.put("metadata-" + player.getUniqueId(), Data.of(metadata, PlayerMetadata.class));
        // Set display name to nickname if different
        if (metadata.nickname != null && !metadata.nickname.equals(player.getDisplayName())) {
            player.setDisplayName(metadata.nickname);
        }
        metadata.logMetadata();
        return metadata;
    }

    public static PlayerMetadata getOfflinePlayerMetadata(UUID uuid) {
        if (PerSessionDataStorage.dataStore.containsKey("metadata-" + uuid)) {
            return (PlayerMetadata) PerSessionDataStorage.dataStore.get("metadata-" + uuid).key;
        }
        return GrimmsServer.pds.retrieveData(uuid.toString() + ".json", "playerMetadata", PlayerMetadata.class);
    }
}