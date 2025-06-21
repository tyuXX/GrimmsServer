package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MarketBaseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /market <get|stock|ripoff|enchant|tp|sell|buy> [args]");
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "get":
                return GetMarket.SubCommand(sender, args);
            case "stock":
                return GetMarketStock.SubCommand(sender, args);
            case "ripoff":
                return BuyRipoff.SubCommand(sender, args);
            case "enchant":
                return BuyEnchantment.SubCommand(sender, args);
            case "tp":
                return BuyTp.SubCommand(sender, args);
            case "sell":
                return SellItem.SubCommand(sender, args);
            case "buy":
                return BuyItem.SubCommand(sender, args);
            default:
                sender.sendMessage("Unknown subcommand. Usage: /market <get|stock|ripoff|enchant|tp|sell|buy> [args]");
                return false;
        }
    }
}