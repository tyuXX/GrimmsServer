package org.gsdistance.grimmsServer.Constructable;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.GrimmsServer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemDataHandler {
    private final ItemStack item;
    private final JavaPlugin plugin;

    public ItemDataHandler(ItemStack item, @Nullable JavaPlugin plugin) {
        this.item = item;
        this.plugin = plugin != null ? plugin : GrimmsServer.instance;
    }

    public Object getItemNBTData(String key, PersistentDataType<?, ?> type) {
        if (item.getItemMeta() != null && item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, key), type)) {
            return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, key), type);
        }
        return null;
    }

    public void setItemNBTData(String key, Boolean value) {
        if (item.getItemMeta() != null) {
            var meta = item.getItemMeta(); // Get the ItemMeta
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, key), PersistentDataType.BOOLEAN, value);
            item.setItemMeta(meta); // Set the modified ItemMeta back to the ItemStack
        }
    }

    public void setItemNBTData(String key, Double value) {
        if (item.getItemMeta() != null) {
            var meta = item.getItemMeta(); // Get the ItemMeta
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, key), PersistentDataType.DOUBLE, value);
            item.setItemMeta(meta); // Set the modified ItemMeta back to the ItemStack
        }
    }

    public void setItemNBTData(String key, String value) {
        if (item.getItemMeta() != null) {
            var meta = item.getItemMeta(); // Get the ItemMeta
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, key), PersistentDataType.STRING, value);
            item.setItemMeta(meta); // Set the modified ItemMeta back to the ItemStack
        }
    }

    public void addItemLoreData(String lore) {
        if (item.getItemMeta() != null) {
            var meta = item.getItemMeta(); // Get the ItemMeta
            var loreList = meta.getLore();
            if (loreList == null) {
                loreList = new ArrayList<>();
            }
            loreList.add(lore);
            meta.setLore(loreList);
            item.setItemMeta(meta); // Set the modified ItemMeta back to the ItemStack
        }
    }

    public void setItemLoreData(List<String> lore) {
        if (item.getItemMeta() != null) {
            var meta = item.getItemMeta(); // Get the ItemMeta
            meta.setLore(lore);
            item.setItemMeta(meta); // Set the modified ItemMeta back to the ItemStack
        }
    }

    public static ItemDataHandler getItemDataHandler(ItemStack item) {
        return new ItemDataHandler(item, GrimmsServer.instance);
    }
}
