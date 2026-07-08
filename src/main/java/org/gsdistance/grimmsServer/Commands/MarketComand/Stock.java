package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Config.ConfigKey;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.Shared;

public class Stock {
    public Stock() {
    }

    public static boolean SubCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /market stock <item>");
            return false;
        } else {
            Material mat = Material.matchMaterial(args[1]);
            if (mat == null) {
                sender.sendMessage(ChatColor.RED + "Invalid item: '" + args[1] + "'. Use /market stock to see available items.");
                return false;
            } else {
                Market market = Market.getMarket();
                Long stock = market.items.get(mat.getKey().getKey());
                double minPrice = ActiveConfig.getConfigValue(ConfigKey.MARKET_MIN_PRICE, Double.class);
                sender.sendMessage(ChatColor.YELLOW + args[1] + ChatColor.GRAY + ": " + ChatColor.GOLD + (stock != null ? stock : 0L) + ChatColor.GRAY + " in stock");
                sender.sendMessage(ChatColor.GRAY + "Price: " + ChatColor.GOLD + Shared.formatNumber(Math.max(minPrice, Math.round(market.getPrice(mat)))));
                return true;
            }
        }
    }
}
