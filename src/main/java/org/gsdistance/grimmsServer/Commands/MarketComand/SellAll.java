package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Constructable.Market;

public class SellAll {
   public SellAll() {
   }

   public static boolean SubCommand(CommandSender sender, String[] args) {
      if (sender instanceof Player) {
         Market market = Market.getMarket();
         Material iS = ((Player)sender).getInventory().getItemInMainHand().getType();
         if (iS == Material.AIR) {
            sender.sendMessage("You are not holding anything.");
            return false;
         } else {
            Data<Double, Integer> sold = market.sellAll(iS, (Player)sender);
            market.saveMarket();
            String var10001 = String.valueOf(sold.value());
            sender.sendMessage("You sold " + var10001 + " many of " + String.valueOf(iS.getKey()) + " for " + Shared.formatNumber(Math.max((double)0.25F, (double)Math.round((Double)sold.key()))));
            return true;
         }
      } else {
         sender.sendMessage("This command can only be run by a player.");
         return false;
      }
   }
}
