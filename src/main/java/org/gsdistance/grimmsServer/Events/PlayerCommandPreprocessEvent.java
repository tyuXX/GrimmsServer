package org.gsdistance.grimmsServer.Events;

import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Manage.CommandRegistry;
import org.gsdistance.grimmsServer.Manage.GeneralChatHandler;

import java.util.logging.Level;

public class PlayerCommandPreprocessEvent {
    public static void Event(org.bukkit.event.player.PlayerCommandPreprocessEvent event) {
        event.setCancelled(GeneralChatHandler.handleCommand(event.getMessage(), event.getPlayer()));
    }
}
