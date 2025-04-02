package org.gsdistance.grimmsServer.Events;

import org.gsdistance.grimmsServer.Data.PlayerTitleManager;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.ServerStats;
import org.gsdistance.grimmsServer.Stats.WorldStats;

public class PlayerJoinEvent {
    public static void Event(org.bukkit.event.player.PlayerJoinEvent event) {
        PlayerStats.getPlayerStats(event.getPlayer()).changeStat("join_count", 1);
        WorldStats.getWorldStats(event.getPlayer().getWorld()).changeStat("join_count", 1);
        ServerStats.getServerStats().changeStat("join_count", 1);
        PlayerTitleManager.joinedGame(event.getPlayer());
    }
}
