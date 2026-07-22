package org.gsdistance.grimmsServer.Constructable.Item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.GrimmsServer;

import javax.annotation.Nullable;

public class CustomItemHandler {
    private final ItemStack item;
    private final ItemDataHandler dataHandler;
    private static final String CUSTOM_ITEM_KEY = "customItemId";
    private static final String IS_CUSTOM_ITEM_KEY = "isCustomItem";

    public CustomItemHandler(ItemStack item, @Nullable JavaPlugin plugin) {
        this.item = item;
        this.dataHandler = new ItemDataHandler(item, plugin);
    }

    public static CustomItemHandler getHandler(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return null;
        }
        return new CustomItemHandler(item, GrimmsServer.instance);
    }

    public static CustomItemHandler createHandler(ItemStack item) {
        CustomItemHandler handler = new CustomItemHandler(item, GrimmsServer.instance);
        handler.initializeCustomItem();
        return handler;
    }

    private void initializeCustomItem() {
        if (!isCustomItem(this.item)) {
            this.dataHandler.setItemNBTData(IS_CUSTOM_ITEM_KEY, true, PersistentDataType.BOOLEAN);
            // Set default item ID to vanilla material name
            String vanillaId = CustomItemRegistry.getVanillaItemId(this.item.getType());
            this.dataHandler.setItemNBTData(CUSTOM_ITEM_KEY, vanillaId, PersistentDataType.STRING);
        }
    }

    public static boolean isCustomItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        ItemDataHandler dataHandler = new ItemDataHandler(item, GrimmsServer.instance);
        Boolean isCustom = dataHandler.getItemNBTData(IS_CUSTOM_ITEM_KEY, PersistentDataType.BOOLEAN);
        return isCustom != null && isCustom;
    }

    public String getCustomItemId() {
        return this.dataHandler.getItemNBTData(CUSTOM_ITEM_KEY, PersistentDataType.STRING);
    }

    public void setCustomItemId(String itemId) {
        this.dataHandler.setItemNBTData(CUSTOM_ITEM_KEY, itemId, PersistentDataType.STRING);
    }

    public <T> T getCustomData(String key, PersistentDataType<?, T> type) {
        return this.dataHandler.getItemNBTData("custom_" + key, type);
    }

    public <T> void setCustomData(String key, T value, PersistentDataType<?, T> type) {
        this.dataHandler.setItemNBTData("custom_" + key, value, type);
    }

    public ItemStack getItemStack() {
        return item;
    }

    public ItemDataHandler getDataHandler() {
        return dataHandler;
    }
}
