package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.Shared;

public class Buy {
    public static boolean SubCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (args.length < 3) { // Ensure args has at least 3 elements
                sender.sendMessage("Usage: /market buy <item> <amount>");
                return false;
            }
            if (Material.matchMaterial(args[1]) == null) {
                sender.sendMessage("Invalid item id: " + args[1]);
                return false;
            }
            int amount;
            try {
                amount = Integer.parseInt(args[2]);
                if (amount <= 0) {
                    sender.sendMessage("Amount must be greater than 0.");
                    return false;
                }
            } catch (NumberFormatException e) {
                sender.sendMessage("Invalid amount: " + args[2]);
                return false;
            }
            // Check if the item is in stock
            Market market = Market.getMarket();
            Long stock = market.items.get(Material.matchMaterial(args[1]).getKey().getKey());
            if (stock == null || stock < amount) {
                sender.sendMessage("Not enough stock of " + args[1] + " in the market.");
                return false;
            }
            double bought = market.buy(Material.matchMaterial(args[1]), amount, (Player) sender);
            market.saveMarket();
            sender.sendMessage("You bought " + args[2] + " many of " + args[1] + " for " + Shared.formatNumber(Math.max(0.25D, Math.round((bought)))));
            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}
