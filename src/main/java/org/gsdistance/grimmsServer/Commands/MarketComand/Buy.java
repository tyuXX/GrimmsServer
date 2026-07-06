package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Constructable.Market;

public class Buy {
   public Buy() {
   }

   public static boolean SubCommand(CommandSender sender, String[] args) {
      if (!(sender instanceof Player)) {
         sender.sendMessage("This command can only be run by a player.");
         return false;
      } else if (args.length < 3) {
         sender.sendMessage("Usage: /market buy <item> <amount>");
         return false;
      } else if (Material.matchMaterial(args[1]) == null) {
         sender.sendMessage("Invalid item id: " + args[1]);
         return false;
      } else {
         int amount;
         try {
            amount = Integer.parseInt(args[2]);
            if (amount <= 0) {
               sender.sendMessage("Amount must be greater than 0.");
               return false;
            }
         } catch (NumberFormatException var7) {
            sender.sendMessage("Invalid amount: " + args[2]);
            return false;
         }

         Market market = Market.getMarket();
         Long stock = (Long)market.items.get(Material.matchMaterial(args[1]).getKey().getKey());
         if (stock != null && stock >= (long)amount) {
            double bought = market.buy(Material.matchMaterial(args[1]), amount, (Player)sender);
            market.saveMarket();
            sender.sendMessage("You bought " + args[2] + " many of " + args[1] + " for " + Shared.formatNumber(Math.max((double)0.25F, (double)Math.round(bought))));
            return true;
         } else {
            sender.sendMessage("Not enough stock of " + args[1] + " in the market.");
            return false;
         }
      }
   }
}
