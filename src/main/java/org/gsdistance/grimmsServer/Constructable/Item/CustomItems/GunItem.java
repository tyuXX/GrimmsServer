package org.gsdistance.grimmsServer.Constructable.Item.CustomItems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.gsdistance.grimmsServer.Constructable.Item.CustomItem;
import org.gsdistance.grimmsServer.Constructable.Item.CustomItemHandler;

public class GunItem extends CustomItem {
    private final String gunType;
    private final double damage;
    private final double fireRate;
    private final int maxAmmo;
    private final Material projectileType;

    public GunItem(String itemId, ItemStack itemStack, String gunType, double damage, double fireRate, int maxAmmo, Material projectileType) {
        super(itemId, itemStack);
        this.gunType = gunType;
        this.damage = damage;
        this.fireRate = fireRate;
        this.maxAmmo = maxAmmo;
        this.projectileType = projectileType;
        this.initializeItem();
    }

    @Override
    protected void initializeItem() {
        CustomItemHandler handler = CustomItemHandler.getHandler(this.getItemStack());
        
        // Initialize gun-specific NBT data
        handler.setCustomData("gun_type", this.gunType, PersistentDataType.STRING);
        handler.setCustomData("damage", this.damage, PersistentDataType.DOUBLE);
        handler.setCustomData("fire_rate", this.fireRate, PersistentDataType.DOUBLE);
        handler.setCustomData("max_ammo", this.maxAmmo, PersistentDataType.INTEGER);
        handler.setCustomData("current_ammo", this.maxAmmo, PersistentDataType.INTEGER);
        handler.setCustomData("projectile_type", this.projectileType.name(), PersistentDataType.STRING);
    }

    @Override
    public String getDisplayName() {
        return ChatColor.GOLD + this.gunType + " Gun";
    }

    @Override
    public String getDescription() {
        return ChatColor.GRAY + "Damage: " + ChatColor.RED + this.damage + 
               ChatColor.GRAY + " | Fire Rate: " + ChatColor.YELLOW + this.fireRate + "/s" +
               ChatColor.GRAY + " | Ammo: " + ChatColor.AQUA + this.maxAmmo;
    }

    // Getters for gun properties
    public String getGunType() {
        return this.gunType;
    }

    public double getDamage() {
        return this.damage;
    }

    public double getFireRate() {
        return this.fireRate;
    }

    public int getMaxAmmo() {
        return this.maxAmmo;
    }

    public int getCurrentAmmo() {
        CustomItemHandler handler = CustomItemHandler.getHandler(this.getItemStack());
        Integer ammo = handler.getCustomData("current_ammo", PersistentDataType.INTEGER);
        return ammo != null ? ammo : this.maxAmmo;
    }

    public void setCurrentAmmo(int ammo) {
        CustomItemHandler handler = CustomItemHandler.getHandler(this.getItemStack());
        handler.setCustomData("current_ammo", ammo, PersistentDataType.INTEGER);
    }

    public Material getProjectileType() {
        return this.projectileType;
    }
}
