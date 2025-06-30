package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.gsdistance.grimmsServer.Constructable.Market;

public class GetMarket {
    @SuppressWarnings("SameReturnValue")
    public static boolean SubCommand(CommandSender sender, String[] args) {
        Market market = Market.getMarket();
        sender.sendMessage("__Market:");
        for (String item : market.items.keySet()) {
            Material material = Material.matchMaterial(item);
            if (material != null) {
                sender.sendMessage("|" + item + ": " + market.items.get(item) + " - " + Math.max(0.25D, Math.round(market.getPrice(material))));
            } else {
                sender.sendMessage("|" + item + ": " + market.items.get(item) + " - Error:" + item);
            }
        }
        return true;
    }
}
