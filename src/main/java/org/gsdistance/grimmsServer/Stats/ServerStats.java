package org.gsdistance.grimmsServer.Stats;

import com.google.gson.Gson;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.lang.reflect.Type;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

public class ServerStats {
    public static final Dictionary<String, Type> Stats = new Hashtable<>();

    static {
        Stats.put("death_count", Integer.class);
        Stats.put("join_count", Integer.class);
        Stats.put("market", String.class);
        Stats.put("leaderboard", String.class);
    }

    public static final Dictionary<String, String> StatNames = new Hashtable<>();

    static {
        StatNames.put("death_count", "Death Count");
        StatNames.put("join_count", "Join Count");
        StatNames.put("market", "Market");
        StatNames.put("leaderboard", "Leaderboard");
    }

    private Map<String, Object> stats;

    public ServerStats() {
        loadStats();
    }

    public static ServerStats getServerStats() {
        return new ServerStats();
    }

    private void loadStats() {
        //noinspection unchecked
        stats = (Map<String, Object>) GrimmsServer.pds.retrieveData("server_stats.json", "", Map.class);
        if (stats == null) {
            stats = new Hashtable<>();
            stats.put("market", new Gson().toJson(new Market())); // Initialize market stat
            stats.put("leaderboard", new Gson().toJson(new PlayerStatLeaderBoard())); // Initialize leaderboard stat
            saveStats();
        } else {
            if (!stats.containsKey("market")) {
                stats.put("market", new Gson().toJson(new Market())); // Initialize market stat
            }
            if (!stats.containsKey("leaderboard")) {
                stats.put("leaderboard", new Gson().toJson(new PlayerStatLeaderBoard())); // Initialize leaderboard stat
            }
        }
    }

    private void saveStats() {
        GrimmsServer.pds.saveData(stats, Map.class, "server_stats.json", "");
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
