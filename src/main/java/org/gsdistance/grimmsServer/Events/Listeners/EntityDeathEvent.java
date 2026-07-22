package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.GameRule;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Config.ConfigKey;
import org.gsdistance.grimmsServer.Constructable.Item.CustomEnchantmentHandler;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerLevelHandler;
import org.gsdistance.grimmsServer.Data.CustomEnchantments;
import org.gsdistance.grimmsServer.Data.Player.PlayerInventoryData;
import org.gsdistance.grimmsServer.Data.Player.PlayerTitleChecker;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Manage.CustomEntityManager;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.ServerStats;
import org.gsdistance.grimmsServer.Stats.WorldStats;

import java.io.IOException;
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
            Boolean keepInventory = player.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY);
            if (keepInventory == null || !keepInventory) {
                PlayerInventoryData inventoryData = PlayerInventoryData.getPlayerInventoryData(player.getUniqueId());

                // Convert current inventory to Base64 for saving
                Map<Integer, ItemStack> currentInventory = new HashMap<>();
                for (int i = 0; i < player.getInventory().getSize(); i++) {
                    ItemStack item = player.getInventory().getItem(i);
                    if (item != null) {
                        currentInventory.put(i, item);
                    }
                }

                // Save current inventory as a snapshot to previous inventories
                // Convert current inventory to Base64 for the snapshot
                Map<Integer, String> currentInventoryBase64 = new HashMap<>();
                for (Map.Entry<Integer, ItemStack> entry : currentInventory.entrySet()) {
                    try {
                        String base64 = PlayerInventoryData.itemStackToBase64(entry.getValue());
                        if (base64 != null) {
                            currentInventoryBase64.put(entry.getKey(), base64);
                        }
                    } catch (IOException e) {
                        GrimmsServer.logger.warning("Failed to serialize ItemStack at slot " + entry.getKey());
                    }
                }

                String[] armorBase64 = new String[4];
                ItemStack[] armor = player.getInventory().getArmorContents();
                for (int i = 0; i < armor.length && i < 4; i++) {
                    try {
                        if (armor[i] != null) {
                            armorBase64[i] = PlayerInventoryData.itemStackToBase64(armor[i]);
                        }
                    } catch (IOException e) {
                        GrimmsServer.logger.warning("Failed to serialize armor slot " + i);
                    }
                }

                String[] extraBase64 = new String[Math.min(player.getInventory().getExtraContents().length, 36)];
                ItemStack[] extra = player.getInventory().getExtraContents();
                for (int i = 0; i < extraBase64.length; i++) {
                    try {
                        if (extra[i] != null) {
                            extraBase64[i] = PlayerInventoryData.itemStackToBase64(extra[i]);
                        }
                    } catch (IOException e) {
                        GrimmsServer.logger.warning("Failed to serialize extra slot " + i);
                    }
                }

                PlayerInventoryData.InventorySnapshot snapshot = new PlayerInventoryData.InventorySnapshot(
                        currentInventoryBase64,
                        armorBase64,
                        extraBase64
                );
                inventoryData.addPreviousInventory(snapshot);

                // Save the new inventory
                inventoryData.setInventoryFromItemStacks(currentInventory);
                inventoryData.setArmorFromItemStacks(player.getInventory().getArmorContents());
                inventoryData.setExtraFromItemStacks(player.getInventory().getExtraContents());
                inventoryData.saveToPDS();

                // Mark soulbound items (items that should not drop on death)
                // Check each item for soulbound enchantment
                for (Map.Entry<Integer, ItemStack> entry : currentInventory.entrySet()) {
                    ItemStack item = entry.getValue();
                    if (item != null) {
                        CustomEnchantmentHandler enchantHandler = CustomEnchantmentHandler.getHandler(item);
                        if (enchantHandler.hasEnchantment(CustomEnchantments.SOULBOUND)) {
                            inventoryData.markSoulboundItemStack(entry.getKey(), item, "inventory");
                        }
                    }
                }

                // Check armor for soulbound items
                for (int i = 0; i < armor.length && i < 4; i++) {
                    ItemStack item = armor[i];
                    if (item != null) {
                        CustomEnchantmentHandler enchantHandler = CustomEnchantmentHandler.getHandler(item);
                        if (enchantHandler.hasEnchantment(CustomEnchantments.SOULBOUND)) {
                            inventoryData.markSoulboundItemStack(i, item, "armor");
                        }
                    }
                }

                // Check extra contents for soulbound items
                for (int i = 0; i < extra.length && i < 36; i++) {
                    ItemStack item = extra[i];
                    if (item != null) {
                        CustomEnchantmentHandler enchantHandler = CustomEnchantmentHandler.getHandler(item);
                        if (enchantHandler.hasEnchantment(CustomEnchantments.SOULBOUND)) {
                            inventoryData.markSoulboundItemStack(i, item, "extra");
                        }
                    }
                }

                // Save again after marking soulbound items to update the saved data
                inventoryData.saveToPDS();

                // Clear inventory if config allows
                Boolean clearInventoryConfig = ActiveConfig.getConfigValue(ConfigKey.CLEAR_INVENTORY_ON_DEATH, Boolean.class);
                boolean clearInventory = clearInventoryConfig != null && clearInventoryConfig;
                if (clearInventory) {
                    player.getInventory().clear();
                }
            }
        }

        WorldStats.getWorldStats(event.getEntity().getWorld()).changeStat("death_count", 1);
        ServerStats.getServerStats().changeStat("death_count", 1);
    }
}
