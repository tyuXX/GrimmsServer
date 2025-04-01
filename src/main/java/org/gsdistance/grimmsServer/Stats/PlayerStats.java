package org.gsdistance.grimmsServer.Stats;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.Dictionary;
import java.util.Hashtable;

public class PlayerStats {
    public static final Dictionary<String, PersistentDataType<?, ?>> Stats = new Hashtable<>();
    static {
        Stats.put("death_count", PersistentDataType.INTEGER);
        Stats.put("money", PersistentDataType.DOUBLE);
        Stats.put("total_kill_count", PersistentDataType.INTEGER);
        Stats.put("join_count", PersistentDataType.INTEGER);
        Stats.put("tPoint", PersistentDataType.DOUBLE);
    }
    public static final Dictionary<String,String> StatNames = new Hashtable<>();
    static {
        StatNames.put("death_count", "Death Count");
        StatNames.put("money", "Money");
        StatNames.put("total_kill_count", "Total Kill Count");
        StatNames.put("join_count", "Join Count");
        StatNames.put("tPoint", "Total Points");
    }

    private final JavaPlugin plugin;
    private final PersistentDataContainer dataContainer;

    public PlayerStats(JavaPlugin plugin, Player player) {
        this.plugin = plugin;
        this.dataContainer = player.getPersistentDataContainer();
    }

    public static PlayerStats getPlayerStats(Player player) {
        return new PlayerStats(GrimmsServer.instance, player);
    }

    public Object getStat(String stat) {
        PersistentDataType type = Stats.get(stat);
        if (type == null) {
            GrimmsServer.logger.warning("Stat " + stat + " does not exist.");
            return null;
        }
        return dataContainer.getOrDefault(new NamespacedKey(plugin, stat), type, 0);
    }

    public void setStat(String stat, Object value) {
        PersistentDataType type = Stats.get(stat);
        if (type == null) {
            GrimmsServer.logger.warning("Stat " + stat + " does not exist.");
            return;
        }
        dataContainer.set(new NamespacedKey(plugin, stat), type, value);
    }

    public void changeStat(String stat, int amount) {
        PersistentDataType type = Stats.get(stat);
        if (type == null) {
            GrimmsServer.logger.warning("Stat " + stat + " does not exist.");
            return;
        }
        if (type == PersistentDataType.INTEGER) {
            Integer currentStat = dataContainer.get(new NamespacedKey(plugin, stat), PersistentDataType.INTEGER);
            if (currentStat == null) {
                currentStat = 0;
            }
            dataContainer.set(new NamespacedKey(plugin, stat), PersistentDataType.INTEGER, currentStat + amount);
        } else if (type == PersistentDataType.DOUBLE) {
            Double currentStat = dataContainer.get(new NamespacedKey(plugin, stat), PersistentDataType.DOUBLE);
            if (currentStat == null) {
                currentStat = 0.0;
            }
            dataContainer.set(new NamespacedKey(plugin, stat), PersistentDataType.DOUBLE, currentStat + amount);
        }
    }
}
