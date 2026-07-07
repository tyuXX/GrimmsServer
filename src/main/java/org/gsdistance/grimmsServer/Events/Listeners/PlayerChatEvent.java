package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.gsdistance.grimmsServer.Manage.GeneralChatHandler;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class PlayerChatEvent {
    public PlayerChatEvent() {
    }

    public static void Event(AsyncPlayerChatEvent event) {
        PlayerStats.getPlayerStats(event.getPlayer()).changeStat("sent_messages", 1);
        event.setFormat(GeneralChatHandler.handleMessage(event.getMessage(), event.getPlayer()));
    }
}
