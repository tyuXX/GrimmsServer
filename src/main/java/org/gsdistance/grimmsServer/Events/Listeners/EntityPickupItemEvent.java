package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Commands.GAuthCommand.GAuthBaseCommand;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.Player.PlayerCapability;

public class EntityPickupItemEvent {
    public EntityPickupItemEvent() {
    }

    public static void Event(org.bukkit.event.entity.EntityPickupItemEvent event) {
        LivingEntity var2 = event.getEntity();
        if (var2 instanceof Player player) {
            if (!GAuthBaseCommand.isLoggedIn(player)) {
                player.sendMessage(ChatColor.RED + "You must login with /gAuth login <password> to perform this action.");
                event.setCancelled(true);
                return;
            }

            PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
            if (playerMetadata.capabilities.containsKey(PlayerCapability.AUTOSELL) && playerMetadata.settings.contains(PlayerCapability.AUTOSELL.capabilityId)) {
                Market market = Market.getMarket();
                market.unsafeSell(event.getItem().getItemStack(), player);
                market.saveMarket();
                event.getItem().remove();
                event.setCancelled(true);
            }
        }

    }
}
