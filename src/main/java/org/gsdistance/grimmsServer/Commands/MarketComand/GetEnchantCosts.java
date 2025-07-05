package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.gsdistance.grimmsServer.Data.EnchantBaseValues;
import org.gsdistance.grimmsServer.Shared;

public class GetEnchantCosts {
    @SuppressWarnings("SameReturnValue")
    public static boolean SubCommand(CommandSender sender, String[] args) {
        sender.sendMessage("__Enchant costs:");
        for (Enchantment enchantment : EnchantBaseValues.enchantBaseValues.keySet()) {
            sender.sendMessage("|" + enchantment.getKey() + ": " + Shared.formatNumber(EnchantBaseValues.enchantBaseValues.get(enchantment)) + "*level");
        }
        return true;
    }
}
