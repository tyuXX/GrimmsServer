package org.gsdistance.grimmsServer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.gsdistance.grimmsServer.Data.EnchantBaseValues;

public class LogEnchantmentCosts implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("__Enchant costs:");
        for (Enchantment enchantment : EnchantBaseValues.enchantBaseValues.keySet()) {
            sender.sendMessage("|" + enchantment.getKey() + ": " + EnchantBaseValues.enchantBaseValues.get(enchantment) + "*level");
        }
        return true;
    }
}
