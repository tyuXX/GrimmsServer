package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.gsdistance.grimmsServer.Constructable.Entity.EntityMetadata;
import org.gsdistance.grimmsServer.Manage.CustomEntityManager;

public class UnlevelEntity {
    public UnlevelEntity() {
    }

    public static boolean subCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("grimmsserver.util.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return false;
        }

        if (sender instanceof Player player) {
            if (args.length < 2) {
                // Remove leveling from entity player is looking at
                RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getEyeLocation().getDirection(), 5);
                Entity targetEntity = rayTrace != null ? rayTrace.getHitEntity() : null;
                if (targetEntity == null) {
                    sender.sendMessage(ChatColor.RED + "No entity found in range (5 blocks). Specify an entity UUID or look at an entity.");
                    return false;
                }
                return removeLeveling(sender, targetEntity);
            } else {
                // Remove leveling by UUID
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
                    return removeLeveling(sender, targetEntity);
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

    private static boolean removeLeveling(CommandSender sender, Entity entity) {
        // Remove from registry
        CustomEntityManager.unregisterEntity(entity);
        
        // Remove metadata file
        try {
            java.io.File metadataFile = new java.io.File(
                org.gsdistance.grimmsServer.GrimmsServer.instance.getDataFolder(),
                "entityMetadata/" + entity.getUniqueId() + ".json"
            );
            if (metadataFile.exists()) {
                metadataFile.delete();
            }
        } catch (Exception e) {
            sender.sendMessage(ChatColor.YELLOW + "Warning: Could not delete metadata file.");
        }

        // Remove from session storage
        org.gsdistance.grimmsServer.Data.PerSessionDataStorage.dataStore.remove("entityMetadata-" + entity.getUniqueId());

        // Reset entity stats if it's a living entity
        if (entity instanceof LivingEntity livingEntity) {
            // Reset attributes to default values
            if (livingEntity.getAttribute(Attribute.MAX_HEALTH) != null) {
                livingEntity.getAttribute(Attribute.MAX_HEALTH).setBaseValue(livingEntity.getAttribute(Attribute.MAX_HEALTH).getDefaultValue());
                livingEntity.setHealth(livingEntity.getAttribute(Attribute.MAX_HEALTH).getValue());
            }
            if (livingEntity.getAttribute(Attribute.ARMOR) != null) {
                livingEntity.getAttribute(Attribute.ARMOR).setBaseValue(livingEntity.getAttribute(Attribute.ARMOR).getDefaultValue());
            }
            if (livingEntity.getAttribute(Attribute.ARMOR_TOUGHNESS) != null) {
                livingEntity.getAttribute(Attribute.ARMOR_TOUGHNESS).setBaseValue(livingEntity.getAttribute(Attribute.ARMOR_TOUGHNESS).getDefaultValue());
            }
            if (livingEntity.getAttribute(Attribute.ATTACK_DAMAGE) != null) {
                livingEntity.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(livingEntity.getAttribute(Attribute.ATTACK_DAMAGE).getDefaultValue());
            }

            // Reset custom name
            EntityMetadata metadata = EntityMetadata.getEntityMetadata(entity);
            if (metadata != null && metadata.originalName != null) {
                livingEntity.setCustomName(metadata.originalName);
            } else {
                livingEntity.setCustomName(null);
            }
            livingEntity.setCustomNameVisible(false);
        }

        sender.sendMessage(ChatColor.GREEN + "Removed leveling from entity: " + entity.getType().name() + " (" + entity.getUniqueId() + ")");
        return true;
    }
}
