package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Commands.GAuthCommand.GAuthBaseCommand;
import org.gsdistance.grimmsServer.Constructable.Item.ItemLevelHandler;
import org.gsdistance.grimmsServer.Data.StaticLists;

public class EntityDamageByEntityEvent {
    public EntityDamageByEntityEvent() {
    }

    public static void Event(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() == EntityType.PLAYER) {
            Player player = (Player) event.getDamager();
            if (!GAuthBaseCommand.isLoggedIn(player)) {
                player.sendMessage(ChatColor.RED + "You must login with /gAuth login <password> to perform this action.");
                event.setCancelled(true);
                return;
            }
        }

        if (event.getDamager().getType() == EntityType.PLAYER && !StaticLists.xpBlacklist.contains(event.getEntityType()) && ItemLevelHandler.isItemLevelable(((Player) event.getDamager()).getInventory().getItemInMainHand())) {
            ItemLevelHandler.getLevelHandler((Player) event.getDamager()).addXp(event.getDamage());
        }

    }
}
