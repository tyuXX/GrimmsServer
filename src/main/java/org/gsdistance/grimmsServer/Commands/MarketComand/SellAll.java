package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Config.ConfigKey;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.Shared;

public class SellAll {
    public SellAll() {
    }

    public static boolean SubCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
            return false;
        }
        
        Market market = Market.getMarket();
        Material iS = ((Player) sender).getInventory().getItemInMainHand().getType();
        if (iS == Material.AIR) {
            sender.sendMessage(ChatColor.RED + "You are not holding anything.");
            return false;
        } else {
            Data<Double, Integer> sold = market.sellAll(iS, (Player) sender);
            market.saveMarket();
            double minPrice = ActiveConfig.getConfigValue(ConfigKey.MARKET_MIN_PRICE, Double.class);
            sender.sendMessage(ChatColor.GREEN + "You sold " + ChatColor.GOLD + sold.value() + ChatColor.GREEN + " of " + ChatColor.YELLOW + iS.getKey() + ChatColor.GREEN + " for " + ChatColor.GOLD + Shared.formatNumber(Math.max(minPrice, Math.round(sold.key()))));
            return true;
        }
    }
}
