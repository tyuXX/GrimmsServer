package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.gsdistance.grimmsServer.Constructable.Market;

public class GetMarketStock {
    public static boolean SubCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("grimmsserver.market.stock")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage("Usage: /market stock <item>");
            return false;
        }
        Material mat = Material.matchMaterial(args[1]);
        if (mat == null) {
            sender.sendMessage("Invalid item id: " + args[1]);
            return false;
        }
        Market market = Market.getMarket();
        sender.sendMessage(args[1] + ": " + market.items.get(mat.getKey().toString()) + " with the price of " + Math.max(0.25D, Math.round((market.getPrice(mat)))));
        return true;
    }
}
