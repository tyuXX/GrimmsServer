package org.gsdistance.grimmsServer.Stats;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HistoricalStatsManager {
    private final JavaPlugin plugin;
    private final Map<UUID, BukkitTask> hourlyTasks;
    private BukkitTask globalHourlyTask;

    public HistoricalStatsManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.hourlyTasks = new HashMap<>();
    }

    public void start() {
        startGlobalHourlyRecording();
    }

    public void stop() {
        stopGlobalHourlyRecording();
        stopAllPlayerHourlyTasks();
    }

    private void startGlobalHourlyRecording() {
        long ticksPerHour = 20 * 60 * 60;
        this.globalHourlyTask = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::recordHourlyStatsForAllPlayers, ticksPerHour, ticksPerHour);
        GrimmsServer.logger.info("Started hourly historical stats recording");
    }

    private void stopGlobalHourlyRecording() {
        if (this.globalHourlyTask != null) {
            this.globalHourlyTask.cancel();
            this.globalHourlyTask = null;
            GrimmsServer.logger.info("Stopped hourly historical stats recording");
        }
    }

    private void recordHourlyStatsForAllPlayers() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            recordHourlySnapshot(player);
        }
    }

    public void recordHourlySnapshot(Player player) {
        PlayerHistoricalStats historicalStats = PlayerHistoricalStats.getPlayerHistoricalStats(player);
        HistoricalStatsSnapshot snapshot = historicalStats.createSnapshotFromPlayer(player);
        historicalStats.addHourlySnapshot(snapshot);
        historicalStats.addAllTimeSnapshot(snapshot);
        historicalStats.saveToPDS();
    }

    public void recordQuitSnapshot(Player player) {
        PlayerHistoricalStats historicalStats = PlayerHistoricalStats.getPlayerHistoricalStats(player);
        HistoricalStatsSnapshot snapshot = historicalStats.createSnapshotFromPlayer(player);
        historicalStats.addQuitSnapshot(snapshot);
        historicalStats.addAllTimeSnapshot(snapshot);
        historicalStats.saveToPDS();
    }

    public void recordJoinSnapshot(Player player) {
        PlayerHistoricalStats historicalStats = PlayerHistoricalStats.getPlayerHistoricalStats(player);
        HistoricalStatsSnapshot snapshot = historicalStats.createSnapshotFromPlayer(player);
        historicalStats.addAllTimeSnapshot(snapshot);
        historicalStats.saveToPDS();
    }

    private void stopAllPlayerHourlyTasks() {
        for (BukkitTask task : this.hourlyTasks.values()) {
            task.cancel();
        }
        this.hourlyTasks.clear();
    }
}
