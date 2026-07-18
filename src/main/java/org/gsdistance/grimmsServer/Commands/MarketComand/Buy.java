package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.Shared;

public class Buy {
    public Buy() {
    }

    public static boolean SubCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
            return false;
        }

        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /market buy <item> <amount>");
            return false;
        }

        Material material = Material.matchMaterial(args[1]);
        if (material == null) {
            sender.sendMessage(ChatColor.RED + "Invalid item: '" + args[1] + "'. Use /market stock to see available items.");
            return false;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[2]);
            if (amount <= 0) {
                sender.sendMessage(ChatColor.RED + "Amount must be greater than 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid amount: '" + args[2] + "'. Must be a whole number.");
            return false;
        }

        Market market = Market.getMarket();
        Long stock = market.items.get(material.getKey().getKey());

        if (stock == null) {
            sender.sendMessage(ChatColor.RED + "Item '" + args[1] + "' is not available in the market.");
            return false;
        }

        if (stock < amount) {
            sender.sendMessage(ChatColor.RED + "Not enough stock of " + ChatColor.YELLOW + args[1] + ChatColor.RED + ". Available: " + ChatColor.GOLD + stock);
            return false;
        }

        double bought = market.buy(material, amount, (Player) sender);
        market.saveMarket();
        sender.sendMessage(ChatColor.GREEN + "You bought " + ChatColor.GOLD + amount + ChatColor.GREEN + " of " + ChatColor.YELLOW + args[1] + ChatColor.GREEN + " for " + ChatColor.GOLD + Shared.formatNumber(Math.max(0.25, Math.round(bought))));
        return true;
    }
}
