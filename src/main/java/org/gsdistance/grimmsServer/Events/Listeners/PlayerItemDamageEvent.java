package org.gsdistance.grimmsServer.Events.Listeners;

import org.gsdistance.grimmsServer.Constructable.Item.RelicHandler;

import java.util.Random;

public class PlayerItemDamageEvent {
    public static void Event(org.bukkit.event.player.PlayerItemDamageEvent event) {
        if (RelicHandler.isRelic(event.getItem())){
            RelicHandler relicHandler = RelicHandler.getRelicHandler(event.getItem());
            if((new Random().nextInt(0, 100) < relicHandler.getRelicDurabilityResistance())){
                event.setCancelled(true);
                return;
            }
        }
        // Handle rest
    }
}
