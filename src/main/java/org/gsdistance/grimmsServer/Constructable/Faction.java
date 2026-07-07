package org.gsdistance.grimmsServer.Constructable;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Constructable.World.ChunkMetadata;
import org.gsdistance.grimmsServer.Data.FactionRank;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.Data.Player.PlayerRank;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Faction {
    public String name;
    public final String id;
    public final UUID uuid = UUID.randomUUID();
    public List<Data<UUID, FactionRank>> members;
    public List<Location> claims = new ArrayList();

    public Faction(String id, List<Data<UUID, FactionRank>> members) {
        this.id = id;
        this.members = members;
    }

    public KeyedBossBar getBossBar() {
        KeyedBossBar bossBar = GrimmsServer.instance.getServer().getBossBar(Shared.getNamespacedKey("faction-" + this.id));
        if (bossBar == null) {
            bossBar = GrimmsServer.instance.getServer().createBossBar(Shared.getNamespacedKey("faction-" + this.id), this.name, BarColor.WHITE, BarStyle.SOLID);
        }

        return bossBar;
    }

    public static Faction getFaction(UUID uuid) {
        Faction faction;
        if (PerSessionDataStorage.dataStore.containsKey("faction-" + uuid)) {
            faction = (Faction) PerSessionDataStorage.dataStore.get("faction-" + uuid).key();
        } else {
            faction = GrimmsServer.pds.retrieveData(uuid + ".json", "factions", Faction.class);
            if (faction == null) {
                return null;
            }

            PerSessionDataStorage.dataStore.put("faction-" + uuid, Data.of(faction, Faction.class));
        }

        return faction;
    }

    public void softSave() {
        PerSessionDataStorage.dataStore.put("faction-" + this.uuid, Data.of(this, Faction.class));
    }

    public boolean claimChunk(org.bukkit.Location location, Player player) {
        int claimLimit = this.getClaimLimit();
        if (this.claims.size() >= claimLimit) {
            player.sendMessage("§cYou have reached the claim limit for your faction.");
            return false;
        } else if (this.getMemberRank(player.getUniqueId()).weight < FactionRank.MEMBER.weight) {
            player.sendMessage("§cYou must be above member rank to claim a chunk.");
            return false;
        } else if (location.getWorld() == null) {
            player.sendMessage("§cInvalid world.");
            return false;
        } else {
            ChunkMetadata chunkMetadata = ChunkMetadata.getChunkMetadata(location.getChunk());
            if (chunkMetadata.factionUUID != null) {
                player.sendMessage("§cThis chunk is already claimed by a faction.");
                return false;
            } else {
                Location claimLocation = new Location(location.getWorld().getName(), location.getChunk().getX(), 0.0F, location.getChunk().getZ());
                this.claims.add(claimLocation);
                chunkMetadata.factionUUID = this.uuid;
                chunkMetadata.saveToFile();
                this.saveToFile();
                this.getBossBar().setProgress((double) this.claims.size() / (double) claimLimit);
                player.sendMessage("§aChunk (" + claimLocation.x + "/" + claimLocation.z + ") claimed successfully.");
                return true;
            }
        }
    }

    public boolean unClaimChunk(org.bukkit.Location location, Player player) {
        if (this.getMemberRank(player.getUniqueId()).weight < FactionRank.MEMBER.weight) {
            player.sendMessage("§cYou must be the above member rank to un-claim a chunk.");
            return false;
        } else {
            Location claimLocation = new Location(Objects.requireNonNull(location.getWorld()).getName(), location.getChunk().getX(), 0.0F, location.getChunk().getZ());
            ChunkMetadata chunkMetadata = ChunkMetadata.getChunkMetadata(location.getChunk());
            if (chunkMetadata.factionUUID == null) {
                player.sendMessage("§cThis chunk is not claimed by any faction.");
                return false;
            } else if (!chunkMetadata.factionUUID.equals(this.uuid)) {
                player.sendMessage("§cThis chunk is not claimed by your faction.");
                return false;
            } else {
                this.claims.remove(claimLocation);
                chunkMetadata.factionUUID = null;
                chunkMetadata.saveToFile();
                this.saveToFile();
                this.getBossBar().setProgress((double) this.claims.size() / (double) this.getClaimLimit());
                player.sendMessage("§aChunk (" + claimLocation.x + "/" + claimLocation.z + ") un-claimed successfully.");
                return true;
            }
        }
    }

    public void unClaimAllChunks() {
        for (Location claim : this.claims) {
            if (Bukkit.getWorld(claim.world) == null) {
                GrimmsServer.logger.warning("World " + claim.world + " is not loaded or does not exist. Skipping chunk unclaim for " + claim.x + ", " + claim.z);
            } else {
                ChunkMetadata chunkMetadata = ChunkMetadata.getChunkMetadata(Bukkit.getWorld(claim.world).getChunkAt((int) Math.round(claim.x), (int) Math.round(claim.z)));
                if (chunkMetadata != null) {
                    chunkMetadata.factionUUID = null;
                    chunkMetadata.saveToFile();
                }
            }
        }

        this.claims.clear();
        this.saveToFile();
    }

    public Integer getClaimLimit() {
        int claimLimit = 0;

        for (Data<UUID, FactionRank> member : this.members) {
            PlayerMetadata playerMetadata = PlayerMetadata.getOfflinePlayerMetadata(member.key());
            if (playerMetadata != null && playerMetadata.rank != null) {
                claimLimit += 100 * playerMetadata.rank.weight;
            } else {
                claimLimit += 100 * PlayerRank.DEFAULT.weight;
            }
        }

        if (claimLimit < 100) {
            claimLimit = 100;
        }

        return claimLimit;
    }

    public void saveToFile() {
        this.softSave();
        GrimmsServer.pds.saveData(this, Faction.class, this.uuid + ".json", "factions");
    }

    public void delete() {
        this.unClaimAllChunks();

        for (Data<UUID, FactionRank> member : new ArrayList<>(this.members)) {
            PlayerMetadata playerMetadata = PlayerMetadata.getOfflinePlayerMetadata(member.key());
            if (playerMetadata != null) {
                playerMetadata.factionUUID = null;
                playerMetadata.saveToPDS();
            }
        }

        GrimmsServer.instance.getServer().removeBossBar(Shared.getNamespacedKey("faction-" + this.id));
        GrimmsServer.pds.deleteData(this.uuid + ".json", "factions");
        PerSessionDataStorage.dataStore.remove("faction-" + this.uuid);
    }

    public void addMember(UUID memberId, FactionRank rank) {
        this.members.add(new Data(memberId, rank));
        this.saveToFile();
    }

    public UUID getMemberWithRank(FactionRank rank) {
        return this.members.stream().filter((data) -> data.value() == rank).map((data) -> data.key()).findFirst().orElse(null);
    }

    public void removeMember(UUID memberId) {
        this.members.removeIf((data) -> data.key().equals(memberId));
        this.saveToFile();
    }

    public FactionRank getMemberRank(UUID memberId) {
        return this.members.stream().filter((data) -> data.key().equals(memberId)).map((data) -> data.value()).findFirst().orElse(FactionRank.NONE);
    }

    public boolean isMember(UUID memberId) {
        return this.getMemberRank(memberId) != FactionRank.NONE;
    }
}
