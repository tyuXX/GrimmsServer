package org.gsdistance.grimmsServer.Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.GrimmsServer;

public class BuyItem implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length < 2) {
                return false;
            }
            if (Material.matchMaterial(args[0]) == null) {
                sender.sendMessage("Invalid item id:" + args[0]);
                return false;
            }
            Market market = Market.getMarket();
            if (market.items.get(Material.matchMaterial(args[0]).getKey().toString()) <= Integer.parseInt(args[1])) {
                sender.sendMessage("Not enough stock of " + args[0] + " in the market.");
                return false;
            }
            double bought = market.buy(Material.matchMaterial(args[0]), Integer.parseInt(args[1]), (Player) sender);
            market.saveMarket();
            sender.sendMessage("You bought " + args[1] + " many of " + args[0] + " for " + Math.max(0.25D, Math.round((bought))));
            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}
