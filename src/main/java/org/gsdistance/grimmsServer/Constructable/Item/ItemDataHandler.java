package org.gsdistance.grimmsServer.Constructable.Item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
        return this.item.getItemMeta() != null && this.item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(this.plugin, key), type) ? this.item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(this.plugin, key), type) : null;
    }

    public <T> void setItemNBTData(String key, T value, PersistentDataType<?, T> type) {
        if (this.item.getItemMeta() != null) {
            ItemMeta meta = this.item.getItemMeta();
            meta.getPersistentDataContainer().set(new NamespacedKey(this.plugin, key), type, value);
            this.item.setItemMeta(meta);
        }

    }

    public void addItemLoreData(String lore) {
        if (this.item.getItemMeta() != null) {
            ItemMeta meta = this.item.getItemMeta();
            List<String> loreList = meta.getLore();
            if (loreList == null) {
                loreList = new ArrayList();
            }

            loreList.add(lore);
            meta.setLore(loreList);
            this.item.setItemMeta(meta);
        }

    }

    public void setItemLoreData(List<String> lore) {
        if (this.item.getItemMeta() != null) {
            ItemMeta meta = this.item.getItemMeta();
            meta.setLore(lore);
            this.item.setItemMeta(meta);
        }

    }

    public AttributeModifier getAttributeModifier(Attribute attribute, NamespacedKey key) {
        if (this.item.getItemMeta() != null && this.item.getItemMeta().getAttributeModifiers() != null) {
            Collection<AttributeModifier> modifiers = this.item.getItemMeta().getAttributeModifiers(attribute);
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
        if (this.item.getItemMeta() != null) {
            ItemMeta meta = this.item.getItemMeta();
            Multimap<Attribute, AttributeModifier> attributeModifiers = ArrayListMultimap.create();
            if (meta.getAttributeModifiers() != null) {
                attributeModifiers.putAll(meta.getAttributeModifiers());
            }

            if (attributeModifiers.containsKey(attribute)) {
                Collection<AttributeModifier> existingModifiers = attributeModifiers.get(attribute);
                existingModifiers.removeIf((existingModifier) -> existingModifier.getKey().equals(modifier.getKey()));
            }

            attributeModifiers.put(attribute, modifier);
            meta.setAttributeModifiers(attributeModifiers);
            this.item.setItemMeta(meta);
        }

    }

    public static ItemDataHandler getItemDataHandler(ItemStack item) {
        return new ItemDataHandler(item, GrimmsServer.instance);
    }
}
