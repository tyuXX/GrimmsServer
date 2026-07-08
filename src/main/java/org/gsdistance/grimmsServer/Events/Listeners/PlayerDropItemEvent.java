package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.ChatColor;
import org.gsdistance.grimmsServer.Commands.GAuthCommand.GAuthBaseCommand;

public class PlayerDropItemEvent {
    public PlayerDropItemEvent() {
    }

    public static void Event(org.bukkit.event.player.PlayerDropItemEvent event) {
        if (!GAuthBaseCommand.isLoggedIn(event.getPlayer())) {
            event.getPlayer().sendMessage(ChatColor.RED + "You must login with /gAuth login <password> to perform this action.");
            event.setCancelled(true);
        }
    }
}
