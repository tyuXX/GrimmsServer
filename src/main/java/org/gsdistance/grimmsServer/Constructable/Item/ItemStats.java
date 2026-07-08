package org.gsdistance.grimmsServer.Constructable.Item;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
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
        this.dataHandler.setItemLoreData(this.getItemStats());
    }

    public List<String> getItemStats() {
        List<String> toolStats = new ArrayList<>();
        
        if (ItemLevelHandler.isItemLevelable(this.item)) {
            ItemLevelHandler levelHandler = ItemLevelHandler.getLevelHandler(this.item, null);
            toolStats.add(ChatColor.GOLD + "Level: " + ChatColor.YELLOW + Shared.formatNumber(levelHandler.getLevel()));
            toolStats.add(ChatColor.GREEN + "XP: " + ChatColor.AQUA + Shared.formatNumber(Math.floor(levelHandler.getXp())) + 
                         ChatColor.GRAY + "/" + ChatColor.AQUA + Shared.formatNumber(Math.ceil(levelHandler.getXpToLevel())));
        }

        if (RelicHandler.isRelic(this.item)) {
            RelicHandler relicHandler = RelicHandler.getRelicHandler(this.item);
            toolStats.add(ChatColor.LIGHT_PURPLE + "Type: " + ChatColor.WHITE + relicHandler.getRelicType());
            toolStats.add(ChatColor.DARK_PURPLE + "Tier: " + ChatColor.WHITE + relicHandler.getRelicTier());
            toolStats.add(ChatColor.BLUE + "Grade: " + ChatColor.WHITE + relicHandler.getRelicGrade());
            toolStats.add(ChatColor.RED + "Durability Resistance: " + ChatColor.WHITE + relicHandler.getRelicDurabilityResistance() + "%");
        }

        return toolStats;
    }
}
