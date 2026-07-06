package org.gsdistance.grimmsServer.Constructable.Player;

import com.google.gson.Gson;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Constructable.Location;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.Data.Player.PlayerCapability;
import org.gsdistance.grimmsServer.Data.Player.PlayerRank;

public class PlayerMetadata {
   public final Map<String, Location> homes;
   public String nickname;
   public List<String> lastKnownNames;
   public final UUID uuid;
   public Location exitLocation;
   public String lastExitTime = LocalDateTime.now().toString();
   public String[] titles;
   public final String timestamp;
   public UUID factionUUID = null;
   public double offlineMoney = (double)0.0F;
   public boolean firstJoin = true;
   public PlayerRank rank;
   public Map<PlayerCapability, Integer> capabilities;
   public List<String> settings;
   public String decoTitle;

   public PlayerMetadata(Player player) {
      this.rank = PlayerRank.DEFAULT;
      this.nickname = player.getDisplayName();
      this.lastKnownNames = List.of(player.getName());
      this.uuid = player.getUniqueId();
      this.exitLocation = new Location(player.getLocation());
      this.homes = new HashMap();
      this.titles = new String[0];
      this.timestamp = LocalDateTime.now().toString();
      this.capabilities = new HashMap();
      this.settings = new ArrayList();
      this.decoTitle = "";
   }

   public void fixNulls() {
      if (this.lastKnownNames == null) {
         this.lastKnownNames = new ArrayList();
      }

      if (this.titles == null) {
         this.titles = new String[0];
      }

      if (this.capabilities == null) {
         this.capabilities = new HashMap();
      }

      if (this.settings == null) {
         this.settings = new ArrayList();
      }

   }

   public void logMetadata() {
      Logger var10000 = GrimmsServer.logger;
      String var10001 = String.valueOf(this.uuid);
      var10000.info("Player Metadata for " + var10001 + ":" + (new Gson()).toJson(this));
   }

   public void softSave() {
      PerSessionDataStorage.dataStore.put("metadata-" + String.valueOf(this.uuid), Data.of(this, PlayerMetadata.class));
   }

   public void saveToPDS() {
      this.softSave();
      GrimmsServer.pds.saveData(this, PlayerMetadata.class, this.uuid.toString() + ".json", "playerMetadata");
   }

   public static PlayerMetadata getPlayerMetadata(Player player) {
      if (PerSessionDataStorage.dataStore.containsKey("metadata-" + String.valueOf(player.getUniqueId()))) {
         return (PlayerMetadata)((Data)PerSessionDataStorage.dataStore.get("metadata-" + String.valueOf(player.getUniqueId()))).key();
      } else {
         PlayerMetadata metadata = (PlayerMetadata)GrimmsServer.pds.retrieveData(String.valueOf(player.getUniqueId()) + ".json", "playerMetadata", PlayerMetadata.class);
         if (metadata == null) {
            metadata = new PlayerMetadata(player);
            GrimmsServer.logger.info("Created new PlayerMetadata for " + player.getName());
         } else {
            GrimmsServer.logger.info("Retrieved PlayerMetadata for " + player.getName());
         }

         PerSessionDataStorage.dataStore.put("metadata-" + String.valueOf(player.getUniqueId()), Data.of(metadata, PlayerMetadata.class));
         if (metadata.nickname != null && !metadata.nickname.equals(player.getDisplayName())) {
            player.setDisplayName(metadata.nickname);
         }

         metadata.fixNulls();
         metadata.softSave();
         metadata.logMetadata();
         return metadata;
      }
   }

   public static PlayerMetadata getOfflinePlayerMetadata(UUID uuid) {
      return PerSessionDataStorage.dataStore.containsKey("metadata-" + String.valueOf(uuid)) ? (PlayerMetadata)((Data)PerSessionDataStorage.dataStore.get("metadata-" + String.valueOf(uuid))).key() : (PlayerMetadata)GrimmsServer.pds.retrieveData(uuid.toString() + ".json", "playerMetadata", PlayerMetadata.class);
   }
}
