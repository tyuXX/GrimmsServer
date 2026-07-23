package org.gsdistance.grimmsServer.Constructable.Item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Constructable.Item.CustomItems.AmmoItem;
import org.gsdistance.grimmsServer.Constructable.Item.CustomItems.GunItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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

    public static void registerGunTypes() {
        // Register pistol
        registerCustomItem("gun_pistol", (itemStack) -> 
            new GunItem("gun_pistol", itemStack, "Pistol", 5.0, 2.0, 12, Material.IRON_NUGGET));
        
        // Register rifle
        registerCustomItem("gun_rifle", (itemStack) -> 
            new GunItem("gun_rifle", itemStack, "Rifle", 8.0, 4.0, 30, Material.GOLD_NUGGET));
        
        // Register shotgun
        registerCustomItem("gun_shotgun", (itemStack) -> 
            new GunItem("gun_shotgun", itemStack, "Shotgun", 15.0, 1.0, 8, Material.IRON_INGOT));
    }

    public static void registerAmmoTypes() {
        // Register pistol ammo
        registerCustomItem("ammo_pistol", (itemStack) -> 
            new AmmoItem("ammo_pistol", itemStack, "Pistol", 30, Material.IRON_NUGGET));
        
        // Register rifle ammo
        registerCustomItem("ammo_rifle", (itemStack) -> 
            new AmmoItem("ammo_rifle", itemStack, "Rifle", 60, Material.GOLD_NUGGET));
        
        // Register shotgun ammo
        registerCustomItem("ammo_shotgun", (itemStack) -> 
            new AmmoItem("ammo_shotgun", itemStack, "Shotgun", 16, Material.IRON_INGOT));
    }

    public static Set<String> getRegisteredItemIds() {
        return itemConstructors.keySet();
    }
}
