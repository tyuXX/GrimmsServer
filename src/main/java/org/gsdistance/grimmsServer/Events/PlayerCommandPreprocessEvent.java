package org.gsdistance.grimmsServer.Events;

import org.gsdistance.grimmsServer.Manage.GeneralChatHandler;

public class PlayerCommandPreprocessEvent {
    public static void Event(org.bukkit.event.player.PlayerCommandPreprocessEvent event) {
        event.setCancelled(GeneralChatHandler.handleCommand(event.getMessage(), event.getPlayer()));
    }
}
