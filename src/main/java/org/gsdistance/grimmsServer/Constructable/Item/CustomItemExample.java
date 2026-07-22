package org.gsdistance.grimmsServer.Constructable.Item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Example custom item implementation showing how to create and register custom items.
 * This is a template for creating your own custom items.
 */
public class CustomItemExample extends CustomItem {
    
    public CustomItemExample(String itemId, ItemStack itemStack) {
        super(itemId, itemStack);
    }

    @Override
    protected void initializeItem() {
        // Set up the item's initial properties
        ItemMeta meta = getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Custom Example Item");
            meta.setCustomModelData(1001); // Optional: Set custom model data for resource packs
            setItemMeta(meta);
        }
        
        // Store custom data specific to this item type
        getHandler().setCustomData("exampleValue", 42, org.bukkit.persistence.PersistentDataType.INTEGER);
    }

    @Override
    public String getDisplayName() {
        return ChatColor.GOLD + "Custom Example Item";
    }

    @Override
    public String getDescription() {
        return "An example custom item with special properties.";
    }
    
    /**
     * Example method to register this custom item.
     * Call this during your plugin's onEnable() method.
     */
    public static void register() {
        CustomItemRegistry.registerCustomItem("custom_example", itemStack -> {
            // Create the ItemStack (can be any material)
            ItemStack stack = new ItemStack(Material.DIAMOND_SWORD);
            
            // Initialize it as a custom item
            CustomItemHandler.createHandler(stack);
            
            // Create and return the custom item instance
            return new CustomItemExample("custom_example", stack);
        });
    }
    
    /**
     * Example method to create an instance of this custom item.
     */
    public static ItemStack create() {
        ItemStack stack = new ItemStack(Material.DIAMOND_SWORD);
        CustomItemHandler.createHandler(stack);
        return CustomItemRegistry.createCustomItem("custom_example", stack).getItemStack();
    }
}
