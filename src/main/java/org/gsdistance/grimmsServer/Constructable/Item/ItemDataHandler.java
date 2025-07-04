package org.gsdistance.grimmsServer.Constructable.Item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.GrimmsServer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemDataHandler {
    private final ItemStack item;
    private final JavaPlugin plugin;

    public ItemDataHandler(ItemStack item, @Nullable JavaPlugin plugin) {
        this.item = item;
        this.plugin = plugin != null ? plugin : GrimmsServer.instance;
    }

    public <T> T getItemNBTData(String key, PersistentDataType<?, T> type) {
        if (item.getItemMeta() != null && item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, key), type)) {
            return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, key), type);
        }
        return null;
    }

    public <T> void setItemNBTData(String key, T value, PersistentDataType<?, T> type) {
        if (item.getItemMeta() != null) {
            var meta = item.getItemMeta(); // Get the ItemMeta
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, key), type, value);
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


    // TODO - Implement methods to get and set attributes
    public AttributeModifier getAttributeModifier(Attribute attribute, NamespacedKey key) {
        if (item.getItemMeta() != null && item.getItemMeta().getAttributeModifiers() != null) {
            Collection<AttributeModifier> modifiers = item.getItemMeta().getAttributeModifiers(attribute);
            if (modifiers != null) {
                for (AttributeModifier modifier : modifiers) {
                    if (modifier.getKey().equals(key)) {
                        return modifier;
                    }
                }
            }
        }
        return null;
    }

    public void setAttribute(Attribute attribute, AttributeModifier modifier) {
        if (item.getItemMeta() != null) {
            var meta = item.getItemMeta(); // Get the ItemMeta
            Multimap<Attribute, AttributeModifier> attributeModifiers = ArrayListMultimap.create();
            if (meta.getAttributeModifiers() != null) {
                attributeModifiers.putAll(meta.getAttributeModifiers());
            }
            // Remove existing modifier with the same key if it exists
            if (attributeModifiers.containsKey(attribute)) {
                Collection<AttributeModifier> existingModifiers = attributeModifiers.get(attribute);
                existingModifiers.removeIf(existingModifier -> existingModifier.getKey().equals(modifier.getKey()));
            }
            attributeModifiers.put(attribute, modifier);
            meta.setAttributeModifiers(attributeModifiers); // Set the modified attribute modifiers
            item.setItemMeta(meta); // Set the modified ItemMeta back to the ItemStack
        }
    }

    public static ItemDataHandler getItemDataHandler(ItemStack item) {
        return new ItemDataHandler(item, GrimmsServer.instance);
    }
}
