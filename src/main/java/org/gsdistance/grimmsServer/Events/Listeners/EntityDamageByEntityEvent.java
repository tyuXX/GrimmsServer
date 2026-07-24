package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Commands.GAuthCommand.GAuthBaseCommand;

public class EntityDamageByEntityEvent {
    public EntityDamageByEntityEvent() {
    }

    public static void Event(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        // GAuth check for player damagers
        if (event.getDamager().getType() == EntityType.PLAYER) {
            Player player = (Player) event.getDamager();
            if (!GAuthBaseCommand.isLoggedIn(player)) {
                player.sendMessage(ChatColor.RED + "You must login with /gAuth login <password> to perform this action.");
                event.setCancelled(true);
                return;
            }
        }
    }
}
