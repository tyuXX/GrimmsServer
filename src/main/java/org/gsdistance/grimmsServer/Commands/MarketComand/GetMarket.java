package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Config.ConfigKey;
import org.gsdistance.grimmsServer.Shared;

public class GetMarket {
    public GetMarket() {
    }

    public static boolean SubCommand(CommandSender sender, String[] args) {
        Market market = Market.getMarket();
        sender.sendMessage(ChatColor.GOLD + "=== Market Overview ===");
        sender.sendMessage(ChatColor.GRAY + "Use " + ChatColor.YELLOW + "/market gui" + ChatColor.GRAY + " for a better interface!");

        double minPrice = ActiveConfig.getConfigValue(ConfigKey.MARKET_MIN_PRICE, Double.class);
        for (String item : market.items.keySet()) {
            Material material = Material.matchMaterial(item);
            if (material != null) {
                sender.sendMessage(ChatColor.YELLOW + "| " + item + ": " + ChatColor.GOLD + market.items.get(item) + ChatColor.GRAY + " - " + ChatColor.GREEN + Shared.formatNumber(Math.max(minPrice, Math.round(market.getPrice(material)))));
            } else {
                sender.sendMessage(ChatColor.RED + "| " + item + ": " + market.items.get(item) + " - Error: Unknown material");
            }
        }

        return true;
    }
}
