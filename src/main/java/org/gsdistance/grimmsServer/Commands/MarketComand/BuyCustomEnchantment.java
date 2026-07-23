package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Constructable.Item.CustomEnchantmentHandler;
import org.gsdistance.grimmsServer.Constructable.Item.ItemLevelHandler;
import org.gsdistance.grimmsServer.Constructable.Item.RelicHandler;
import org.gsdistance.grimmsServer.Data.CustomEnchantment;
import org.gsdistance.grimmsServer.Data.Market.EnchantBaseValues;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class BuyCustomEnchantment {
    public BuyCustomEnchantment() {
    }

    public static boolean SubCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /market customenchant <enchantment>");
                return false;
            } else {
                try {
                    CustomEnchantment enchantment = CustomEnchantment.valueOf(args[1].toUpperCase());
                    if (EnchantBaseValues.customEnchantBaseValues.containsKey(enchantment)) {
                        ItemStack itemToApply = player.getInventory().getItemInMainHand();
                        if (itemToApply.getAmount() > 1) {
                            sender.sendMessage(ChatColor.RED + "You can only enchant one item at a time. Please unstack the item first.");
                            return false;
                        }

                        CustomEnchantmentHandler enchantHandler = CustomEnchantmentHandler.getHandler(itemToApply);
                        int currentLevel = enchantHandler.getEnchantmentLevel(enchantment);

                        ItemLevelHandler itemLevelHandler = ItemLevelHandler.getLevelHandler(itemToApply, player.getPlayer());
                        double itemLevel = itemLevelHandler.getLevel();
                        
                        // Respect the hard max level from CustomEnchantment
                        if (currentLevel >= enchantment.maxLevel || Math.sqrt(itemLevel) / 2 < currentLevel + 1) {
                            sender.sendMessage(ChatColor.RED + "Enchantment already at maximum level (" + enchantment.maxLevel + ").");
                            return false;
                        }

                        PlayerStats playerStats = PlayerStats.getPlayerStats(player);
                        
                        // Simple cost calculation based on current level
                        double cost = EnchantBaseValues.customEnchantBaseValues.get(enchantment) * (currentLevel + 1);
                        
                        if (playerStats.getStat("money", Double.class) < cost) {
                            sender.sendMessage(ChatColor.RED + "Not enough money. Cost: " + ChatColor.GOLD + Shared.formatNumber(cost));
                            return false;
                        } else {
                            int oldLevel = currentLevel;
                            int newLevel = oldLevel + 1;
                            
                            if (enchantHandler.setEnchantmentLevel(enchantment, newLevel)) {
                                playerStats.setStat("money", playerStats.getStat("money", Double.class) - cost);
                                sender.sendMessage(ChatColor.GREEN + "Upgraded the custom enchantment " + ChatColor.GOLD + enchantment.enchantmentName + 
                                        ChatColor.GREEN + " from level " + ChatColor.YELLOW + oldLevel + 
                                        ChatColor.GREEN + " to level " + ChatColor.YELLOW + newLevel + 
                                        ChatColor.GREEN + " for " + ChatColor.GOLD + Shared.formatNumber(cost) + ChatColor.GREEN + " money.");
                                return true;
                            } else {
                                sender.sendMessage(ChatColor.RED + "Failed to upgrade enchantment.");
                                return false;
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Custom enchantment not listed or nonexistent.");
                        return false;
                    }
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid custom enchantment: " + args[1]);
                    return false;
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
            return false;
        }
    }
}
