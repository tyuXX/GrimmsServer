package org.gsdistance.grimmsServer.Constructable.Entity;

import com.google.gson.Gson;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Config.ConfigKey;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerLevelHandler;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class EntityMetadata {
    public final UUID uuid;
    public final String timestamp;
    public int level = 1;
    public String originalName;

    public EntityMetadata(Entity entity) {
        this.uuid = entity.getUniqueId();
        this.timestamp = LocalDateTime.now().toString();
        this.originalName = entity.getName();
    }

    public void logMetadata() {
        String logLevel = ActiveConfig.getConfigValue(ConfigKey.LOG_LEVEL, String.class);
        if ("Verbose".equalsIgnoreCase(logLevel)) {
            Logger var10000 = GrimmsServer.logger;
            String var10001 = String.valueOf(this.uuid);
            var10000.info("Entity Metadata for " + var10001 + ":" + (new Gson()).toJson(this));
        }
    }

    public void softSave() {
        PerSessionDataStorage.dataStore.put("entityMetadata-" + this.uuid, Data.of(this, EntityMetadata.class));
    }

    public void saveToFile() {
        this.softSave();
        GrimmsServer.pds.saveData(this, EntityMetadata.class, this.uuid.toString() + ".json", "entityMetadata");
    }

    public static EntityMetadata getEntityMetadata(Entity entity) {
        if (PerSessionDataStorage.dataStore.containsKey("entityMetadata-" + entity.getUniqueId())) {
            return (EntityMetadata) PerSessionDataStorage.dataStore.get("entityMetadata-" + entity.getUniqueId()).key();
        } else {
            EntityMetadata metadata = GrimmsServer.pds.retrieveData(entity.getUniqueId() + ".json", "entityMetadata", EntityMetadata.class);
            if (metadata == null) {
                metadata = new EntityMetadata(entity);
                String logLevel = ActiveConfig.getConfigValue(ConfigKey.LOG_LEVEL, String.class);
                if ("Verbose".equalsIgnoreCase(logLevel)) {
                    GrimmsServer.logger.info("Created new EntityMetadata for " + entity.getUniqueId());
                }
                // Apply levelling when metadata is first created
                if (entity instanceof LivingEntity) {
                    applyLevelling((LivingEntity) entity, metadata);
                }
            } else {
                String logLevel = ActiveConfig.getConfigValue(ConfigKey.LOG_LEVEL, String.class);
                if ("Verbose".equalsIgnoreCase(logLevel)) {
                    GrimmsServer.logger.info("Retrieved EntityMetadata for " + entity.getUniqueId());
                }
                // Re-apply levelling when metadata is loaded (e.g., after chunk reload)
                if (entity instanceof LivingEntity && metadata.level > 1) {
                    applyLevelling((LivingEntity) entity, metadata);
                }
            }

            PerSessionDataStorage.dataStore.put("entityMetadata-" + entity.getUniqueId(), Data.of(metadata, EntityMetadata.class));
            metadata.softSave();
            metadata.logMetadata();
            return metadata;
        }
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

    private static void applyLevelling(LivingEntity livingEntity, EntityMetadata metadata) {
        Boolean enabled = ActiveConfig.getConfigValue(ConfigKey.LEVELLED_MOBS_ENABLED, Boolean.class);
        if (enabled == null || !enabled) {
            return;
        }

        // Check blacklist
        List<String> blacklist = ActiveConfig.getConfigValue(ConfigKey.LEVELLED_MOBS_BLACKLIST, List.class);
        if (blacklist != null && blacklist.contains(livingEntity.getType().name())) {
            return;
        }

        // Calculate level based on nearby players
        int totalLevel = 0;
        List<Player> nearbyPlayers = new ArrayList<>();
        Integer searchRadius = ActiveConfig.getConfigValue(ConfigKey.LEVELLED_MOBS_SEARCH_RADIUS, Integer.class);
        if (searchRadius == null) searchRadius = 50;

        for (Entity entity : livingEntity.getNearbyEntities(searchRadius, searchRadius, searchRadius)) {
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

        // Apply attribute modifiers
        if (livingEntity.getAttribute(Attribute.MAX_HEALTH) != null) {
            double baseHealth = livingEntity.getAttribute(Attribute.MAX_HEALTH).getBaseValue();
            Double healthDivisor = ActiveConfig.getConfigValue(ConfigKey.LEVELLED_MOBS_HEALTH_DIVISOR, Double.class);
            if (healthDivisor == null) healthDivisor = 25.0;
            double healthBoost = baseHealth * (finalLevel / healthDivisor);
            livingEntity.getAttribute(Attribute.MAX_HEALTH).setBaseValue(baseHealth + healthBoost);
            livingEntity.setHealth(livingEntity.getAttribute(Attribute.MAX_HEALTH).getValue());
        }

        if (livingEntity.getAttribute(Attribute.ARMOR) != null) {
            Double armorDivisor = ActiveConfig.getConfigValue(ConfigKey.LEVELLED_MOBS_ARMOR_DIVISOR, Double.class);
            if (armorDivisor == null) armorDivisor = 2.0;
            double armorBoost = Math.sqrt(finalLevel) / armorDivisor;
            livingEntity.getAttribute(Attribute.ARMOR).setBaseValue(armorBoost);
        }

        if (livingEntity.getAttribute(Attribute.ATTACK_DAMAGE) != null) {
            double damageBoost = Math.cbrt(finalLevel);
            livingEntity.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(
                    livingEntity.getAttribute(Attribute.ATTACK_DAMAGE).getBaseValue() + damageBoost
            );
        }

        // Set custom name with level and health bar
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
    }
}
