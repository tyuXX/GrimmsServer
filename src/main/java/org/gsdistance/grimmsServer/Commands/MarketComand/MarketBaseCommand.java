package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MarketBaseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        if (!(sender instanceof Player player)){
            return false;
        }

        return switch (args[0].toLowerCase()) {
            case "get" -> GetMarket.SubCommand(sender, args);
            case "stock" -> Stock.SubCommand(sender, args);
            case "ripoff" -> Ripoff.SubCommand(sender, args);
            case "enchant" -> BuyEnchantment.SubCommand(sender, args);
            case "tp" -> Tp.SubCommand(sender, args);
            case "sell" -> Sell.SubCommand(sender, args);
            case "sellall" -> SellAll.SubCommand(sender, args);
            case "buy" -> Buy.SubCommand(sender, args);
            case "enchcosts" -> GetEnchantCosts.SubCommand(sender, args);
            case "info" -> Info.subCommand(player);
            default -> false;
        };
    }
}