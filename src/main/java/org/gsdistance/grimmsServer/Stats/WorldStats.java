package org.gsdistance.grimmsServer.Stats;

import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.Dictionary;
import java.util.Hashtable;

public class WorldStats {
    public static final Dictionary<String, PersistentDataType<?, ?>> Stats = new Hashtable<>();
    static {
        Stats.put("death_count", PersistentDataType.INTEGER);
    }
    public static final Dictionary<String,String> StatNames = new Hashtable<>();
    static {
        StatNames.put("death_count", "Death Count");
    }

    private final JavaPlugin plugin;
    private final PersistentDataContainer dataContainer;

    public WorldStats(JavaPlugin plugin, World world) {
        this.plugin = plugin;
        this.dataContainer = world.getPersistentDataContainer();
    }

    public static WorldStats getWorldStats(World world){
        return new WorldStats(GrimmsServer.instance, world);
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

    public void changeStat(String stat, int amount){
        PersistentDataType type = Stats.get(stat);
        if (!(Stats.get(stat) == PersistentDataType.BYTE || Stats.get(stat) == PersistentDataType.SHORT || Stats.get(stat) == PersistentDataType.INTEGER || Stats.get(stat) == PersistentDataType.FLOAT || Stats.get(stat) == PersistentDataType.DOUBLE)) {
            return;
        }
        int currentStat = dataContainer.getOrDefault(new NamespacedKey(plugin, stat), type, 0);
        dataContainer.set(new NamespacedKey(plugin, stat), type, currentStat + amount);
    }
}
