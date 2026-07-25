package org.gsdistance.grimmsServer.Constructable.Entity;

import com.google.gson.Gson;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Config.ConfigKey;
import org.gsdistance.grimmsServer.Constructable.Data;
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
    public List<EntityAffix> affixes = new ArrayList<>();
    public List<EntityAbility> abilities = new ArrayList<>();
    public int championTier = 0;
    // Store base values after first leveling to prevent snowballing on chunk reload
    public double maxHealth = 0;
    public double armor = 0;
    public double armorToughness = 0;
    public double attackDamage = 0;
    // Bossbar for champion mobs
    public transient org.bukkit.boss.BossBar bossBar;
    // Ability cooldown tracking (ability UUID -> last execution time in ticks)
    public transient java.util.Map<UUID, Long> abilityCooldowns = new java.util.HashMap<>();

    public EntityMetadata(Entity entity) {
        this.uuid = entity.getUniqueId();
        this.timestamp = LocalDateTime.now().toString();
        this.originalName = entity.getName();

        // Determine if this entity becomes a champion
        Boolean championEnabled = ActiveConfig.getConfigValue(ConfigKey.CHAMPION_MOBS_ENABLED, Boolean.class);
        Double championChance = ActiveConfig.getConfigValue(ConfigKey.CHAMPION_MOBS_CHANCE, Double.class);

        if (championEnabled != null && championEnabled && championChance != null && Math.random() < championChance) {
            // Roll for champion tier (1-5)
            while (Math.random() < 0.5 && this.championTier < 5) {
                this.championTier++;
            }

            // Select random affixes based on champion tier
            if (this.championTier > 0) {
                int numAffixes = Math.min(this.championTier, EntityAffixes.affixes.size());
                List<EntityAffix> availableAffixes = new ArrayList<>(EntityAffixes.affixes.keySet());
                java.util.Collections.shuffle(availableAffixes);

                for (int i = 0; i < numAffixes; i++) {
                    this.affixes.add(availableAffixes.get(i));
                }

                // Select random abilities based on champion tier (higher tiers get more abilities)
                int numAbilities = Math.max(0, this.championTier - 1);
                if (numAbilities > 0) {
                    List<EntityAbility> availableAbilities = new ArrayList<>(EntityAbilities.abilities.keySet());
                    java.util.Collections.shuffle(availableAbilities);

                    for (int i = 0; i < Math.min(numAbilities, availableAbilities.size()); i++) {
                        this.abilities.add(availableAbilities.get(i));
                    }
                }
            }
        }
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

    public void deleteFromFile() {
        // Remove bossbar if present
        if (this.bossBar != null) {
            this.bossBar.removeAll();
            this.bossBar = null;
        }
        GrimmsServer.pds.deleteData(this.uuid.toString() + ".json", "entityMetadata");
        PerSessionDataStorage.dataStore.remove("entityMetadata-" + this.uuid);
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
                if (entity instanceof LivingEntity && entity.getType() != EntityType.PLAYER) {
                    applyLevelling((LivingEntity) entity, metadata);
                    applyChampionAffixes((LivingEntity) entity, metadata);
                }
                metadata.saveToFile();
            } else {
                String logLevel = ActiveConfig.getConfigValue(ConfigKey.LOG_LEVEL, String.class);
                if ("Verbose".equalsIgnoreCase(logLevel)) {
                    GrimmsServer.logger.info("Retrieved EntityMetadata for " + entity.getUniqueId() + " with level " + metadata.level);
                }
                // Re-apply levelling based on saved level/prestige when loading from disk (chunk reload)
                if (entity instanceof LivingEntity && entity.getType() != EntityType.PLAYER) {
                    applyAttributeModifiers((LivingEntity) entity, metadata);
                    applyChampionAffixes((LivingEntity) entity, metadata);
                }
            }

            PerSessionDataStorage.dataStore.put("entityMetadata-" + entity.getUniqueId(), Data.of(metadata, EntityMetadata.class));
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

        applyAttributeModifiers(livingEntity, metadata);
        saveAttributeValues(livingEntity, metadata);
    }

    private static void saveAttributeValues(LivingEntity livingEntity, EntityMetadata metadata) {
        // Save the base values after leveling to restore on chunk reload
        if (livingEntity.getAttribute(Attribute.MAX_HEALTH) != null) {
            metadata.maxHealth = livingEntity.getAttribute(Attribute.MAX_HEALTH).getBaseValue();
        }
        if (livingEntity.getAttribute(Attribute.ARMOR) != null) {
            metadata.armor = livingEntity.getAttribute(Attribute.ARMOR).getBaseValue();
        }
        if (livingEntity.getAttribute(Attribute.ARMOR_TOUGHNESS) != null) {
            metadata.armorToughness = livingEntity.getAttribute(Attribute.ARMOR_TOUGHNESS).getBaseValue();
        }
        if (livingEntity.getAttribute(Attribute.ATTACK_DAMAGE) != null) {
            metadata.attackDamage = livingEntity.getAttribute(Attribute.ATTACK_DAMAGE).getBaseValue();
        }
    }

    private static void applyChampionAffixes(LivingEntity livingEntity, EntityMetadata metadata) {
        // Apply all champion affixes to the entity
        for (EntityAffix affix : metadata.affixes) {
            affix.effect().apply(livingEntity);
        }

        // Create bossbar for champion mobs
        if (metadata.championTier > 0) {
            createBossBar(livingEntity, metadata);
        }
    }

    private static void createBossBar(LivingEntity livingEntity, EntityMetadata metadata) {
        // Remove existing bossbar if present
        if (metadata.bossBar != null) {
            metadata.bossBar.removeAll();
        }

        // Create new bossbar
        String title = ChatColor.GOLD + "★ Champion " + metadata.championTier + " ★ " + ChatColor.WHITE + livingEntity.getName();
        metadata.bossBar = org.bukkit.Bukkit.createBossBar(title, org.bukkit.boss.BarColor.RED, org.bukkit.boss.BarStyle.SEGMENTED_10);
        updateBossBarHealth(livingEntity, metadata);

        // Add nearby players to bossbar
        Integer radius = ActiveConfig.getConfigValue(ConfigKey.CHAMPION_MOBS_BOSSBAR_RADIUS, Integer.class);
        if (radius == null) radius = 50;

        for (org.bukkit.entity.Player player : livingEntity.getWorld().getPlayers()) {
            if (player.getLocation().distance(livingEntity.getLocation()) <= radius) {
                metadata.bossBar.addPlayer(player);
            }
        }
    }

    public static void updateBossBarHealth(LivingEntity livingEntity, EntityMetadata metadata) {
        if (metadata.bossBar != null && livingEntity.getAttribute(Attribute.MAX_HEALTH) != null) {
            double maxHealth = livingEntity.getAttribute(Attribute.MAX_HEALTH).getValue();
            double currentHealth = livingEntity.getHealth();
            metadata.bossBar.setProgress(Math.max(0, Math.min(1, currentHealth / maxHealth)));
        }
    }

    public void executeAbilities(LivingEntity livingEntity, EntityMetadata metadata, long currentTick) {
        for (EntityAbility ability : metadata.abilities) {
            UUID abilityId = java.util.UUID.nameUUIDFromBytes(ability.id().getBytes());
            Long lastExecution = metadata.abilityCooldowns.get(abilityId);

            if (lastExecution == null || currentTick - lastExecution >= ability.cooldownTicks()) {
                ability.effect().apply(livingEntity);
                metadata.abilityCooldowns.put(abilityId, currentTick);
            }
        }
    }

    private static void applyAttributeModifiers(LivingEntity livingEntity, EntityMetadata metadata) {
        Boolean enabled = ActiveConfig.getConfigValue(ConfigKey.LEVELLED_MOBS_ENABLED, Boolean.class);
        if (enabled == null || !enabled) {
            return;
        }

        // Check blacklist
        List<String> blacklist = ActiveConfig.getConfigValue(ConfigKey.LEVELLED_MOBS_BLACKLIST, List.class);
        if (blacklist != null && blacklist.contains(livingEntity.getType().name())) {
            return;
        }

        int finalLevel = metadata.level;
        int totalPrestige = metadata.prestige;

        // Check if we have saved base values (chunk reload) or need to calculate from vanilla (new entity)
        boolean hasSavedValues = metadata.maxHealth > 0 || metadata.armor > 0 || metadata.armorToughness > 0 || metadata.attackDamage > 0;

        // Clear any existing attribute modifiers to prevent stacking
        if (livingEntity.getAttribute(Attribute.MAX_HEALTH) != null) {
            livingEntity.getAttribute(Attribute.MAX_HEALTH).getModifiers().clear();
        }
        if (livingEntity.getAttribute(Attribute.ARMOR) != null) {
            livingEntity.getAttribute(Attribute.ARMOR).getModifiers().clear();
        }
        if (livingEntity.getAttribute(Attribute.ARMOR_TOUGHNESS) != null) {
            livingEntity.getAttribute(Attribute.ARMOR_TOUGHNESS).getModifiers().clear();
        }
        if (livingEntity.getAttribute(Attribute.ATTACK_DAMAGE) != null) {
            livingEntity.getAttribute(Attribute.ATTACK_DAMAGE).getModifiers().clear();
        }

        // Apply attribute modifiers
        if (livingEntity.getAttribute(Attribute.MAX_HEALTH) != null) {
            if (hasSavedValues && metadata.maxHealth > 0) {
                // Restore saved base value (chunk reload)
                livingEntity.getAttribute(Attribute.MAX_HEALTH).setBaseValue(metadata.maxHealth);
                livingEntity.setHealth(metadata.maxHealth);
            } else {
                // Calculate from vanilla base value (new entity)
                double vanillaHealth = livingEntity.getAttribute(Attribute.MAX_HEALTH).getBaseValue();
                Double healthDivisor = ActiveConfig.getConfigValue(ConfigKey.LEVELLED_MOBS_HEALTH_DIVISOR, Double.class);
                if (healthDivisor == null) healthDivisor = 25.0;
                double healthBoost = vanillaHealth * (finalLevel / healthDivisor) * Math.cbrt(totalPrestige);
                livingEntity.getAttribute(Attribute.MAX_HEALTH).setBaseValue(vanillaHealth + healthBoost);
                livingEntity.setHealth(livingEntity.getAttribute(Attribute.MAX_HEALTH).getValue());
            }
        }

        if (livingEntity.getAttribute(Attribute.ARMOR) != null) {
            if (hasSavedValues && metadata.armor > 0) {
                // Restore saved base value (chunk reload)
                livingEntity.getAttribute(Attribute.ARMOR).setBaseValue(metadata.armor);
            } else {
                // Calculate from scratch (new entity)
                Double armorDivisor = ActiveConfig.getConfigValue(ConfigKey.LEVELLED_MOBS_ARMOR_DIVISOR, Double.class);
                if (armorDivisor == null) armorDivisor = 2.0;
                double armorBoost = Math.sqrt(finalLevel) * Math.sqrt(totalPrestige) / armorDivisor;
                livingEntity.getAttribute(Attribute.ARMOR).setBaseValue(armorBoost);
            }
        }

        if (livingEntity.getAttribute(Attribute.ARMOR_TOUGHNESS) != null) {
            if (hasSavedValues && metadata.armorToughness > 0) {
                // Restore saved base value (chunk reload)
                livingEntity.getAttribute(Attribute.ARMOR_TOUGHNESS).setBaseValue(metadata.armorToughness);
            } else {
                // Calculate from scratch (new entity)
                double toughnessBoost = Math.cbrt(finalLevel) * Math.cbrt(totalPrestige);
                livingEntity.getAttribute(Attribute.ARMOR_TOUGHNESS).setBaseValue(toughnessBoost);
            }
        }

        if (livingEntity.getAttribute(Attribute.ATTACK_DAMAGE) != null) {
            if (hasSavedValues && metadata.attackDamage > 0) {
                // Restore saved base value (chunk reload)
                livingEntity.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(metadata.attackDamage);
            } else {
                // Calculate from vanilla base value (new entity)
                double vanillaDamage = livingEntity.getAttribute(Attribute.ATTACK_DAMAGE).getBaseValue();
                double damageBoost = Math.cbrt(finalLevel) * Math.sqrt(totalPrestige);
                livingEntity.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(vanillaDamage + damageBoost);
            }
        }

        // Set custom name with level and health bar (skip for champion mobs)
        if (metadata.championTier > 0) {
            return;
        }

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
