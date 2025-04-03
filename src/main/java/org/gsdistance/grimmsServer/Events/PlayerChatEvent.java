package org.gsdistance.grimmsServer.Events;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class PlayerChatEvent {
    public static void Event(AsyncPlayerChatEvent event) {
        PlayerStats.getPlayerStats(event.getPlayer()).changeStat("sent_messages", 1);
    }
}
