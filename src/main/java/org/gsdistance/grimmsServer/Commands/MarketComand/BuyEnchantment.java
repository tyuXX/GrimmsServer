package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Constructable.Item.ItemLevelHandler;
import org.gsdistance.grimmsServer.Constructable.Item.RelicHandler;
import org.gsdistance.grimmsServer.Data.Market.EnchantBaseValues;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class BuyEnchantment {
    public static boolean SubCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (args.length < 2) {
                sender.sendMessage("Usage: /market enchant <enchantment>");
                return false;
            }
            Enchantment enchantment = Enchantment.getByKey(org.bukkit.NamespacedKey.minecraft(args[1].toLowerCase()));
            if (enchantment == null || !EnchantBaseValues.enchantBaseValues.containsKey(enchantment)) {
                sender.sendMessage("Enchantment not listed or nonexistent.");
                return false;
            }
            ItemStack itemToApply = ((Player) sender).getInventory().getItemInMainHand();
            int enchantBonus = Math.toIntExact(Math.min((Math.round(Math.sqrt(ItemLevelHandler.getLevelHandler(itemToApply, (Player) sender).getLevel()))), 100));
            if (RelicHandler.isRelic(itemToApply)) {
                RelicHandler relicHandler = RelicHandler.getRelicHandler(itemToApply);
                enchantBonus += (int) Math.min(Math.sqrt(relicHandler.getRelicGrade()) * relicHandler.getRelicTier(), 100);
            }
            int max = enchantment.getMaxLevel() == 1 ? 1 : enchantment.getMaxLevel() + enchantBonus;
            if (itemToApply.getEnchantmentLevel(enchantment) >= max) {
                sender.sendMessage("Enchantment already maxed out.");
                return false;
            }
            PlayerStats playerStats = PlayerStats.getPlayerStats(((Player) sender));
            double cost = EnchantBaseValues.enchantBaseValues.get(enchantment) * (itemToApply.getEnchantmentLevel(enchantment) + 1);
            if (playerStats.getStat("money", Double.class) < cost) {
                sender.sendMessage("Not enough money.");
                return false;
            }
            int oldLevel = itemToApply.getEnchantmentLevel(enchantment);
            int newLevel = oldLevel + 1;
            itemToApply.addUnsafeEnchantment(enchantment, newLevel);
            playerStats.setStat("money", playerStats.getStat("money", Double.class) - cost);
            sender.sendMessage("Upgraded the enchantment " + enchantment.getKey().getKey() + " from level " + oldLevel + " to level " + newLevel + " for " + Shared.formatNumber(cost) + " money.");
            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}
