package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.gsdistance.grimmsServer.Data.EnchantBaseValues;

public class GetEnchantCosts {
    public static boolean SubCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("grimmsserver.market.enchant")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }
        sender.sendMessage("__Enchant costs:");
        for (Enchantment enchantment : EnchantBaseValues.enchantBaseValues.keySet()) {
            sender.sendMessage("|" + enchantment.getKey() + ": " + EnchantBaseValues.enchantBaseValues.get(enchantment) + "*level");
        }
        return true;
    }
}
