package org.gsdistance.grimmsServer.Events.Listeners;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Constructable.Entity.EntityMetadata;
import org.gsdistance.grimmsServer.Constructable.Item.ItemLevelHandler;

public class EntityDamageEvent {
    public EntityDamageEvent() {
    }

    private static String generateHealthBar(double currentHealth, double maxHealth) {
        if (maxHealth <= 0) return "";

        int bars = 10;
        double healthPercent = currentHealth / maxHealth;
        int filledBars = (int) Math.round(healthPercent * bars);

        ChatColor barColor;
        if (healthPercent > 0.6) {
            barColor = ChatColor.GREEN;
        } else if (healthPercent > 0.3) {
            barColor = ChatColor.YELLOW;
        } else {
            barColor = ChatColor.RED;
        }

        StringBuilder healthBar = new StringBuilder(barColor.toString());
        healthBar.append("[");
        for (int i = 0; i < bars; i++) {
            if (i < filledBars) {
                healthBar.append("|");
            } else {
                healthBar.append(ChatColor.GRAY).append("|").append(barColor);
            }
        }
        healthBar.append("]");

        return healthBar.toString();
    }

    public static void Event(org.bukkit.event.entity.EntityDamageEvent event) {
        Entity var2 = event.getEntity();
        if (var2 instanceof Player player) {
            ItemStack[] items = player.getInventory().getArmorContents();

            for (ItemStack item : items) {
                if (item != null && ItemLevelHandler.isItemLevelable(item)) {
                    ItemLevelHandler itemLevelHandler = ItemLevelHandler.getLevelHandler(item, player);
                    itemLevelHandler.addXp(event.getDamage());
                }
            }
        }

        if (var2 instanceof LivingEntity livingEntity && var2.hasMetadata("customEntityLevel")) {
            int level = var2.getMetadata("customEntityLevel").get(0).asInt();
            EntityMetadata metadata = EntityMetadata.getEntityMetadata(livingEntity);

            ChatColor levelColor;
            if (level < 10) {
                levelColor = ChatColor.GREEN;
            } else if (level < 25) {
                levelColor = ChatColor.YELLOW;
            } else if (level < 50) {
                levelColor = ChatColor.GOLD;
            } else if (level < 75) {
                levelColor = ChatColor.RED;
            } else {
                levelColor = ChatColor.DARK_RED;
            }

            double maxHealth = livingEntity.getAttribute(Attribute.MAX_HEALTH).getValue();
            String healthBar = generateHealthBar(livingEntity.getHealth(), maxHealth);

            String displayName = levelColor + "[" + level + "] " + ChatColor.WHITE + metadata.originalName + " " + healthBar;
            livingEntity.setCustomName(displayName);
        }
    }
}
