package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.Shared;

public class SellAll {
    public static boolean SubCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Market market = Market.getMarket();
            Material iS = ((Player) sender).getInventory().getItemInMainHand().getType();
            if (iS == Material.AIR) {
                sender.sendMessage("You are not holding anything.");
                return false;
            }
            Data<Double, Integer> sold = market.sellAll(iS, (Player) sender);
            market.saveMarket();
            sender.sendMessage("You sold " + sold.value + " many of " + iS.getKey() + " for " + Shared.formatNumber(Math.max(0.25D, Math.round((sold.key)))));
            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}
