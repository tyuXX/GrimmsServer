package org.gsdistance.grimmsServer.Stats;

import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerHistoricalStats {
    public final UUID uuid;
    public final List<HistoricalStatsSnapshot> allTimeSnapshots;
    public final List<HistoricalStatsSnapshot> hourlySnapshots;
    public final List<HistoricalStatsSnapshot> quitSnapshots;

    public PlayerHistoricalStats(UUID uuid) {
        this.uuid = uuid;
        this.allTimeSnapshots = new ArrayList<>();
        this.hourlySnapshots = new ArrayList<>();
        this.quitSnapshots = new ArrayList<>();
    }

    public void addAllTimeSnapshot(HistoricalStatsSnapshot snapshot) {
        this.allTimeSnapshots.add(snapshot);
    }

    public void addHourlySnapshot(HistoricalStatsSnapshot snapshot) {
        this.hourlySnapshots.add(snapshot);
    }

    public void addQuitSnapshot(HistoricalStatsSnapshot snapshot) {
        this.quitSnapshots.add(snapshot);
    }

    public HistoricalStatsSnapshot createSnapshotFromPlayer(Player player) {
        HistoricalStatsSnapshot snapshot = new HistoricalStatsSnapshot();
        PlayerStats playerStats = PlayerStats.getPlayerStats(player);

        for (String stat : PlayerStats.StatOrder) {
            Object value = getStatValue(playerStats, stat);
            if (value != null) {
                snapshot.addStat(stat, value);
            }
        }

        return snapshot;
    }

    private Object getStatValue(PlayerStats playerStats, String stat) {
        PersistentDataType<?, ?> type = PlayerStats.Stats.get(stat);
        if (type == null) {
            return null;
        }

        if (type == PersistentDataType.INTEGER) {
            return playerStats.getStat(stat, Integer.class);
        } else if (type == PersistentDataType.DOUBLE) {
            return playerStats.getStat(stat, Double.class);
        } else if (type == PersistentDataType.LONG) {
            return playerStats.getStat(stat, Long.class);
        } else if (type == PersistentDataType.STRING) {
            return playerStats.getStat(stat, String.class);
        } else if (type == PersistentDataType.BOOLEAN) {
            return playerStats.getStat(stat, Boolean.class);
        }

        return null;
    }

    public void softSave() {
        PerSessionDataStorage.dataStore.put("historicalStats-" + this.uuid, Data.of(this, PlayerHistoricalStats.class));
    }

    public void saveToPDS() {
        this.softSave();
        GrimmsServer.pds.saveData(this, PlayerHistoricalStats.class, this.uuid.toString() + ".json", "historicalStats");
    }

    public static PlayerHistoricalStats getPlayerHistoricalStats(Player player) {
        return getPlayerHistoricalStats(player.getUniqueId());
    }

    public static PlayerHistoricalStats getPlayerHistoricalStats(UUID uuid) {
        if (PerSessionDataStorage.dataStore.containsKey("historicalStats-" + uuid)) {
            return (PlayerHistoricalStats) PerSessionDataStorage.dataStore.get("historicalStats-" + uuid).key();
        } else {
            PlayerHistoricalStats historicalStats = GrimmsServer.pds.retrieveData(uuid.toString() + ".json", "historicalStats", PlayerHistoricalStats.class);
            if (historicalStats == null) {
                historicalStats = new PlayerHistoricalStats(uuid);
                GrimmsServer.logger.info("Created new PlayerHistoricalStats for " + uuid);
            } else {
                GrimmsServer.logger.info("Retrieved PlayerHistoricalStats for " + uuid);
            }

            PerSessionDataStorage.dataStore.put("historicalStats-" + uuid, Data.of(historicalStats, PlayerHistoricalStats.class));
            historicalStats.softSave();
            return historicalStats;
        }
    }
}
