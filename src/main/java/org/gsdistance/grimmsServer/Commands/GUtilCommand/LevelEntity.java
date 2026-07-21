package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.gsdistance.grimmsServer.Constructable.Entity.EntityMetadata;
import org.gsdistance.grimmsServer.Manage.CustomEntityManager;

public class LevelEntity {
    public LevelEntity() {
    }

    public static boolean subCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("grimmsserver.util.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return false;
        }

        if (sender instanceof Player player) {
            if (args.length < 2) {
                // Force leveling on entity player is looking at
                RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getEyeLocation().getDirection(), 5);
                Entity targetEntity = rayTrace != null ? rayTrace.getHitEntity() : null;
                if (targetEntity == null) {
                    sender.sendMessage(ChatColor.RED + "No entity found in range (5 blocks). Specify an entity UUID or look at an entity.");
                    return false;
                }
                return applyLeveling(sender, targetEntity);
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
                    return applyLeveling(sender, targetEntity);
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

    private static boolean applyLeveling(CommandSender sender, Entity entity) {
        // Register entity in the registry
        CustomEntityManager.registerEntity(entity);
        
        // Get or create metadata and apply leveling
        EntityMetadata metadata = EntityMetadata.getEntityMetadata(entity);
        
        if (entity instanceof LivingEntity livingEntity) {
            // Force re-apply leveling even if already leveled
            if (metadata.level > 1 || metadata.prestige > 1) {
                // Re-apply to refresh stats
                try {
                    java.lang.reflect.Method applyLevelling = EntityMetadata.class.getDeclaredMethod("applyLevelling", LivingEntity.class, EntityMetadata.class);
                    applyLevelling.setAccessible(true);
                    applyLevelling.invoke(null, livingEntity, metadata);
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.YELLOW + "Warning: Could not re-apply leveling via reflection.");
                }
            }
            
            sender.sendMessage(ChatColor.GREEN + "Forced leveling on entity: " + entity.getType().name() + " (Level: " + metadata.level + ", Prestige: " + metadata.prestige + ")");
            return true;
        } else {
            sender.sendMessage(ChatColor.YELLOW + "Entity registered but not a LivingEntity (no leveling applied).");
            return true;
        }
    }
}
