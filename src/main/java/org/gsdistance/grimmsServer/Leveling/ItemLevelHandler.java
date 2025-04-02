package org.gsdistance.grimmsServer.Leveling;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.logging.Level;

public class ItemLevelHandler {
    private final ItemStack item;
    private final Player player;
    private final NamespacedKey levelKey;
    private final NamespacedKey xpKey;
    private final NamespacedKey levelableKey;

    public ItemLevelHandler(ItemStack item, Player player, JavaPlugin plugin) {
        this.item = item;
        this.player = player;
        this.levelKey = new NamespacedKey(plugin, "level");
        this.xpKey = new NamespacedKey(plugin, "xp");
        this.levelableKey = new NamespacedKey(plugin, "isLevelable");
        MakeItemLevelable();
    }

    public static ItemLevelHandler getLevelHandler(ItemStack item, Player player) {
        return new ItemLevelHandler(item, player, GrimmsServer.instance);
    }

    public static ItemLevelHandler getLevelHandler(Player player) {
        return new ItemLevelHandler(player.getInventory().getItemInMainHand(), player, GrimmsServer.instance);
    }

    public static boolean isItemLevelable(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            PersistentDataContainer data = meta.getPersistentDataContainer();
            return data.has(new NamespacedKey(GrimmsServer.instance, "isLevelable"), PersistentDataType.BOOLEAN);
        }
        return false;
    }

    public void MakeItemLevelable() {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(levelKey, PersistentDataType.DOUBLE, 0.0);
            data.set(xpKey, PersistentDataType.DOUBLE, 0.0);
            data.set(levelableKey, PersistentDataType.BOOLEAN, true);
            item.setItemMeta(meta);
        }
    }

    public double getLevel() {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            PersistentDataContainer data = meta.getPersistentDataContainer();
            return data.getOrDefault(levelKey, PersistentDataType.DOUBLE, 0.0);
        }
        return 0.0;
    }

    public void changeLevel(double level) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            PersistentDataContainer data = meta.getPersistentDataContainer();
            double currentLevel = data.getOrDefault(levelKey, PersistentDataType.DOUBLE, 0.0);
            GrimmsServer.logger.log(Level.INFO,"Current level: " + currentLevel);
            data.set(levelKey, PersistentDataType.DOUBLE, currentLevel + level);
            item.setItemMeta(meta);
        }
    }

    // TODO - Fix this idk
    public int addXp(double xp) {
        double exp = xp + getXp();
        int levelUps = 0;
        while (exp > getXpToLevel()) {
            exp -= getXpToLevel();
            GrimmsServer.logger.log(Level.INFO,"Current level: " + getLevel());
            changeLevel(1);
            GrimmsServer.logger.log(Level.INFO,"Current level: " + getLevel());
            levelUps++;
        }
        GrimmsServer.logger.log(Level.INFO,"Current level: " + getLevel());
        setXp(exp);
        GrimmsServer.logger.log(Level.INFO,"Current level: " + getLevel());
        if (levelUps > 0) {
            player.sendMessage("Your " + item.getType().name().toLowerCase() + " has leveled up to " + getLevel() + "!");
            GrimmsServer.logger.log(Level.INFO,"Current level: " + getLevel());
        }
        GrimmsServer.logger.log(Level.INFO,"Current level: " + getLevel());
        return levelUps;
    }

    public double getXpToLevel() {
        return Math.pow(getLevel(), 1.5) * 100;
    }

    public double getXp() {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            PersistentDataContainer data = meta.getPersistentDataContainer();
            return data.getOrDefault(xpKey, PersistentDataType.DOUBLE, 0.0);
        }
        return 0.0;
    }

    public void setXp(double xp) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(xpKey, PersistentDataType.DOUBLE, xp);
            item.setItemMeta(meta);
        }
    }
}
