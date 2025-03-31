package org.gsdistance.grimmsServer;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class PlayerStats {
    public static final Dictionary<String, PersistentDataType<?, ?>> Stats = new Hashtable<>();
    static {
        Stats.put("death_count", PersistentDataType.INTEGER);
        Stats.put("money", PersistentDataType.DOUBLE);
        Stats.put("total_kill_count", PersistentDataType.INTEGER);
        Stats.put("join_count", PersistentDataType.INTEGER);
    }
    public static final Dictionary<String,String> StatNames = new Hashtable<>();
    static {
        StatNames.put("death_count", "Death Count");
        StatNames.put("money", "Money");
        StatNames.put("total_kill_count", "Total Kill Count");
        StatNames.put("join_count", "Join Count");
    }

    private final JavaPlugin plugin;
    private final Player player;
    private final PersistentDataContainer dataContainer;

    public PlayerStats(JavaPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.dataContainer = player.getPersistentDataContainer();
    }

    public static PlayerStats getPlayerStats(Player player) {
        return new PlayerStats(GrimmsServer.instance, player);
    }

    public Object getStat(String stat, PersistentDataType type) {
        if (Stats.get(stat) == null) {
            GrimmsServer.logger.warning("Stat " + stat + " does not exist.");
            return null;
        }
        if (type != Stats.get(stat)) {
            GrimmsServer.logger.warning("Stat type mismatch: " + stat + " is not of type " + type);
            return null;
        }
        return dataContainer.getOrDefault(new NamespacedKey(plugin, stat), type, 0);
    }

    public void setStat(String stat, Object value, PersistentDataType type) {
        if (Stats.get(stat) == null) {
            GrimmsServer.logger.warning("Stat " + stat + " does not exist.");
            return;
        }
        if((Stats.get(stat) != type)){
            GrimmsServer.logger.warning("Stat type mismatch: " + stat + " is not of type " + type);
            return;
        }
        dataContainer.set(new NamespacedKey(plugin, stat), type, value);
    }

    public void changeStat(String stat, int amount) {
        if (!(Stats.get(stat) == PersistentDataType.INTEGER)) {
            return;
        }
        int currentStat = dataContainer.getOrDefault(new NamespacedKey(plugin, stat), PersistentDataType.INTEGER, 0);
        dataContainer.set(new NamespacedKey(plugin, stat), PersistentDataType.INTEGER, currentStat + amount);
    }
}
