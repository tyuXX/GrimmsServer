package org.gsdistance.grimmsServer.Constructable.Item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Custom item implementation for relics that integrates with the new CustomItem system.
 * This wraps the existing RelicHandler functionality while providing the CustomItem interface.
 */
public class CustomRelicItem extends CustomItem {
    private final RelicHandler relicHandler;

    public CustomRelicItem(String itemId, ItemStack itemStack) {
        super(itemId, itemStack);
        this.relicHandler = RelicHandler.getRelicHandler(itemStack);
    }

    @Override
    protected void initializeItem() {
        // Relic initialization is handled by RelicHandler.makeRelic()
        // This method is called after the item is already set up as a relic
        updateDisplayName();
    }

    private void updateDisplayName() {
        ItemMeta meta = getItemMeta();
        if (meta != null) {
            String relicType = relicHandler.getRelicType();
            int tier = relicHandler.getRelicTier();
            ChatColor tierColor = switch (tier) {
                case 1 -> ChatColor.GREEN;
                case 2 -> ChatColor.BLUE;
                case 3 -> ChatColor.DARK_PURPLE;
                default -> ChatColor.WHITE;
            };
            
            String displayName = tierColor + (relicType.isEmpty() ? "" : relicType.substring(0, 1).toUpperCase() + relicType.substring(1)) + 
                                 " Relic " + ChatColor.GRAY + "[" + tierColor + "Tier " + tier + ChatColor.GRAY + "]";
            meta.setDisplayName(displayName);
            setItemMeta(meta);
        }
    }

    @Override
    public String getDisplayName() {
        ItemMeta meta = getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            return meta.getDisplayName();
        }
        return ChatColor.GOLD + "Relic";
    }

    @Override
    public String getDescription() {
        return "A powerful relic with enhanced abilities.";
    }

    // Delegate methods to RelicHandler for backward compatibility
    public RelicHandler getRelicHandler() {
        return relicHandler;
    }

    public int getRelicTier() {
        return relicHandler.getRelicTier();
    }

    public int getRelicGrade() {
        return relicHandler.getRelicGrade();
    }

    public int getRelicDurabilityResistance() {
        return relicHandler.getRelicDurabilityResistance();
    }

    public String getRelicType() {
        return relicHandler.getRelicType();
    }

    /**
     * Register relic items in the CustomItemRegistry.
     * Call this during plugin startup to enable relic integration.
     */
    public static void registerRelicTypes() {
        // Register each relic type as a custom item
        String[] relicTypes = {"sword", "pickaxe", "axe", "shovel", "hoe", "armor"};
        
        for (String type : relicTypes) {
            String itemId = "relic_" + type;
            CustomItemRegistry.registerCustomItem(itemId, itemStack -> {
                // This constructor is used when loading existing relics from NBT
                // The relic should already be initialized via RelicHandler
                return new CustomRelicItem(itemId, itemStack);
            });
        }
    }

    /**
     * Create a new relic item from a vanilla ItemStack.
     * This is the preferred way to create new relics.
     */
    public static ItemStack createRelic(ItemStack vanillaItem) {
        RelicHandler.makeRelic(vanillaItem);
        return vanillaItem;
    }
}
