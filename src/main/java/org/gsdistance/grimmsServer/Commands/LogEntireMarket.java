package org.gsdistance.grimmsServer.Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.gsdistance.grimmsServer.Constructable.Market;

public class LogEntireMarket implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
