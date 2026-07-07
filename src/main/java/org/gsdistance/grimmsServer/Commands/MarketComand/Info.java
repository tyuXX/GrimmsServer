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

        double totalValue = 0.0;

        // OPTIMIZATION: Iterating over entrySet() prevents redundant .get() lookups on every loop cycle
        for (Map.Entry<String, Long> entry : market.items.entrySet()) {
            Material material = Material.matchMaterial(entry.getKey());

            if (material != null) {
                long amount = entry.getValue();
                Double price = MarketBaseValues.marketBaseValues.get(material);

                // Cleaned up double casting syntax with a modern fallback assignment
                double unitPrice = (price != null) ? price : 0.25;
                totalValue += amount * unitPrice;
            }
        }

        player.sendMessage("Total value: " + Shared.formatNumber(totalValue));
        player.sendMessage("Unique item count: " + market.items.size());
        return true;
    }
}