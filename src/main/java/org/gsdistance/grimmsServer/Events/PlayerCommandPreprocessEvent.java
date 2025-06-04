package org.gsdistance.grimmsServer.Events;

import org.gsdistance.grimmsServer.Manage.CommandRegistry;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.logging.Level;

public class PlayerCommandPreprocessEvent {
    public static void Event(org.bukkit.event.player.PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().split(" ")[0].substring(1); // Extract the command
        if (!CommandRegistry.CanExecute(command)) {
            event.setCancelled(true);
            GrimmsServer.logger.log(Level.INFO, "Command interrupted: '" + event.getMessage() + "' by player: " + event.getPlayer().getName() + ". Command is disabled in the config.");
        }
    }
}
