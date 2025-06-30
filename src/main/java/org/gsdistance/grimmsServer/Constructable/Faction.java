package org.gsdistance.grimmsServer.Constructable;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Data.FactionRank;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.Data.PlayerRank;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Faction {
    public String name;
    public final String id;
    public final UUID uuid = UUID.randomUUID();
    public final List<Data<UUID, FactionRank>> members;
    public final List<Location> claims = new ArrayList<>(); // Assuming Location is a class you have defined elsewhere

    public Faction(String id, List<Data<UUID, FactionRank>> members) {
        this.id = id;
        this.members = members;
    }

    public static Faction getFaction(UUID uuid) {
        Faction faction;
        if(PerSessionDataStorage.dataStore.containsKey("faction-" + uuid)) {
            faction = (Faction) PerSessionDataStorage.dataStore.get("faction-" + uuid).key;
        } else {
            faction = GrimmsServer.pds.retrieveData(uuid + ".json", "factions", Faction.class);
            if (faction == null) {
                GrimmsServer.logger.info("No faction found for UUID: " + uuid);
                return null; // No faction found
            }
            PerSessionDataStorage.dataStore.put("faction-" + uuid, Data.of(faction, Faction.class));
        }
        return faction;
    }

    public void softSave(){
        PerSessionDataStorage.dataStore.put("faction-" + uuid, Data.of(this, Faction.class));
    }

    public boolean claimChunk(org.bukkit.Location location, Player player) {
        if(claims.size() >= getClaimLimit()) {
            player.sendMessage("§cYou have reached the claim limit for your faction.");
            return false;
        }
        if(getMemberRank(player.getUniqueId()).weight < FactionRank.MEMBER.weight) {
            player.sendMessage("§cYou must be above member rank to claim a chunk.");
            return false;
        }
        if(location.getWorld() == null) {
            player.sendMessage("§cInvalid world.");
            return false;
        }
        ChunkMetadata chunkMetadata = ChunkMetadata.getChunkMetadata(location.getChunk());
        //Logging
        GrimmsServer.logger.warning(chunkMetadata.x + "_" + chunkMetadata.z + ".json");
        GrimmsServer.logger.warning("chunkMetadata" + File.separator + chunkMetadata.world);
        GrimmsServer.logger.warning(new Gson().toJson(chunkMetadata));
        GrimmsServer.logger.warning(new Gson().toJson(claims));
        if (chunkMetadata.factionUUID != null) {
            player.sendMessage("§cThis chunk is already claimed by a faction.");
            return false;
        }
        Location claimLocation = new Location(location.getWorld().getName(), location.getChunk().getX(), 0, location.getChunk().getZ());
        GrimmsServer.logger.warning(new Gson().toJson(claimLocation));
        claims.add(claimLocation);
        chunkMetadata.factionUUID = uuid;
        chunkMetadata.saveToFile();
        saveToFile();
        player.sendMessage("§aChunk (" + claimLocation.x + "-" + claimLocation.z + ") claimed successfully.");
        return true;
    }

    public boolean unClaimChunk(org.bukkit.Location location, Player player) {
        if(getMemberRank(player.getUniqueId()).weight < FactionRank.MEMBER.weight) {
            player.sendMessage("§cYou must be the above member rank to un-claim a chunk.");
            return false;
        }
        Location claimLocation = new Location(Objects.requireNonNull(location.getWorld()).getName(), location.getChunk().getX(), 0, location.getChunk().getZ());
        ChunkMetadata chunkMetadata = ChunkMetadata.getChunkMetadata(location.getChunk());
        GrimmsServer.logger.warning(chunkMetadata.x + "_" + chunkMetadata.z + ".json");
        GrimmsServer.logger.warning("chunkMetadata" + File.separator + chunkMetadata.world);
        GrimmsServer.logger.warning(new Gson().toJson(chunkMetadata));
        GrimmsServer.logger.warning(new Gson().toJson(claims));
        GrimmsServer.logger.warning(new Gson().toJson(claimLocation));
        if (chunkMetadata.factionUUID == null) {
            player.sendMessage("§cThis chunk is not claimed by any faction.");
            return false;
        }
        if (!chunkMetadata.factionUUID.equals(uuid)) {
            player.sendMessage("§cThis chunk is not claimed by your faction.");
            return false;
        }
        claims.remove(claimLocation);
        chunkMetadata.factionUUID = null;
        chunkMetadata.saveToFile();
        saveToFile();
        player.sendMessage("§aChunk (" + claimLocation.x + "-" + claimLocation.z + ") un-claimed successfully.");
        return true;
    }

    public void unClaimAllChunks() {
        for (Location claim : claims) {
            if (Bukkit.getWorld(claim.world) == null) {
                GrimmsServer.logger.warning("World " + claim.world + " is not loaded or does not exist. Skipping chunk unclaim for " + claim.x + ", " + claim.z);
                continue; // Skip this chunk
            }
            ChunkMetadata chunkMetadata = ChunkMetadata.getChunkMetadata(
                    Bukkit.getWorld(claim.world).getChunkAt((int) Math.round(claim.x), (int) Math.round(claim.z))
            );
            if (chunkMetadata != null) {
                chunkMetadata.factionUUID = null;
                chunkMetadata.saveToFile();
            }
        }
        claims.clear();
        saveToFile();
    }

    public Integer getClaimLimit() {
        int claimLimit = 0;
        for (Data<UUID, FactionRank> member : members) {
            PlayerMetadata playerMetadata = PlayerMetadata.getOfflinePlayerMetadata(member.key);
            if (playerMetadata == null || playerMetadata.rank == null) {
                claimLimit += 100 * PlayerRank.DEFAULT.weight;
            }
            else{
                claimLimit += 100 * playerMetadata.rank.weight;
            }
        }
        if (claimLimit < 100) {
            claimLimit = 100; // Minimum claim limit
        }
        return claimLimit;
    }

    public void saveToFile() {
        softSave();
        GrimmsServer.pds.saveData(this, Faction.class, uuid + ".json", "factions");
    }

    public void delete(){
        unClaimAllChunks();
        GrimmsServer.pds.deleteData(uuid + ".json", "factions");
        PerSessionDataStorage.dataStore.remove("faction-" + uuid);
    }

    public void addMember(UUID memberId, FactionRank rank) {
        members.add(new Data<>(memberId, rank));
        saveToFile();
    }

    public UUID getMemberWithRank(FactionRank rank) {
        return members.stream()
                .filter(data -> data.value == rank)
                .map(data -> data.key)
                .findFirst()
                .orElse(null);
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
