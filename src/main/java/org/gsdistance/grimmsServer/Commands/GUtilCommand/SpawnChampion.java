package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Constructable.Entity.EntityAbility;
import org.gsdistance.grimmsServer.Constructable.Entity.EntityAffix;
import org.gsdistance.grimmsServer.Constructable.Entity.EntityMetadata;
import org.gsdistance.grimmsServer.Manage.CustomEntityManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpawnChampion {

    public static boolean subCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("grimmsserver.util.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return false;
        }

        if (sender instanceof Player player) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /gutil spawnchampion <entity_type> [tier] [level] [prestige]");
                sender.sendMessage(ChatColor.YELLOW + "Example: /gutil spawnchampion ZOMBIE 3 50 2");
                return false;
            }

            // Parse entity type
            EntityType entityType;
            try {
                entityType = EntityType.valueOf(args[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                sender.sendMessage(ChatColor.RED + "Invalid entity type: " + args[1]);
                return false;
            }

            if (!entityType.isSpawnable() || !entityType.isAlive()) {
                sender.sendMessage(ChatColor.RED + "Entity type must be a living spawnable entity.");
                return false;
            }

            // Parse optional parameters
            int tier = 1;
            int level = 1;
            int prestige = 1;

            if (args.length >= 3) {
                try {
                    tier = Integer.parseInt(args[2]);
                    if (tier < 1 || tier > 5) {
                        sender.sendMessage(ChatColor.RED + "Tier must be between 1 and 5.");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid tier: " + args[2]);
                    return false;
                }
            }

            if (args.length >= 4) {
                try {
                    level = Integer.parseInt(args[3]);
                    if (level < 1) {
                        sender.sendMessage(ChatColor.RED + "Level must be at least 1.");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid level: " + args[3]);
                    return false;
                }
            }

            if (args.length >= 5) {
                try {
                    prestige = Integer.parseInt(args[4]);
                    if (prestige < 1) {
                        sender.sendMessage(ChatColor.RED + "Prestige must be at least 1.");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid prestige: " + args[4]);
                    return false;
                }
            }

            // Spawn the entity
            LivingEntity entity = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), entityType);

            // Set a temporary tag to prevent EntitySpawnEvent from processing this entity
            entity.setMetadata("champion_spawn", new org.bukkit.metadata.FixedMetadataValue(org.gsdistance.grimmsServer.GrimmsServer.instance, true));

            // Register entity
            CustomEntityManager.registerEntity(entity);

            // Create metadata manually to bypass automatic leveling
            EntityMetadata metadata = new EntityMetadata(entity);

            // Clear any affixes/abilities that the constructor might have set
            metadata.affixes.clear();
            metadata.abilities.clear();

            // Override champion tier, level, and prestige
            metadata.championTier = tier;
            metadata.level = level;
            metadata.prestige = prestige;

            // Store in session data to prevent recreation
            org.gsdistance.grimmsServer.Data.PerSessionDataStorage.dataStore.put("entityMetadata-" + entity.getUniqueId(), Data.of(metadata, EntityMetadata.class));

            // Select affixes based on tier
            int numAffixes = Math.min(tier, org.gsdistance.grimmsServer.Constructable.Entity.EntityAffixes.affixes.size());
            List<EntityAffix> availableAffixes = new ArrayList<>(org.gsdistance.grimmsServer.Constructable.Entity.EntityAffixes.affixes.keySet());
            Collections.shuffle(availableAffixes);

            for (int i = 0; i < numAffixes; i++) {
                metadata.affixes.add(availableAffixes.get(i));
            }

            // Select abilities based on tier
            metadata.abilities.clear();
            int numAbilities = Math.max(0, tier - 1);
            if (numAbilities > 0) {
                List<EntityAbility> availableAbilities = new ArrayList<>(org.gsdistance.grimmsServer.Constructable.Entity.EntityAbilities.abilities.keySet());
                Collections.shuffle(availableAbilities);

                for (int i = 0; i < Math.min(numAbilities, availableAbilities.size()); i++) {
                    metadata.abilities.add(availableAbilities.get(i));
                }
            }

            // Apply leveling and champion effects
            try {
                // Use applyAttributeModifiers directly to avoid recalculation of level/prestige
                java.lang.reflect.Method applyAttributeModifiers = EntityMetadata.class.getDeclaredMethod("applyAttributeModifiers", LivingEntity.class, EntityMetadata.class);
                applyAttributeModifiers.setAccessible(true);
                applyAttributeModifiers.invoke(null, entity, metadata);

                java.lang.reflect.Method saveAttributeValues = EntityMetadata.class.getDeclaredMethod("saveAttributeValues", LivingEntity.class, EntityMetadata.class);
                saveAttributeValues.setAccessible(true);
                saveAttributeValues.invoke(null, entity, metadata);

                java.lang.reflect.Method applyChampionAffixes = EntityMetadata.class.getDeclaredMethod("applyChampionAffixes", LivingEntity.class, EntityMetadata.class);
                applyChampionAffixes.setAccessible(true);
                applyChampionAffixes.invoke(null, entity, metadata);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.YELLOW + "Warning: Could not apply effects via reflection.");
                e.printStackTrace();
            }

            // Save metadata
            metadata.saveToFile();

            sender.sendMessage(ChatColor.GREEN + "Spawned champion " + entityType.name() + " (Tier: " + tier + ", Level: " + level + ", Prestige: " + prestige + ")");
            sender.sendMessage(ChatColor.YELLOW + "Affixes: " + metadata.affixes.size() + ", Abilities: " + metadata.abilities.size());

            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return false;
        }
    }
}
