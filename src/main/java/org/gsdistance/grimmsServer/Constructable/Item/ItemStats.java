package org.gsdistance.grimmsServer.Constructable.Item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.gsdistance.grimmsServer.Shared;

import java.util.ArrayList;
import java.util.List;

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
        dataHandler.setItemLoreData(getItemStats());
    }

    public List<String> getItemStats() {
        List<String> toolStats = new ArrayList<>();
        if (ItemLevelHandler.isItemLevelable(item)) {
            ItemLevelHandler levelHandler = ItemLevelHandler.getLevelHandler(item, null);
            toolStats.add("Level: " + Shared.formatNumber(levelHandler.getLevel()));
            toolStats.add("XP: " + Shared.formatNumber(Math.floor(levelHandler.getXp())) + "/" + Shared.formatNumber(Math.ceil(levelHandler.getXpToLevel())));
        }
        if (RelicHandler.isRelic(item)) {
            RelicHandler relicHandler = RelicHandler.getRelicHandler(item);
            // TODO: Add relic stats
            toolStats.add("Type: " + relicHandler.getRelicType());
            toolStats.add("Tier: " + relicHandler.getRelicTier());
            toolStats.add("Grade: " + relicHandler.getRelicGrade());
            toolStats.add("Durability Resistance: " + relicHandler.getRelicDurabilityResistance() + "%");
        }
        return toolStats;
    }
}
