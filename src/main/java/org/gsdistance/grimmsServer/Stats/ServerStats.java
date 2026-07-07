package org.gsdistance.grimmsServer.Stats;

import com.google.gson.Gson;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.lang.reflect.Type;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

public class ServerStats {
    public static final Dictionary<String, Type> Stats = new Hashtable();
    public static final Dictionary<String, String> StatNames;
    private Map<String, Object> stats;

    public ServerStats() {
        this.loadStats();
    }

    public static ServerStats getServerStats() {
        return new ServerStats();
    }

    private void loadStats() {
        this.stats = (Map) GrimmsServer.pds.retrieveData("server_stats.json", "", Map.class);
        if (this.stats == null) {
            this.stats = new Hashtable();
            this.stats.put("market", (new Gson()).toJson(new Market()));
            this.stats.put("leaderboard", (new Gson()).toJson(new PlayerStatLeaderBoard()));
            this.saveStats();
        } else {
            if (!this.stats.containsKey("market")) {
                this.stats.put("market", (new Gson()).toJson(new Market()));
            }

            if (!this.stats.containsKey("leaderboard")) {
                this.stats.put("leaderboard", (new Gson()).toJson(new PlayerStatLeaderBoard()));
            }
        }

    }

    private void saveStats() {
        GrimmsServer.pds.saveData(this.stats, Map.class, "server_stats.json", "");
    }

    public Object getStat(String stat) {
        return this.stats.getOrDefault(stat, 0);
    }

    public void setStat(String stat, Object value) {
        this.stats.put(stat, value);
        this.saveStats();
    }

    public void changeStat(String stat, int amount) {
        Class<?> type = (Class) Stats.get(stat);
        if (type == null) {
            GrimmsServer.logger.warning("Stat " + stat + " does not exist.");
        } else {
            if (type == Integer.class) {
                int currentStat = this.stats.get(stat) instanceof Number ? ((Number) this.stats.get(stat)).intValue() : 0;
                this.stats.put(stat, currentStat + amount);
            } else if (type == Double.class) {
                double currentStat = this.stats.get(stat) instanceof Number ? ((Number) this.stats.get(stat)).doubleValue() : (double) 0.0F;
                this.stats.put(stat, currentStat + (double) amount);
            }

            this.saveStats();
        }
    }

    static {
        Stats.put("death_count", Integer.class);
        Stats.put("join_count", Integer.class);
        Stats.put("market", String.class);
        Stats.put("leaderboard", String.class);
        StatNames = new Hashtable();
        StatNames.put("death_count", "Death Count");
        StatNames.put("join_count", "Join Count");
        StatNames.put("market", "Market");
        StatNames.put("leaderboard", "Leaderboard");
    }
}
