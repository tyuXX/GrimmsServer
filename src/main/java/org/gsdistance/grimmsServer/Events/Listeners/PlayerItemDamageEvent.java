package org.gsdistance.grimmsServer.Events.Listeners;

import java.util.Random;
import org.gsdistance.grimmsServer.Constructable.Item.ItemLevelHandler;
import org.gsdistance.grimmsServer.Constructable.Item.RelicHandler;

public class PlayerItemDamageEvent {
   public PlayerItemDamageEvent() {
   }

   public static void Event(org.bukkit.event.player.PlayerItemDamageEvent event) {
      if (RelicHandler.isRelic(event.getItem())) {
         RelicHandler relicHandler = RelicHandler.getRelicHandler(event.getItem());
         if ((new Random()).nextInt(0, 100) < relicHandler.getRelicDurabilityResistance()) {
            event.setCancelled(true);
            return;
         }
      }

      if (ItemLevelHandler.isItemLevelable(event.getItem())) {
         ItemLevelHandler.getLevelHandler(event.getItem(), event.getPlayer()).addXp((double)event.getDamage());
      }

   }
}
