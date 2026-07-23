package org.gsdistance.grimmsServer.Constructable.Item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.gsdistance.grimmsServer.Data.CustomEnchantment;

import java.util.HashMap;
import java.util.Map;

public class CustomEnchantmentHandler {
    private final ItemStack item;
    private final ItemDataHandler dataHandler;
    private static final String ENCHANTMENT_PREFIX = "custom_enchant_";

    public CustomEnchantmentHandler(ItemStack item) {
        this.item = item;
        this.dataHandler = ItemDataHandler.getItemDataHandler(item);
    }

    public static CustomEnchantmentHandler getHandler(ItemStack item) {
        return new CustomEnchantmentHandler(item);
    }

    /**
     * Add a custom enchantment to the item
     * @param enchantment The enchantment to add
     * @param level The level of the enchantment
     * @return true if successful, false if level exceeds max or enchantment already exists
     */
    public boolean addEnchantment(CustomEnchantment enchantment, int level) {
        if (level < 1 || level > enchantment.maxLevel) {
            return false;
        }
        
        if (hasEnchantment(enchantment)) {
            return false;
        }

        String key = ENCHANTMENT_PREFIX + enchantment.name();
        dataHandler.setItemNBTData(key, level, PersistentDataType.INTEGER);
        return true;
    }

    /**
     * Remove a custom enchantment from the item
     * @param enchantment The enchantment to remove
     * @return true if the enchantment was removed, false if it didn't exist
     */
    public boolean removeEnchantment(CustomEnchantment enchantment) {
        if (!hasEnchantment(enchantment)) {
            return false;
        }

        String key = ENCHANTMENT_PREFIX + enchantment.name();
        // To remove, we set it to null or use a different approach
        // Since ItemDataHandler doesn't have a remove method, we'll need to handle this
        // For now, let's implement a workaround by setting to 0
        dataHandler.setItemNBTData(key, 0, PersistentDataType.INTEGER);
        return true;
    }

    /**
     * Check if the item has a specific custom enchantment
     * @param enchantment The enchantment to check for
     * @return true if the item has the enchantment with level > 0
     */
    public boolean hasEnchantment(CustomEnchantment enchantment) {
        return getEnchantmentLevel(enchantment) > 0;
    }

    /**
     * Get the level of a custom enchantment on the item
     * @param enchantment The enchantment to check
     * @return The level of the enchantment, or 0 if not present
     */
    public int getEnchantmentLevel(CustomEnchantment enchantment) {
        String key = ENCHANTMENT_PREFIX + enchantment.name();
        Integer level = dataHandler.getItemNBTData(key, PersistentDataType.INTEGER);
        return level != null ? level : 0;
    }

    /**
     * Get all custom enchantments on the item
     * @return A map of enchantments to their levels
     */
    public Map<CustomEnchantment, Integer> getAllEnchantments() {
        Map<CustomEnchantment, Integer> enchantments = new HashMap<>();
        
        for (CustomEnchantment enchantment : CustomEnchantment.values()) {
            int level = getEnchantmentLevel(enchantment);
            if (level > 0) {
                enchantments.put(enchantment, level);
            }
        }
        
        return enchantments;
    }

    /**
     * Set the level of an enchantment (overwrites existing)
     * @param enchantment The enchantment to set
     * @param level The level to set
     * @return true if successful, false if level is invalid
     */
    public boolean setEnchantmentLevel(CustomEnchantment enchantment, int level) {
        if (level < 0 || level > enchantment.maxLevel) {
            return false;
        }

        String key = ENCHANTMENT_PREFIX + enchantment.name();
        dataHandler.setItemNBTData(key, level, PersistentDataType.INTEGER);
        return true;
    }

    /**
     * Clear all custom enchantments from the item
     */
    public void clearAllEnchantments() {
        for (CustomEnchantment enchantment : CustomEnchantment.values()) {
            setEnchantmentLevel(enchantment, 0);
        }
    }

    /**
     * Check if the item has any custom enchantments
     * @return true if the item has at least one custom enchantment
     */
    public boolean hasAnyEnchantments() {
        return !getAllEnchantments().isEmpty();
    }
}
