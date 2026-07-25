package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.gsdistance.grimmsServer.Constructable.Entity.EntityMetadata;
import org.gsdistance.grimmsServer.Manage.CustomEntityManager;

public class LevelEntity {

    public static boolean subCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("grimmsserver.util.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return false;
        }

        if (sender instanceof Player player) {
            // Parse optional level and prestige parameters
            Integer specifiedLevel = null;
            Integer specifiedPrestige = null;

            if (args.length >= 3) {
                try {
                    specifiedLevel = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid level: " + args[2]);
                    return false;
                }
            }
            if (args.length >= 4) {
                try {
                    specifiedPrestige = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid prestige: " + args[3]);
                    return false;
                }
            }

            if (args.length < 2) {
                // Force leveling on entity player is looking at
                RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getEyeLocation().getDirection(), 5);
                Entity targetEntity = rayTrace != null ? rayTrace.getHitEntity() : null;
                if (targetEntity == null) {
                    sender.sendMessage(ChatColor.RED + "No entity found in range (5 blocks). Specify an entity UUID or look at an entity.");
                    return false;
                }
                return applyLeveling(sender, targetEntity, specifiedLevel, specifiedPrestige);
            } else {
                // Force leveling by UUID
                try {
                    java.util.UUID uuid = java.util.UUID.fromString(args[1]);
                    Entity targetEntity = null;
                    for (Entity entity : player.getWorld().getEntities()) {
                        if (entity.getUniqueId().equals(uuid)) {
                            targetEntity = entity;
                            break;
                        }
                    }
                    if (targetEntity == null) {
                        sender.sendMessage(ChatColor.RED + "Entity with UUID " + args[1] + " not found in current world.");
                        return false;
                    }
                    return applyLeveling(sender, targetEntity, specifiedLevel, specifiedPrestige);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid UUID format: " + args[1]);
                    return false;
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return false;
        }
    }

    private static boolean applyLeveling(CommandSender sender, Entity entity, Integer specifiedLevel, Integer specifiedPrestige) {
        // Register entity in the registry
        CustomEntityManager.registerEntity(entity);

        // Get or create metadata and apply leveling
        EntityMetadata metadata = EntityMetadata.getEntityMetadata(entity);

        // Override level/prestige if specified
        if (specifiedLevel != null) {
            metadata.level = specifiedLevel;
        }
        if (specifiedPrestige != null) {
            metadata.prestige = specifiedPrestige;
        }

        if (entity instanceof LivingEntity livingEntity) {
            // Force re-apply leveling even if already leveled
            try {
                java.lang.reflect.Method applyLevelling = EntityMetadata.class.getDeclaredMethod("applyLevelling", LivingEntity.class, EntityMetadata.class);
                applyLevelling.setAccessible(true);
                applyLevelling.invoke(null, livingEntity, metadata);
                // Save attribute values after applying
                java.lang.reflect.Method saveAttributeValues = EntityMetadata.class.getDeclaredMethod("saveAttributeValues", LivingEntity.class, EntityMetadata.class);
                saveAttributeValues.setAccessible(true);
                saveAttributeValues.invoke(null, livingEntity, metadata);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.YELLOW + "Warning: Could not re-apply leveling via reflection.");
            }

            sender.sendMessage(ChatColor.GREEN + "Forced leveling on entity: " + entity.getType().name() + " (Level: " + metadata.level + ", Prestige: " + metadata.prestige + ")");
            return true;
        } else {
            sender.sendMessage(ChatColor.YELLOW + "Entity registered but not a LivingEntity (no leveling applied).");
            return true;
        }
    }
}
