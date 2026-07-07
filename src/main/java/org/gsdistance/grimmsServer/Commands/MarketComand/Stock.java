package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.gsdistance.grimmsServer.Constructable.Market;

public class Stock {
    public Stock() {
    }

    public static boolean SubCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Usage: /market stock <item>");
            return false;
        } else {
            Material mat = Material.matchMaterial(args[1]);
            if (mat == null) {
                sender.sendMessage("Invalid item id: " + args[1]);
                return false;
            } else {
                Market market = Market.getMarket();
                Long stock = market.items.get(mat.getKey().getKey());
                sender.sendMessage(args[1] + ": " + (stock != null ? stock : 0L) + " with the price of " + Math.max(0.25F, (double) Math.round(market.getPrice(mat))));
                return true;
            }
        }
    }
}
