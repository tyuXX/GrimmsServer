package org.gsdistance.grimmsServer.Constructable.Item;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Data.CustomEnchantment;
import org.gsdistance.grimmsServer.Shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        if (CustomItemHandler.isCustomItem(this.item)) {
            CustomItemHandler customItemHandler = CustomItemHandler.getHandler(this.item);
            String customItemId = customItemHandler.getCustomItemId();
            toolStats.add(ChatColor.BOLD + "Item: " + ChatColor.RESET + ChatColor.WHITE + customItemId);
            
            // Check if this is a registered custom item with additional info
            if (CustomItemRegistry.isCustomItemRegistered(customItemId)) {
                CustomItem customItem = CustomItemRegistry.getCustomItem(customItemId);
                if (customItem != null) {
                    String description = customItem.getDescription();
                    if (description != null && !description.isEmpty()) {
                        toolStats.add(ChatColor.GRAY + description);
                    }
                }
            }
        }

        // Display custom enchantments (Minecraft-style format)
        CustomEnchantmentHandler enchantHandler = CustomEnchantmentHandler.getHandler(this.item);
        Map<CustomEnchantment, Integer> enchantments = enchantHandler.getAllEnchantments();
        if (!enchantments.isEmpty()) {
            for (Map.Entry<CustomEnchantment, Integer> entry : enchantments.entrySet()) {
                CustomEnchantment enchantment = entry.getKey();
                int level = entry.getValue();
                String enchantText = ChatColor.GRAY + enchantment.enchantmentName;
                if (level > 1 || enchantment.maxLevel > 1) {
                    enchantText += " " + ChatColor.GRAY + getRomanNumeral(level);
                }
                toolStats.add(enchantText);
            }
        }

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

    private String getRomanNumeral(int number) {
        return switch (number) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            case 8 -> "VIII";
            case 9 -> "IX";
            case 10 -> "X";
            default -> String.valueOf(number);
        };
    }
}
