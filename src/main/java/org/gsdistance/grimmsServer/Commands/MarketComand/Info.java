package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.Data.Market.MarketBaseValues;
import org.gsdistance.grimmsServer.Shared;

import java.util.Map;

public class Info {

    public Info() {
    }

    @SuppressWarnings("unchecked")
    public static boolean subCommand(Player player) {
        Market market = Market.getMarket();

        player.sendMessage("Market:");

        // FIXED: Inlined the stream pipeline cleanly to eliminate the broken raw 'Stream var10001' allocation
        double totalItemCount = market.items.values().stream()
                .mapToDouble(Long::doubleValue)
                .sum();

        player.sendMessage("Total item count: " + Shared.formatNumber(totalItemCount));

        double totalValue = Math.pow(market.NegMarketSaturation, 4);

        player.sendMessage("Total value: " + Shared.formatNumber(totalValue));
        player.sendMessage("Unique item count: " + market.items.size());
        return true;
    }
}