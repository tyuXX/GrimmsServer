package org.gsdistance.grimmsServer.Constructable.Item;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Shared;

public class ItemStats {
   ItemStack item;
   ItemDataHandler dataHandler;

   public ItemStats(ItemStack item) {
      this.item = item;
      this.dataHandler = ItemDataHandler.getItemDataHandler(item);
   }

   public static ItemStats getItemStats(ItemStack tool) {
      return new ItemStats(tool);
   }

   public void UpdateItemStats() {
      this.dataHandler.setItemLoreData(this.getItemStats());
   }

   public List<String> getItemStats() {
      List<String> toolStats = new ArrayList();
      if (ItemLevelHandler.isItemLevelable(this.item)) {
         ItemLevelHandler levelHandler = ItemLevelHandler.getLevelHandler(this.item, (Player)null);
         toolStats.add("Level: " + Shared.formatNumber(levelHandler.getLevel()));
         String var10001 = Shared.formatNumber(Math.floor(levelHandler.getXp()));
         toolStats.add("XP: " + var10001 + "/" + Shared.formatNumber(Math.ceil(levelHandler.getXpToLevel())));
      }

      if (RelicHandler.isRelic(this.item)) {
         RelicHandler relicHandler = RelicHandler.getRelicHandler(this.item);
         toolStats.add("Type: " + relicHandler.getRelicType());
         toolStats.add("Tier: " + relicHandler.getRelicTier());
         toolStats.add("Grade: " + relicHandler.getRelicGrade());
         toolStats.add("Durability Resistance: " + relicHandler.getRelicDurabilityResistance() + "%");
      }

      return toolStats;
   }
}
