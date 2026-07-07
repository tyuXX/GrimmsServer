package org.gsdistance.grimmsServer.Constructable.Item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.GrimmsServer;

public class ItemLevelHandler {
    private final ItemStack item;
    private final Player player;
    private final ItemDataHandler dataHandler;
    private static final String LEVEL_KEY = "level";
    private static final String XP_KEY = "xp";
    private static final String LEVELABLE_KEY = "isLevelable";

    public ItemLevelHandler(ItemStack item, Player player, JavaPlugin plugin) {
        this.item = item;
        this.player = player;
        this.dataHandler = new ItemDataHandler(item, plugin);
        this.initializeItemLevelable();
    }

    public static ItemLevelHandler getLevelHandler(Player player) {
        return new ItemLevelHandler(player.getInventory().getItemInMainHand(), player, GrimmsServer.instance);
    }

    public static ItemLevelHandler getLevelHandler(ItemStack item, Player player) {
        return new ItemLevelHandler(item, player, GrimmsServer.instance);
    }

    public static boolean isItemLevelable(ItemStack item) {
        ItemDataHandler dataHandler = new ItemDataHandler(item, GrimmsServer.instance);
        Boolean isLevelable = dataHandler.getItemNBTData("isLevelable", PersistentDataType.BOOLEAN);
        return isLevelable != null && isLevelable;
    }

    private void initializeItemLevelable() {
        if (!isItemLevelable(this.item)) {
            this.dataHandler.setItemNBTData("level", (double) 0.0F, PersistentDataType.DOUBLE);
            this.dataHandler.setItemNBTData("xp", (double) 0.0F, PersistentDataType.DOUBLE);
            this.dataHandler.setItemNBTData("isLevelable", true, PersistentDataType.BOOLEAN);
        }

    }

    public double getLevel() {
        Double level = this.dataHandler.getItemNBTData("level", PersistentDataType.DOUBLE);
        return level != null ? level : (double) 0.0F;
    }

    public void changeLevel(double levelDelta) {
        double currentLevel = this.getLevel();
        this.dataHandler.setItemNBTData("level", currentLevel + levelDelta, PersistentDataType.DOUBLE);
        Damageable damageable = (Damageable) this.item.getItemMeta();

        assert damageable != null;

        damageable.setDamage(0);
        this.item.setItemMeta(damageable);
    }

    public void addXp(double xp) {
        double totalXp = this.getXp() + xp;

        int levelUps;
        for (levelUps = 0; totalXp >= this.getXpToLevel(); ++levelUps) {
            totalXp -= this.getXpToLevel();
            this.changeLevel(1.0F);
        }

        this.setXp(totalXp);
        ItemStats.getItemStats(this.item).UpdateItemStats();
        if (levelUps > 0) {
            Player var10000 = this.player;
            String var10001 = String.valueOf(this.item.getType());
            var10000.sendMessage("Your " + var10001 + " has leveled up to " + this.getLevel() + "!");
        }

    }

    public double getXpToLevel() {
        return Math.pow(this.getLevel(), 1.5F) * (double) 100.0F;
    }

    public double getXp() {
        Double xp = this.dataHandler.getItemNBTData("xp", PersistentDataType.DOUBLE);
        return xp != null ? xp : (double) 0.0F;
    }

    public void setXp(double xp) {
        this.dataHandler.setItemNBTData("xp", xp, PersistentDataType.DOUBLE);
    }
}
