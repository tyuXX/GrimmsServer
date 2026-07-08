package org.gsdistance.grimmsServer.Events.Listeners;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Entity.EntityMetadata;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerLevelHandler;
import org.gsdistance.grimmsServer.Manage.CustomEntityManager;

import java.util.ArrayList;
import java.util.List;

public class EntitySpawnEvent {
    public EntitySpawnEvent() {
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

    public static void Event(org.bukkit.event.entity.EntitySpawnEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER) {
            CustomEntityManager.registerEntity(event.getEntity());
            EntityMetadata metadata = EntityMetadata.getEntityMetadata(event.getEntity());

            int totalLevel = 0;
            List<Player> nearbyPlayers = new ArrayList<>();
            for (Entity entity : event.getEntity().getNearbyEntities(50, 50, 50)) {
                if (entity instanceof Player) {
                    nearbyPlayers.add((Player) entity);
                }
            }
            for (Player player : nearbyPlayers) {
                totalLevel += PlayerLevelHandler.getLevelHandler(player).getLevel();
            }

            double randomness = Math.sqrt(totalLevel);
            double randomOffset = (Math.random() * 2 * randomness) - randomness;
            int finalLevel = (int) Math.max(1, Math.round(totalLevel + randomOffset));

            metadata.level = finalLevel;

            if (event.getEntity() instanceof LivingEntity livingEntity) {

                if (livingEntity.getAttribute(Attribute.MAX_HEALTH) != null) {
                    double baseHealth = livingEntity.getAttribute(Attribute.MAX_HEALTH).getBaseValue();
                    double healthBoost = baseHealth * (finalLevel / 25.0);
                    livingEntity.getAttribute(Attribute.MAX_HEALTH).setBaseValue(baseHealth + healthBoost);
                    livingEntity.setHealth(livingEntity.getAttribute(Attribute.MAX_HEALTH).getValue());
                }

                if (livingEntity.getAttribute(Attribute.ARMOR) != null) {
                    double armorBoost = Math.sqrt(finalLevel) / 2.0;
                    livingEntity.getAttribute(Attribute.ARMOR).setBaseValue(armorBoost);
                }

                if (livingEntity.getAttribute(Attribute.ATTACK_DAMAGE) != null) {
                    double damageBoost = Math.cbrt(finalLevel);
                    livingEntity.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(
                        livingEntity.getAttribute(Attribute.ATTACK_DAMAGE).getBaseValue() + damageBoost
                    );
                }

                ChatColor levelColor;
                if (finalLevel < 10) {
                    levelColor = ChatColor.GREEN;
                } else if (finalLevel < 25) {
                    levelColor = ChatColor.YELLOW;
                } else if (finalLevel < 50) {
                    levelColor = ChatColor.GOLD;
                } else if (finalLevel < 75) {
                    levelColor = ChatColor.RED;
                } else {
                    levelColor = ChatColor.DARK_RED;
                }

                double maxHealth = livingEntity.getAttribute(Attribute.MAX_HEALTH).getValue();
                String healthBar = generateHealthBar(livingEntity.getHealth(), maxHealth);

                metadata.originalName = livingEntity.getName();
                String displayName = levelColor + "[" + finalLevel + "] " + ChatColor.WHITE + metadata.originalName + " " + healthBar;
                livingEntity.setCustomName(displayName);
                livingEntity.setCustomNameVisible(true);
                livingEntity.setPersistent(false);

                livingEntity.setMetadata("customEntityLevel", new org.bukkit.metadata.FixedMetadataValue(org.gsdistance.grimmsServer.GrimmsServer.instance, finalLevel));
            }
        }
    }
}
