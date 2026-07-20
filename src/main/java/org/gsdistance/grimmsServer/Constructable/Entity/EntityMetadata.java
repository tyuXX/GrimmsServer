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
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class EntityMetadata {
    public final UUID uuid;
    public final String timestamp;
    public int level = 1;
    public int prestige = 1;
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
                if (entity instanceof LivingEntity && (metadata.level > 1 || metadata.prestige > 1)) {
                    applyLevelling((LivingEntity) entity, metadata);
                }
            }

            PerSessionDataStorage.dataStore.put("entityMetadata-" + entity.getUniqueId(), Data.of(metadata, EntityMetadata.class));
            metadata.softSave();
            metadata.logMetadata();
            return metadata;
        }
    }

    public static ChatColor getLevelColor(int level) {
        if (level < 10) {
            return ChatColor.GREEN;
        } else if (level < 25) {
            return ChatColor.YELLOW;
        } else if (level < 50) {
            return ChatColor.GOLD;
        } else if (level < 75) {
            return ChatColor.RED;
        } else {
            return ChatColor.DARK_RED;
        }
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
        int totalPrestige = 0;
        List<Player> nearbyPlayers = new ArrayList<>();
        Integer searchRadius = ActiveConfig.getConfigValue(ConfigKey.LEVELLED_MOBS_SEARCH_RADIUS, Integer.class);
        if (searchRadius == null) searchRadius = 50;

        for (Entity entity : livingEntity.getNearbyEntities(searchRadius, searchRadius, searchRadius)) {
            if (entity instanceof Player) {
                nearbyPlayers.add((Player) entity);
            }
        }
        for (Player player : nearbyPlayers) {
            PlayerStats playerStats = PlayerStats.getPlayerStats(player);
            totalLevel += playerStats.getStat("level", Integer.class);
            totalPrestige += playerStats.getStat("prestige", Integer.class);
        }

        double randomness = Math.sqrt(totalLevel);
        double randomOffset = (Math.random() * 2 * randomness) - randomness;
        int finalLevel = (int) Math.max(1, Math.round(totalLevel + randomOffset));
        totalPrestige = Math.max(totalPrestige, 1);

        metadata.level = finalLevel;
        metadata.prestige = totalPrestige;

        // Apply attribute modifiers
        if (livingEntity.getAttribute(Attribute.MAX_HEALTH) != null) {
            double baseHealth = livingEntity.getAttribute(Attribute.MAX_HEALTH).getBaseValue();
            Double healthDivisor = ActiveConfig.getConfigValue(ConfigKey.LEVELLED_MOBS_HEALTH_DIVISOR, Double.class);
            if (healthDivisor == null) healthDivisor = 25.0;
            double healthBoost = baseHealth * (finalLevel / healthDivisor) * Math.cbrt(totalPrestige);
            livingEntity.getAttribute(Attribute.MAX_HEALTH).setBaseValue(baseHealth + healthBoost);
            livingEntity.setHealth(livingEntity.getAttribute(Attribute.MAX_HEALTH).getValue());
        }

        if (livingEntity.getAttribute(Attribute.ARMOR) != null) {
            Double armorDivisor = ActiveConfig.getConfigValue(ConfigKey.LEVELLED_MOBS_ARMOR_DIVISOR, Double.class);
            if (armorDivisor == null) armorDivisor = 2.0;
            double armorBoost = Math.sqrt(finalLevel) * Math.sqrt(totalPrestige) / armorDivisor;
            livingEntity.getAttribute(Attribute.ARMOR).setBaseValue(armorBoost);
        }

        if (livingEntity.getAttribute(Attribute.ATTACK_DAMAGE) != null) {
            double damageBoost = Math.cbrt(finalLevel) * Math.sqrt(totalPrestige);
            livingEntity.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(
                    livingEntity.getAttribute(Attribute.ATTACK_DAMAGE).getBaseValue() + damageBoost
            );
        }

        // Set custom name with level and health bar
        ChatColor levelColor = getLevelColor(finalLevel);

        double maxHealth = livingEntity.getAttribute(Attribute.MAX_HEALTH).getValue();
        String healthBar = Shared.generateHealthBar(livingEntity.getHealth(), maxHealth);

        metadata.originalName = livingEntity.getName();
        String prestigeDisplay = totalPrestige > 1 ? ChatColor.DARK_PURPLE + "[" + totalPrestige + "]" : "";
        String displayName = prestigeDisplay + levelColor + "[" + finalLevel + "] " + ChatColor.WHITE + metadata.originalName + " " + healthBar;
        livingEntity.setCustomName(displayName);
        livingEntity.setCustomNameVisible(true);
    }
}
