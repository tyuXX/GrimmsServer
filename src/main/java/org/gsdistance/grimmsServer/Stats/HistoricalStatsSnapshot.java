package org.gsdistance.grimmsServer.Stats;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class HistoricalStatsSnapshot {
    private final String timestamp;
    private final Map<String, Object> stats;

    public HistoricalStatsSnapshot() {
        this.timestamp = LocalDateTime.now().toString();
        this.stats = new HashMap<>();
    }

    public HistoricalStatsSnapshot(Map<String, Object> stats) {
        this.timestamp = LocalDateTime.now().toString();
        this.stats = new HashMap<>(stats);
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> getStats() {
        return stats;
    }

    public void addStat(String statName, Object value) {
        this.stats.put(statName, value);
    }

    public Object getStat(String statName) {
        return this.stats.get(statName);
    }
}
