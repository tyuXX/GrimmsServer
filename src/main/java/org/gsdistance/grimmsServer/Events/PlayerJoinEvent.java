package org.gsdistance.grimmsServer.Events;

import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class PlayerJoinEvent {
    public static void Event(org.bukkit.event.player.PlayerJoinEvent event) {
        PlayerStats.getPlayerStats(event.getPlayer()).changeStat("join_count", 1);
        event.getPlayer().sendMessage("Welcome to Grimm's Server!");
    }
}
