package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MarketBaseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        return switch (args[0].toLowerCase()) {
            case "get" -> GetMarket.SubCommand(sender, args);
            case "stock" -> GetMarketStock.SubCommand(sender, args);
            case "ripoff" -> BuyRipoff.SubCommand(sender, args);
            case "enchant" -> BuyEnchantment.SubCommand(sender, args);
            case "tp" -> BuyTp.SubCommand(sender, args);
            case "sell" -> SellItem.SubCommand(sender, args);
            case "buy" -> BuyItem.SubCommand(sender, args);
            case "enchcosts" -> GetEnchantCosts.SubCommand(sender, args);
            default -> false;
        };
    }
}