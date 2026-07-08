package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.gsdistance.grimmsServer.Commands.GAuthCommand.GAuthBaseCommand;
import org.gsdistance.grimmsServer.Manage.GeneralChatHandler;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class PlayerChatEvent {
    public PlayerChatEvent() {
    }

    public static void Event(AsyncPlayerChatEvent event) {
        if (!GAuthBaseCommand.isLoggedIn(event.getPlayer())) {
            event.getPlayer().sendMessage(ChatColor.RED + "You must login with /gAuth login <password> to perform this action.");
            event.setCancelled(true);
            return;
        }

        PlayerStats.getPlayerStats(event.getPlayer()).changeStat("sent_messages", 1);
        event.setFormat(GeneralChatHandler.handleMessage(event.getMessage(), event.getPlayer()));
    }
}
