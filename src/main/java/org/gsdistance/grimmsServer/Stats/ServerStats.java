package org.gsdistance.grimmsServer.Stats;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;

public class ServerStats {
    public static final Dictionary<String, Type> Stats = new Hashtable<>();
    static {
        Stats.put("death_count", Integer.class);
        Stats.put("join_count", Integer.class);
        Stats.put("market", Market.class);
    }
    public static final Dictionary<String,String> StatNames = new Hashtable<>();
    static {
        StatNames.put("death_count", "Death Count");
        StatNames.put("join_count", "Join Count");
        StatNames.put("market", "Market");
    }

    private final JavaPlugin plugin;
    private final File statsFile;
    private Map<String, Object> stats;

    public ServerStats(JavaPlugin plugin) {
        this.plugin = plugin;
        this.statsFile = new File(plugin.getDataFolder(), "server_stats.json");
        loadStats();
    }
    public static ServerStats getServerStats() {
        return new ServerStats(GrimmsServer.instance);
    }

    private void loadStats() {
        if (!statsFile.exists()) {
            stats = new Hashtable<>();
            saveStats();
            return;
        }
        try (FileReader reader = new FileReader(statsFile)) {
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            stats = new Gson().fromJson(reader, type);
        } catch (IOException e) {
            GrimmsServer.logger.log(Level.WARNING, "Failed to load server stats.\n" + Arrays.toString(e.getStackTrace()));
            stats = new Hashtable<>();
        }
    }

    private void saveStats() {
        try {
            if (!statsFile.getParentFile().exists()) {
                statsFile.getParentFile().mkdirs();
            }
            try (FileWriter writer = new FileWriter(statsFile)) {
                new Gson().toJson(stats, writer);
            }
        } catch (IOException e) {
            GrimmsServer.logger.log(Level.WARNING, "Failed to save server stats.\n" + Arrays.toString(e.getStackTrace()));
        }
    }

    public Object getStat(String stat) {
        return stats.getOrDefault(stat, 0);
    }

    public void setStat(String stat, Object value) {
        stats.put(stat, value);
        saveStats();
    }

    public void changeStat(String stat, int amount) {
        Class<?> type = (Class<?>) Stats.get(stat);
        if (type == null) {
            GrimmsServer.logger.warning("Stat " + stat + " does not exist.");
            return;
        }
        if (type == Integer.class) {
            int currentStat = stats.get(stat) instanceof Number ? ((Number) stats.get(stat)).intValue() : 0;
            stats.put(stat, currentStat + amount);
        } else if (type == Double.class) {
            double currentStat = stats.get(stat) instanceof Number ? ((Number) stats.get(stat)).doubleValue() : 0.0;
            stats.put(stat, currentStat + amount);
        }
        saveStats();
    }
}
