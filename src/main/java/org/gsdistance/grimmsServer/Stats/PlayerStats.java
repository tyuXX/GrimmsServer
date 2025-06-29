package org.gsdistance.grimmsServer.Stats;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.*;

import static org.bukkit.persistence.PersistentDataType.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PlayerStats {
    public static final Dictionary<String, PersistentDataType<?, ?>> Stats = new Hashtable<>();

    static {
        Stats.put("death_count", INTEGER);
        Stats.put("money", PersistentDataType.DOUBLE);
        Stats.put("total_kill_count", INTEGER);
        Stats.put("join_count", INTEGER);
        Stats.put("tPoint", PersistentDataType.DOUBLE);
        Stats.put("block_break_count", LONG);
        Stats.put("level", INTEGER);
        Stats.put("xp", PersistentDataType.DOUBLE);
        Stats.put("xp_required", PersistentDataType.DOUBLE);
        Stats.put("sent_messages", LONG);
        Stats.put("intelligence", INTEGER);
        Stats.put("jobTitle", STRING);
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
        StatNames.put("sent_messages", "Messages Sent");
        StatNames.put("intelligence", "Intelligence");
        StatNames.put("jobTitle", "Job");
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

    public static final Map<String, ?> StatDefaultValues = Map.ofEntries(
            Map.entry("death_count", 0),
            Map.entry("money", 0.0),
            Map.entry("total_kill_count", 0),
            Map.entry("join_count", 0),
            Map.entry("tPoint", 0.0),
            Map.entry("block_break_count", 0L),
            Map.entry("level", 1),
            Map.entry("xp", 0.0),
            Map.entry("xp_required", 100.0),
            Map.entry("sent_messages", 0L),
            Map.entry("intelligence", new Random().nextInt(0, 100)),
            Map.entry("jobTitle", "")
    );

    private final JavaPlugin plugin;
    private final PersistentDataContainer dataContainer;

    public PlayerStats(JavaPlugin plugin, Player player) {
        this.plugin = plugin;
        this.dataContainer = player.getPersistentDataContainer();
        // Initialize default stats if they do not exist
        for (String stat : StatOrder) {
            if (!hasExactStat(stat)) {
                setStat(stat, StatDefaultValues.get(stat));
            }
        }
    }

    public static PlayerStats getPlayerStats(Player player) {
        return new PlayerStats(GrimmsServer.instance, player);
    }

    public Object getStat(String stat) {
        PersistentDataType type = Stats.get(stat);
        if (!hasExactStat(stat)) {
            if (type == INTEGER) {
                return 0;
            } else if (type == PersistentDataType.DOUBLE) {
                return 0.0;
            } else if (type == LONG) {
                return 0L;
            } else if (type == STRING) {
                return "";
            } else {
                GrimmsServer.logger.warning("Stat " + stat + " does not have a default value.");
                return null;
            }
        }
        if (type == null) {
            GrimmsServer.logger.warning("Stat " + stat + " does not exist.");
            return null;
        }
        return dataContainer.getOrDefault(new NamespacedKey(plugin, stat), type, 0);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
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
        if (type == INTEGER) {
            Integer currentStat = dataContainer.get(new NamespacedKey(plugin, stat), INTEGER);
            if (currentStat == null) {
                currentStat = 0;
            }
            dataContainer.set(new NamespacedKey(plugin, stat), INTEGER, currentStat + amount);
        } else if (type == PersistentDataType.DOUBLE) {
            Double currentStat = dataContainer.get(new NamespacedKey(plugin, stat), PersistentDataType.DOUBLE);
            if (currentStat == null) {
                currentStat = 0.0;
            }
            dataContainer.set(new NamespacedKey(plugin, stat), PersistentDataType.DOUBLE, currentStat + amount);
        } else if (type == LONG) {
            Long currentStat = dataContainer.get(new NamespacedKey(plugin, stat), LONG);
            if (currentStat == null) {
                currentStat = 0L;
            }
            dataContainer.set(new NamespacedKey(plugin, stat), LONG, currentStat + amount);
        }
    }
}
