package org.gsdistance.grimmsServer.Events.Listeners;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Constructable.Entity.EntityMetadata;
import org.gsdistance.grimmsServer.Constructable.Item.ItemLevelHandler;
import org.gsdistance.grimmsServer.Shared;

public class EntityDamageEvent {
    public EntityDamageEvent() {
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

        if (var2 instanceof LivingEntity livingEntity) {
            EntityMetadata metadata = EntityMetadata.getEntityMetadata(livingEntity);
            if (metadata.level > 1 || metadata.prestige > 1) {
                ChatColor levelColor = EntityMetadata.getLevelColor(metadata.level);

                double maxHealth = livingEntity.getAttribute(Attribute.MAX_HEALTH).getValue();
                double healthAfterDamage = Math.max(0, livingEntity.getHealth() - event.getDamage());
                String healthBar = Shared.generateHealthBar(healthAfterDamage, maxHealth);

                String prestigeDisplay = metadata.prestige > 1 ? ChatColor.DARK_PURPLE + "[" + metadata.prestige + "]" : "";
                String displayName = prestigeDisplay + levelColor + "[" + metadata.level + "] " + ChatColor.WHITE + metadata.originalName + " " + healthBar;
                livingEntity.setCustomName(displayName);
            }
        }
    }
}
