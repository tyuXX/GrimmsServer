package org.gsdistance.grimmsServer.Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.gsdistance.grimmsServer.Constructable.Market;

public class GetStockOfItem implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Please specify an item.");
            return false;
        }
        if (Material.matchMaterial(args[0]) == null) {
            sender.sendMessage("Invalid item id:" + args[0]);
            return false;
        }
        Market market = Market.getMarket();
        sender.sendMessage(args[0] + ": " + market.items.get(Material.matchMaterial(args[0]).getKey().toString()) + " with the price of " + Math.max(0.25D, Math.round((market.getPrice(Material.matchMaterial(args[0]))))));
        return true;
    }
}
