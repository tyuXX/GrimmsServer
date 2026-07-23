package org.gsdistance.grimmsServer.Constructable.Item.CustomItems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.gsdistance.grimmsServer.Constructable.Item.CustomItem;
import org.gsdistance.grimmsServer.Constructable.Item.CustomItemHandler;

public class AmmoItem extends CustomItem {
    private final String ammoType;
    private final int ammoAmount;
    private final Material projectileMaterial;

    public AmmoItem(String itemId, ItemStack itemStack, String ammoType, int ammoAmount, Material projectileMaterial) {
        super(itemId, itemStack);
        this.ammoType = ammoType;
        this.ammoAmount = ammoAmount;
        this.projectileMaterial = projectileMaterial;
        this.initializeItem();
    }

    @Override
    protected void initializeItem() {
        CustomItemHandler handler = CustomItemHandler.getHandler(this.getItemStack());
        
        // Initialize ammo-specific NBT data
        handler.setCustomData("ammo_type", this.ammoType, PersistentDataType.STRING);
        handler.setCustomData("ammo_amount", this.ammoAmount, PersistentDataType.INTEGER);
        handler.setCustomData("projectile_material", this.projectileMaterial.name(), PersistentDataType.STRING);
    }

    @Override
    public String getDisplayName() {
        return ChatColor.AQUA + this.ammoType + " Ammo";
    }

    @Override
    public String getDescription() {
        return ChatColor.GRAY + "Contains " + ChatColor.YELLOW + this.ammoAmount + ChatColor.GRAY + " rounds of " + ChatColor.RED + this.ammoType;
    }

    // Getters for ammo properties
    public String getAmmoType() {
        return this.ammoType;
    }

    public int getAmmoAmount() {
        return this.ammoAmount;
    }

    public Material getProjectileMaterial() {
        return this.projectileMaterial;
    }

    public int getCurrentAmmo() {
        CustomItemHandler handler = CustomItemHandler.getHandler(this.getItemStack());
        Integer ammo = handler.getCustomData("ammo_amount", PersistentDataType.INTEGER);
        return ammo != null ? ammo : this.ammoAmount;
    }

    public void setCurrentAmmo(int ammo) {
        CustomItemHandler handler = CustomItemHandler.getHandler(this.getItemStack());
        handler.setCustomData("ammo_amount", ammo, PersistentDataType.INTEGER);
    }

    public void consumeAmmo(int amount) {
        int current = getCurrentAmmo();
        setCurrentAmmo(Math.max(0, current - amount));
    }
}
