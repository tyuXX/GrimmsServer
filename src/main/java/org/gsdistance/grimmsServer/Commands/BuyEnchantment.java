package org.gsdistance.grimmsServer.Commands;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Data.EnchantBaseValues;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class BuyEnchantment implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length < 1) {
                return false;
            }
            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(args[0].toLowerCase()));
            if (enchantment == null || !EnchantBaseValues.enchantBaseValues.containsKey(enchantment)) {
                sender.sendMessage("Enchantment not listed or nonexistent.");
                return false;
            }
            ItemStack itemToApply = ((Player) sender).getInventory().getItemInMainHand();
            if (itemToApply.getEnchantmentLevel(enchantment) >= enchantment.getMaxLevel()) {
                sender.sendMessage("Enchantment already maxed out.");
                return false;
            }
            PlayerStats playerStats = PlayerStats.getPlayerStats(((Player) sender));
            double cost = EnchantBaseValues.enchantBaseValues.get(enchantment) * (itemToApply.getEnchantmentLevel(enchantment) + 1);
            if ((Double) playerStats.getStat("money") < cost) {
                sender.sendMessage("Not enough money");
                return false;
            }
            itemToApply.addEnchantment(enchantment, itemToApply.getEnchantmentLevel(enchantment) + 1);
            playerStats.setStat("money", (Double) playerStats.getStat("money") - cost);
            sender.sendMessage("Upgraded the enchant " + enchantment.getKey() + " from " + (itemToApply.getEnchantmentLevel(enchantment) - 1) + " to " + itemToApply.getEnchantmentLevel(enchantment) + " for " + cost + " money.");
            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}
