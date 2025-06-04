package org.gsdistance.grimmsServer.Stats;

import com.google.gson.Gson;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.*;

public class PlayerStats {
    public static final Dictionary<String, PersistentDataType<?, ?>> Stats = new Hashtable<>();

    static {
        Stats.put("death_count", PersistentDataType.INTEGER);
        Stats.put("money", PersistentDataType.DOUBLE);
        Stats.put("total_kill_count", PersistentDataType.INTEGER);
        Stats.put("join_count", PersistentDataType.INTEGER);
        Stats.put("tPoint", PersistentDataType.DOUBLE);
        Stats.put("block_break_count", PersistentDataType.LONG);
        Stats.put("level", PersistentDataType.INTEGER);
        Stats.put("xp", PersistentDataType.DOUBLE);
        Stats.put("xp_required", PersistentDataType.DOUBLE);
        Stats.put("titles", PersistentDataType.STRING);
        Stats.put("sent_messages", PersistentDataType.LONG);
        Stats.put("intelligence", PersistentDataType.INTEGER);
        Stats.put("jobTitle", PersistentDataType.STRING);
        Stats.put("homes", PersistentDataType.STRING);
    }

    public static final Dictionary<String, String> StatNames = new Hashtable<>();

    static {
        StatNames.put("death_count", "Death Count");
        StatNames.put("money", "Money");
        StatNames.put("total_kill_count", "Total Kill Count");
        StatNames.put("join_count", "Join Count");
        StatNames.put("tPoint", "Total Points");
        StatNames.put("block_break_count", "Block Break Count");
        StatNames.put("level", "Level");
        StatNames.put("xp", "Experience");
        StatNames.put("xp_required", "Experience Required");
        StatNames.put("titles", "Player Titles");
        StatNames.put("sent_messages", "Messages Sent");
        StatNames.put("intelligence", "Intelligence");
        StatNames.put("jobTitle", "Job");
        StatNames.put("homes", "Homes");
    }

    public static final List<String> StatOrder = List.of(
            "death_count",
            "money",
            "total_kill_count",
            "join_count",
            "tPoint",
            "block_break_count",
            "sent_messages",
            "level",
            "xp",
            "xp_required",
            "intelligence",
            "jobTitle"
    );

    private final JavaPlugin plugin;
    private final PersistentDataContainer dataContainer;

    public PlayerStats(JavaPlugin plugin, Player player) {
        this.plugin = plugin;
        this.dataContainer = player.getPersistentDataContainer();
        if (!hasExactStat("titles")) {
            setStat("titles", new Gson().toJson(new ArrayList<String>().toArray()));
        }
        if (!hasExactStat("homes")) {
            setStat("homes", new Gson().toJson(Map.of(
                    "home", "0 100 0"
            )));
        }
        if (!hasExactStat("intelligence")) {
            setStat("intelligence", new Random().nextInt(0, 100));
        }
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

    public boolean hasExactStat(String stat) {
        return dataContainer.has(new NamespacedKey(plugin, stat), Stats.get(stat));
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
        } else if (type == PersistentDataType.LONG) {
            Long currentStat = dataContainer.get(new NamespacedKey(plugin, stat), PersistentDataType.LONG);
            if (currentStat == null) {
                currentStat = 0L;
            }
            dataContainer.set(new NamespacedKey(plugin, stat), PersistentDataType.LONG, currentStat + amount);
        }
    }
}
