package org.gsdistance.grimmsServer.Stats;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerStats {
    public static final Dictionary<String, PersistentDataType<?, ?>> Stats = new Hashtable<>();
    public static final Dictionary<String, String> StatNames;
    public static final List<String> StatOrder;
    public static final Map<String, ?> StatDefaultValues;
    private final JavaPlugin plugin;
    private final PersistentDataContainer dataContainer;
    private final Map<String, Object> offlineStats;
    private final boolean isOffline;

    public PlayerStats(JavaPlugin plugin, Player player) {
        this.plugin = plugin;
        this.dataContainer = player.getPersistentDataContainer();
        this.offlineStats = null;
        this.isOffline = false;

        for (String stat : StatOrder) {
            if (!this.hasExactStat(stat)) {
                this.setStat(stat, StatDefaultValues.get(stat));
            }
        }

    }

    public PlayerStats(Map<String, Object> offlineStatsData) {
        this.plugin = GrimmsServer.instance;
        this.dataContainer = null;
        this.offlineStats = new ConcurrentHashMap<>(offlineStatsData);
        this.isOffline = true;

        for (String stat : StatOrder) {
            if (!this.offlineStats.containsKey(stat)) {
                this.offlineStats.put(stat, StatDefaultValues.get(stat));
            }
        }
    }

    public static PlayerStats getPlayerStats(Player player) {
        return new PlayerStats(GrimmsServer.instance, player);
    }

    public static PlayerStats getOfflinePlayerStats(UUID uuid) {
        org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata metadata =
                org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata.getOfflinePlayerMetadata(uuid);
        if (metadata == null) {
            return null;
        }

        Map<String, Object> statsData = new ConcurrentHashMap<>();
        for (String stat : StatOrder) {
            Object defaultValue = StatDefaultValues.get(stat);
            statsData.put(stat, defaultValue);
        }

        return new PlayerStats(statsData);
    }

    public void resetStat(String stat) {
        this.setStat(stat, StatDefaultValues.get(stat));
    }

    public <T> T getStat(String stat, Class<T> ignoredType) {
        if (this.isOffline) {
            Object value = this.offlineStats.get(stat);
            if (value == null) {
                T defaultValue = (T) StatDefaultValues.get(stat);
                this.offlineStats.put(stat, defaultValue);
                return defaultValue;
            }
            return (T) value;
        }

        PersistentDataType type = Stats.get(stat);
        if (!this.hasExactStat(stat)) {
            GrimmsServer.logger.warning("Stat " + stat + " does not have a value.");
            T defaultValue = (T) StatDefaultValues.get(stat);
            if (defaultValue != null) {
                this.resetStat(stat);
                return defaultValue;
            } else {
                return null;
            }
        } else {
            return (T) this.dataContainer.getOrDefault(new NamespacedKey(this.plugin, stat), type, 0);
        }
    }

    public boolean hasExactStat(String stat) {
        if (this.isOffline) {
            return this.offlineStats.containsKey(stat);
        }
        return this.dataContainer.has(new NamespacedKey(this.plugin, stat), (PersistentDataType) Stats.get(stat));
    }

    public void setStat(String stat, Object value) {
        if (this.isOffline) {
            this.offlineStats.put(stat, value);
            return;
        }

        PersistentDataType type = Stats.get(stat);
        if (type == null) {
            GrimmsServer.logger.warning("Stat " + stat + " does not exist.");
        } else {
            this.dataContainer.set(new NamespacedKey(this.plugin, stat), type, value);
        }
    }

    public void changeStat(String stat, double amount) {
        PersistentDataType type = Stats.get(stat);
        if (type == null) {
            GrimmsServer.logger.warning("Stat " + stat + " does not exist.");
        } else {
            if (type == PersistentDataType.INTEGER) {
                Integer currentStat = this.dataContainer.get(new NamespacedKey(this.plugin, stat), PersistentDataType.INTEGER);
                if (currentStat == null) {
                    currentStat = 0;
                }

                this.dataContainer.set(new NamespacedKey(this.plugin, stat), PersistentDataType.INTEGER, currentStat + (int) amount);
            } else if (type == PersistentDataType.DOUBLE) {
                Double currentStat = this.dataContainer.get(new NamespacedKey(this.plugin, stat), PersistentDataType.DOUBLE);
                if (currentStat == null) {
                    currentStat = (double) 0.0F;
                }

                this.dataContainer.set(new NamespacedKey(this.plugin, stat), PersistentDataType.DOUBLE, currentStat + amount);
            } else if (type == PersistentDataType.LONG) {
                Long currentStat = this.dataContainer.get(new NamespacedKey(this.plugin, stat), PersistentDataType.LONG);
                if (currentStat == null) {
                    currentStat = 0L;
                }

                this.dataContainer.set(new NamespacedKey(this.plugin, stat), PersistentDataType.LONG, currentStat + (long) amount);
            }

        }
    }

    static {
        Stats.put("death_count", PersistentDataType.INTEGER);
        Stats.put("money", PersistentDataType.DOUBLE);
        Stats.put("maximum_balance", PersistentDataType.DOUBLE);
        Stats.put("total_kill_count", PersistentDataType.INTEGER);
        Stats.put("join_count", PersistentDataType.INTEGER);
        Stats.put("tPoint", PersistentDataType.DOUBLE);
        Stats.put("block_break_count", PersistentDataType.LONG);
        Stats.put("level", PersistentDataType.INTEGER);
        Stats.put("xp", PersistentDataType.DOUBLE);
        Stats.put("xp_required", PersistentDataType.DOUBLE);
        Stats.put("sent_messages", PersistentDataType.LONG);
        Stats.put("intelligence", PersistentDataType.INTEGER);
        Stats.put("jobTitle", PersistentDataType.STRING);
        Stats.put("pass", PersistentDataType.STRING);
        Stats.put("autologin", PersistentDataType.BOOLEAN);
        Stats.put("prestige", PersistentDataType.INTEGER);
        Stats.put("prestigePoints", PersistentDataType.LONG);
        StatNames = new Hashtable<>();
        StatNames.put("death_count", "Death Count");
        StatNames.put("money", "Money");
        StatNames.put("maximum_balance", "Maximum Balance");
        StatNames.put("total_kill_count", "Total Kill Count");
        StatNames.put("join_count", "Join Count");
        StatNames.put("tPoint", "Total Points");
        StatNames.put("block_break_count", "Block Break Count");
        StatNames.put("level", "Level");
        StatNames.put("xp", "Experience");
        StatNames.put("xp_required", "Experience Required");
        StatNames.put("sent_messages", "Messages Sent");
        StatNames.put("intelligence", "Intelligence");
        StatNames.put("jobTitle", "Job");
        StatNames.put("pass", "Password");
        StatNames.put("autologin", "Login Automatically");
        StatNames.put("prestige", "Prestige");
        StatNames.put("prestigePoints", "Prestige Points");
        StatOrder = List.of("death_count", "money", "maximum_balance", "total_kill_count", "join_count", "tPoint", "block_break_count", "sent_messages", "level", "xp", "xp_required", "intelligence", "jobTitle", "prestige", "prestigePoints");
        StatDefaultValues = Map.ofEntries(Map.entry("death_count", 0), Map.entry("money", (double) 0.0F), Map.entry("maximum_balance", (double) 1000.0F), Map.entry("total_kill_count", 0), Map.entry("join_count", 0), Map.entry("tPoint", (double) 0.0F), Map.entry("block_break_count", 0L), Map.entry("level", 1), Map.entry("xp", (double) 0.0F), Map.entry("xp_required", (double) 100.0F), Map.entry("sent_messages", 0L), Map.entry("intelligence", (new Random()).nextInt(0, 100)), Map.entry("jobTitle", ""), Map.entry("pass", ""), Map.entry("autologin", false), Map.entry("prestige", 0), Map.entry("prestigePoints", 0L));
    }
}
