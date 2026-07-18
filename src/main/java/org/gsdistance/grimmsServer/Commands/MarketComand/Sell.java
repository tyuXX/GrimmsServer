package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.Shared;

public class Sell {
    public Sell() {
    }

    public static boolean SubCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
            return false;
        }

        Market market = Market.getMarket();
        ItemStack itemStack = ((Player) sender).getInventory().getItemInMainHand();

        if (itemStack.getType() == Material.AIR) {
            sender.sendMessage(ChatColor.RED + "You are not holding anything to sell.");
            return false;
        }

        double sold = market.sell(itemStack, (Player) sender);
        market.saveMarket();

        if (sold <= 0) {
            sender.sendMessage(ChatColor.RED + "This item cannot be sold or has no value.");
            return false;
        }

        sender.sendMessage(ChatColor.GREEN + "You sold " + ChatColor.GOLD + itemStack.getAmount() + ChatColor.GREEN + " of " + ChatColor.YELLOW + itemStack.getType().getKey().getKey() + ChatColor.GREEN + " for " + ChatColor.GOLD + Shared.formatNumber(Math.max(0.25, Math.round(sold))));
        return true;
    }
}
