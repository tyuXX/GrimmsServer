package org.gsdistance.grimmsServer.Constructable.Item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class CustomItem {
    private final String itemId;
    private final ItemStack itemStack;
    private final CustomItemHandler handler;

    public CustomItem(String itemId, ItemStack itemStack) {
        this.itemId = itemId;
        this.itemStack = itemStack;
        this.handler = CustomItemHandler.getHandler(itemStack);
        if (this.handler == null) {
            throw new IllegalArgumentException("ItemStack must be initialized with CustomItemHandler first");
        }
        this.handler.setCustomItemId(itemId);
    }

    protected abstract void initializeItem();

    public String getItemId() {
        return itemId;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public CustomItemHandler getHandler() {
        return handler;
    }

    public Material getMaterial() {
        return itemStack.getType();
    }

    public ItemMeta getItemMeta() {
        return itemStack.getItemMeta();
    }

    public void setItemMeta(ItemMeta meta) {
        itemStack.setItemMeta(meta);
    }

    public abstract String getDisplayName();

    public abstract String getDescription();
}
