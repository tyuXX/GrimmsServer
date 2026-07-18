package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.GameRule;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Config.ConfigKey;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerLevelHandler;
import org.gsdistance.grimmsServer.Data.Player.PlayerInventoryData;
import org.gsdistance.grimmsServer.Data.Player.PlayerTitleChecker;
import org.gsdistance.grimmsServer.Manage.CustomEntityManager;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.ServerStats;
import org.gsdistance.grimmsServer.Stats.WorldStats;

import java.util.HashMap;
import java.util.Map;

public class EntityDeathEvent {
    public EntityDeathEvent() {
    }

    public static void Event(org.bukkit.event.entity.EntityDeathEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER) {
            CustomEntityManager.unregisterEntity(event.getEntity());
            event.getEntity().setCustomName(null);
        }

        if (event.getEntity().getKiller() != null && event.getEntity().getAttribute(Attribute.MAX_HEALTH) != null && event.getEntity().getAttribute(Attribute.MAX_HEALTH).getValue() > 0 && event.getEntity().getKiller().getType() == EntityType.PLAYER) {
            PlayerStats playerStats = PlayerStats.getPlayerStats(event.getEntity().getKiller());
            WorldStats worldStats = WorldStats.getWorldStats(event.getEntity().getWorld());
            playerStats.changeStat("total_kill_count", 1);
            playerStats.changeStat("money", (int) Math.round((double) 3.0F * PlayerLevelHandler.getLevelHandler(event.getEntity().getKiller()).getLesserMoneyMultiplier()));
            playerStats.changeStat("tPoint", (int) Math.round((double) 25.0F * PlayerLevelHandler.getLevelHandler(event.getEntity().getKiller()).getMoneyMultiplier()));
            worldStats.changeStat("wPoint", 40);
            PlayerLevelHandler.getLevelHandler(event.getEntity().getKiller()).addExp(50.0F);
            if (event.getEntity().getType() == EntityType.PLAYER) {
                playerStats.changeStat("money", (int) Math.round((double) 12.0F * PlayerLevelHandler.getLevelHandler(event.getEntity().getKiller()).getLesserMoneyMultiplier()));
                playerStats.changeStat("tPoint", (int) Math.round((double) 50.0F * PlayerLevelHandler.getLevelHandler(event.getEntity().getKiller()).getMoneyMultiplier()));
                worldStats.changeStat("wPoint", 110);
                PlayerLevelHandler.getLevelHandler(event.getEntity().getKiller()).addExp(150.0F);
                PlayerTitleChecker.killedPlayer(event.getEntity().getKiller());
                PlayerTitleChecker.gotKilledByPlayer((Player) event.getEntity());
            } else if (event.getEntity().getType() == EntityType.ENDER_DRAGON) {
                PlayerTitleChecker.killedDragon(event.getEntity().getKiller());
            }
        }

        if (event.getEntity().getType() == EntityType.PLAYER) {
            PlayerStats.getPlayerStats((Player) event.getEntity()).changeStat("death_count", 1);

            // Save inventory if keepInventory is off
            Player player = (Player) event.getEntity();
            if (!player.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY)) {
                PlayerInventoryData inventoryData = PlayerInventoryData.getPlayerInventoryData(player.getUniqueId());

                // Save current inventory as a snapshot to previous inventories
                if (inventoryData.inventoryContents != null && !inventoryData.inventoryContents.isEmpty()) {
                    PlayerInventoryData.InventorySnapshot snapshot = new PlayerInventoryData.InventorySnapshot(
                            inventoryData.inventoryContents,
                            inventoryData.armorContents,
                            inventoryData.extraContents
                    );
                    inventoryData.addPreviousInventory(snapshot);
                }

                // Save current inventory
                Map<Integer, ItemStack> currentInventory = new HashMap<>();
                for (int i = 0; i < player.getInventory().getSize(); i++) {
                    ItemStack item = player.getInventory().getItem(i);
                    if (item != null) {
                        currentInventory.put(i, item);
                    }
                }

                inventoryData.inventoryContents = currentInventory;
                inventoryData.armorContents = player.getInventory().getArmorContents();
                inventoryData.extraContents = player.getInventory().getExtraContents();
                inventoryData.saveToPDS();

                // Clear inventory if config allows
                boolean clearInventory = ActiveConfig.getConfigValue(ConfigKey.CLEAR_INVENTORY_ON_DEATH, Boolean.class);
                if (clearInventory) {
                    player.getInventory().clear();
                }
            }
        }

        WorldStats.getWorldStats(event.getEntity().getWorld()).changeStat("death_count", 1);
        ServerStats.getServerStats().changeStat("death_count", 1);
    }
}
