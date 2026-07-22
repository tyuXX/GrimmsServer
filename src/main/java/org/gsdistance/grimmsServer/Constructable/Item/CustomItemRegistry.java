package org.gsdistance.grimmsServer.Constructable.Item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CustomItemRegistry {
    private static final Map<String, CustomItem> customItems = new HashMap<>();
    private static final Map<String, Function<ItemStack, CustomItem>> itemConstructors = new HashMap<>();

    public static void registerCustomItem(String itemId, Function<ItemStack, CustomItem> constructor) {
        if (customItems.containsKey(itemId)) {
            throw new IllegalArgumentException("Custom item with ID " + itemId + " is already registered");
        }
        itemConstructors.put(itemId, constructor);
    }

    public static CustomItem createCustomItem(String itemId, ItemStack itemStack) {
        Function<ItemStack, CustomItem> constructor = itemConstructors.get(itemId);
        if (constructor == null) {
            throw new IllegalArgumentException("No custom item registered with ID " + itemId);
        }
        CustomItem customItem = constructor.apply(itemStack);
        customItems.put(itemId, customItem);
        return customItem;
    }

    public static boolean isCustomItemRegistered(String itemId) {
        return itemConstructors.containsKey(itemId);
    }

    public static CustomItem getCustomItem(String itemId) {
        return customItems.get(itemId);
    }

    public static String getItemId(ItemStack itemStack) {
        CustomItemHandler handler = CustomItemHandler.getHandler(itemStack);
        if (handler == null) {
            return null;
        }
        return handler.getCustomItemId();
    }

    public static boolean isCustomItem(ItemStack itemStack) {
        return getItemId(itemStack) != null;
    }

    public static String getVanillaItemId(Material material) {
        return material.name().toLowerCase();
    }
}
