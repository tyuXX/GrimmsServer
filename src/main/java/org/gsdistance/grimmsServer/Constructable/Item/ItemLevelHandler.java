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
        initializeItemLevelable();
    }

    public static ItemLevelHandler getLevelHandler(Player player) {
        return new ItemLevelHandler(player.getInventory().getItemInMainHand(), player, GrimmsServer.instance);
    }

    public static ItemLevelHandler getLevelHandler(ItemStack item, Player player) {
        return new ItemLevelHandler(item, player, GrimmsServer.instance);
    }

    public static boolean isItemLevelable(ItemStack item) {
        ItemDataHandler dataHandler = new ItemDataHandler(item, GrimmsServer.instance);
        Boolean isLevelable = dataHandler.getItemNBTData(LEVELABLE_KEY, PersistentDataType.BOOLEAN);
        return isLevelable != null && isLevelable;
    }

    private void initializeItemLevelable() {
        if (!isItemLevelable(item)) {
            dataHandler.setItemNBTData(LEVEL_KEY, 0.0, PersistentDataType.DOUBLE);
            dataHandler.setItemNBTData(XP_KEY, 0.0, PersistentDataType.DOUBLE);
            dataHandler.setItemNBTData(LEVELABLE_KEY, true, PersistentDataType.BOOLEAN);
        }
    }

    public double getLevel() {
        Double level = dataHandler.getItemNBTData(LEVEL_KEY, PersistentDataType.DOUBLE);
        return level != null ? level : 0.0;
    }

    public void changeLevel(double levelDelta) {
        double currentLevel = getLevel();
        dataHandler.setItemNBTData(LEVEL_KEY, currentLevel + levelDelta, PersistentDataType.DOUBLE);
        Damageable damageable = (Damageable) item.getItemMeta();
        assert damageable != null;
        damageable.setDamage(0);
        item.setItemMeta(damageable);
    }

    public void addXp(double xp) {
        double totalXp = getXp() + xp;
        int levelUps = 0;
        while (totalXp >= getXpToLevel()) {
            totalXp -= getXpToLevel();
            changeLevel(1);
            levelUps++;
        }
        setXp(totalXp);
        ItemStats.getItemStats(item).UpdateItemStats();
        if (levelUps > 0) {
            player.sendMessage("Your " + item.getType() + " has leveled up to " + getLevel() + "!");
        }
    }

    public double getXpToLevel() {
        return Math.pow(getLevel(), 1.5) * 100;
    }

    public double getXp() {
        Double xp = dataHandler.getItemNBTData(XP_KEY, PersistentDataType.DOUBLE);
        return xp != null ? xp : 0.0;
    }

    public void setXp(double xp) {
        dataHandler.setItemNBTData(XP_KEY, xp, PersistentDataType.DOUBLE);
    }
}
