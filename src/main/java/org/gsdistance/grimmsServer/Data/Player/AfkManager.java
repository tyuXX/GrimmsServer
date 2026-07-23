package org.gsdistance.grimmsServer.Data.Player;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Config.ConfigKey;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class AfkManager {
    private static final Map<UUID, Long> lastActivityTimes = new ConcurrentHashMap<>();
    private static final Set<UUID> afkPlayers = new CopyOnWriteArraySet<>();
    private static final Map<UUID, Long> afkSinceTimes = new ConcurrentHashMap<>();

    public AfkManager() {
    }

    public static void recordActivity(Player player) {
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        // If player was AFK, announce they're back
        if (afkPlayers.contains(playerId)) {
            long afkDuration = currentTime - afkSinceTimes.get(playerId);
            announceReturn(player, afkDuration);
            afkPlayers.remove(playerId);
            afkSinceTimes.remove(playerId);
        }

        lastActivityTimes.put(playerId, currentTime);
    }

    public static void checkAfkStatus(Player player) {
        UUID playerId = player.getUniqueId();

        // Skip if player not tracked
        if (!lastActivityTimes.containsKey(playerId)) {
            lastActivityTimes.put(playerId, System.currentTimeMillis());
            return;
        }

        // Skip if already AFK
        if (afkPlayers.contains(playerId)) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        long lastActivity = lastActivityTimes.get(playerId);
        long afkTimeout = getAfkTimeout();

        if ((currentTime - lastActivity) >= afkTimeout) {
            setPlayerAfk(player, currentTime);
        }
    }

    private static void setPlayerAfk(Player player, long currentTime) {
        UUID playerId = player.getUniqueId();
        afkPlayers.add(playerId);
        afkSinceTimes.put(playerId, currentTime);
        announceAfk(player);
    }

    private static void announceAfk(Player player) {
        if (isAfkAnnouncementEnabled()) {
            String message = ChatColor.GRAY + player.getName() + " is now AFK";
            GrimmsServer.instance.getServer().broadcastMessage(message);
        }
    }

    private static void announceReturn(Player player, long afkDuration) {
        if (isAfkAnnouncementEnabled()) {
            String duration = formatDuration(afkDuration);
            String message = ChatColor.GRAY + player.getName() + " is no longer AFK (was AFK for " + duration + ")";
            GrimmsServer.instance.getServer().broadcastMessage(message);
        }
    }

    public static boolean isPlayerAfk(Player player) {
        return afkPlayers.contains(player.getUniqueId());
    }

    public static boolean isPlayerAfk(UUID playerId) {
        return afkPlayers.contains(playerId);
    }

    public static Set<UUID> getAfkPlayers() {
        return new HashSet<>(afkPlayers);
    }

    public static long getAfkDuration(Player player) {
        UUID playerId = player.getUniqueId();
        if (afkSinceTimes.containsKey(playerId)) {
            return System.currentTimeMillis() - afkSinceTimes.get(playerId);
        }
        return 0;
    }

    public static void onPlayerQuit(Player player) {
        UUID playerId = player.getUniqueId();
        lastActivityTimes.remove(playerId);
        afkPlayers.remove(playerId);
        afkSinceTimes.remove(playerId);
    }

    private static long getAfkTimeout() {
        return ActiveConfig.getConfigValue(ConfigKey.AFK_TIMEOUT, Long.class);
    }

    private static boolean isAfkAnnouncementEnabled() {
        return ActiveConfig.getConfigValue(ConfigKey.AFK_ANNOUNCEMENT_ENABLED, Boolean.class);
    }

    private static String formatDuration(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        if (hours > 0) {
            return hours + "h " + (minutes % 60) + "m";
        } else if (minutes > 0) {
            return minutes + "m " + (seconds % 60) + "s";
        } else {
            return seconds + "s";
        }
    }
}
