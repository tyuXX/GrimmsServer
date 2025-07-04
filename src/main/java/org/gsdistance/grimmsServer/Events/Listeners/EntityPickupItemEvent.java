package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.Player.PlayerCapability;

public class EntityPickupItemEvent {
    public static void Event(org.bukkit.event.entity.EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player){
            PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
            if(playerMetadata.capabilities.containsKey(PlayerCapability.AUTOSELL) && playerMetadata.settings.contains("autosell")){
                Market market = Market.getMarket();
                market.unsafeSell(event.getItem().getItemStack(), player);
                market.saveMarket();
                // Remove the item from the world
                event.getItem().remove();
                event.setCancelled(true);
            }
        }
    }
}
