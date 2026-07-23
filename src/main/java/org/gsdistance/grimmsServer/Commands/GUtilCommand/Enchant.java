package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Constructable.Item.CustomEnchantmentHandler;
import org.gsdistance.grimmsServer.Data.CustomEnchantment;

import java.util.Map;

public class Enchant {

    public Enchant() {
    }

    public static boolean subCommand(Player player, String[] args) {
        // Guard: Permission Check
        if (!player.hasPermission("grimmsserver.util.admin")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return false;
        }

        // Guard: Minimum arguments requirement
        if (args.length < 2) {
            return false;
        }

        String action = args[1].toLowerCase();
        ItemStack item = player.getInventory().getItemInMainHand();
        
        if (item == null || item.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "You must hold an item in your main hand.");
            return false;
        }

        switch (action) {
            case "add" -> {
                if (args.length < 4) {
                    player.sendMessage(ChatColor.RED + "Usage: /gutil enchant add <enchantment> [level]");
                    return false;
                }

                try {
                    CustomEnchantment enchantment = CustomEnchantment.valueOf(args[2].toUpperCase());
                    int level = args.length >= 4 ? Integer.parseInt(args[3]) : 1;

                    CustomEnchantmentHandler handler = CustomEnchantmentHandler.getHandler(item);
                    if (handler.addEnchantment(enchantment, level)) {
                        player.sendMessage(ChatColor.GREEN + "Added " + ChatColor.GOLD + enchantment.enchantmentName + 
                                ChatColor.GREEN + " level " + ChatColor.YELLOW + level + ChatColor.GREEN + " to " + item.getType());
                        return true;
                    } else {
                        player.sendMessage(ChatColor.RED + "Failed to add enchantment. Check if level exceeds maximum or enchantment already exists.");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Invalid level: " + args[3]);
                    return false;
                } catch (IllegalArgumentException e) {
                    player.sendMessage(ChatColor.RED + "Invalid enchantment: " + args[2]);
                    return false;
                }
            }

            case "remove" -> {
                if (args.length < 3) {
                    player.sendMessage(ChatColor.RED + "Usage: /gutil enchant remove <enchantment>");
                    return false;
                }

                try {
                    CustomEnchantment enchantment = CustomEnchantment.valueOf(args[2].toUpperCase());

                    CustomEnchantmentHandler handler = CustomEnchantmentHandler.getHandler(item);
                    if (handler.removeEnchantment(enchantment)) {
                        player.sendMessage(ChatColor.GREEN + "Removed " + ChatColor.GOLD + enchantment.enchantmentName + ChatColor.GREEN + " from " + item.getType());
                        return true;
                    } else {
                        player.sendMessage(ChatColor.RED + "Item does not have that enchantment.");
                        return false;
                    }
                } catch (IllegalArgumentException e) {
                    player.sendMessage(ChatColor.RED + "Invalid enchantment: " + args[2]);
                    return false;
                }
            }

            case "set" -> {
                if (args.length < 4) {
                    player.sendMessage(ChatColor.RED + "Usage: /gutil enchant set <enchantment> <level>");
                    return false;
                }

                try {
                    CustomEnchantment enchantment = CustomEnchantment.valueOf(args[2].toUpperCase());
                    int level = Integer.parseInt(args[3]);

                    CustomEnchantmentHandler handler = CustomEnchantmentHandler.getHandler(item);
                    if (handler.setEnchantmentLevel(enchantment, level)) {
                        player.sendMessage(ChatColor.GREEN + "Set " + ChatColor.GOLD + enchantment.enchantmentName + 
                                ChatColor.GREEN + " to level " + ChatColor.YELLOW + level + ChatColor.GREEN + " on " + item.getType());
                        return true;
                    } else {
                        player.sendMessage(ChatColor.RED + "Failed to set enchantment level. Check if level is valid.");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Invalid level: " + args[3]);
                    return false;
                } catch (IllegalArgumentException e) {
                    player.sendMessage(ChatColor.RED + "Invalid enchantment: " + args[2]);
                    return false;
                }
            }

            case "list" -> {
                CustomEnchantmentHandler handler = CustomEnchantmentHandler.getHandler(item);
                Map<CustomEnchantment, Integer> enchantments = handler.getAllEnchantments();

                if (enchantments.isEmpty()) {
                    player.sendMessage(ChatColor.GRAY + "Item has no custom enchantments.");
                    return true;
                }

                player.sendMessage(ChatColor.GOLD + "Custom Enchantments on " + item.getType() + ":");
                for (Map.Entry<CustomEnchantment, Integer> entry : enchantments.entrySet()) {
                    player.sendMessage(ChatColor.YELLOW + "- " + entry.getKey().enchantmentName + 
                            ChatColor.GRAY + " (Level " + entry.getValue() + "/" + entry.getKey().maxLevel + ")");
                }
                return true;
            }

            case "clear" -> {
                CustomEnchantmentHandler handler = CustomEnchantmentHandler.getHandler(item);
                handler.clearAllEnchantments();
                player.sendMessage(ChatColor.GREEN + "Cleared all custom enchantments from " + item.getType());
                return true;
            }

            case "has" -> {
                if (args.length < 3) {
                    player.sendMessage(ChatColor.RED + "Usage: /gutil enchant has <enchantment>");
                    return false;
                }

                try {
                    CustomEnchantment enchantment = CustomEnchantment.valueOf(args[2].toUpperCase());
                    CustomEnchantmentHandler handler = CustomEnchantmentHandler.getHandler(item);
                    
                    if (handler.hasEnchantment(enchantment)) {
                        int level = handler.getEnchantmentLevel(enchantment);
                        player.sendMessage(ChatColor.GREEN + "Item has " + ChatColor.GOLD + enchantment.enchantmentName + 
                                ChatColor.GREEN + " at level " + ChatColor.YELLOW + level);
                    } else {
                        player.sendMessage(ChatColor.RED + "Item does not have " + enchantment.enchantmentName);
                    }
                    return true;
                } catch (IllegalArgumentException e) {
                    player.sendMessage(ChatColor.RED + "Invalid enchantment: " + args[2]);
                    return false;
                }
            }

            default -> {
                player.sendMessage(ChatColor.RED + "Unknown enchant command: " + action);
                return false;
            }
        }
    }
}
