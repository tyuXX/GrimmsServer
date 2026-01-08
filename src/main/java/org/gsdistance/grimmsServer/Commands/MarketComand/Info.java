package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.Data.Market.MarketBaseValues;
import org.gsdistance.grimmsServer.Shared;

public class Info {
    public static boolean subCommand(Player player) {
        Market market = Market.getMarket();
        player.sendMessage("Market:");
        player.sendMessage("Total item count: " + Shared.formatNumber(market.items.values().stream().mapToDouble(Long::doubleValue).sum()));
        double totalValue = 0.0;
        for (String string : market.items.keySet()) {
            Material material = Material.matchMaterial(string);
            if (material != null) {
                long amount = market.items.get(string);
                Double price = MarketBaseValues.marketBaseValues.get(material);
                if (price != null) {
                    totalValue += amount * price;
                } else {
                    totalValue += amount * 0.25; // Default price if not found
                }
            }
        }
        player.sendMessage("Total value: " + Shared.formatNumber(totalValue));
        player.sendMessage("Unique item count: " + market.items.size());
        return true;
    }
}