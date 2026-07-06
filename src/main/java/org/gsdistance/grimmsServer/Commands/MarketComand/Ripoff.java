package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.Data.Market.MarketBaseValues;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class Ripoff {
   public Ripoff() {
   }

   public static boolean SubCommand(CommandSender sender, String[] args) {
      if (sender instanceof Player player) {
         if (args.length < 3) {
            sender.sendMessage("Usage: /market ripoff <item> <amount>");
            return false;
         } else {
            Material material = Material.matchMaterial(args[1]);
            if (material != null && MarketBaseValues.marketBaseValues.containsKey(material)) {
               int amount;
               try {
                  amount = Integer.parseInt(args[2]);
                  if (amount < 1) {
                     sender.sendMessage("You must buy at least 1 item.");
                     return false;
                  }
               } catch (NumberFormatException var10) {
                  sender.sendMessage("Invalid amount: " + args[2]);
                  return false;
               }

               PlayerStats stats = PlayerStats.getPlayerStats(player);
               double price = (Double)MarketBaseValues.marketBaseValues.get(material) * (double)10.0F + Market.getMarket().NegMarketSaturation;
               int bought = 0;

               for(int i = 0; i < amount && (Double)stats.getStat("money", Double.class) >= price; ++i) {
                  stats.setStat("money", (Double)stats.getStat("money", Double.class) - price);
                  ++bought;
               }

               player.getInventory().addItem(new ItemStack[]{new ItemStack(material, bought)});
               sender.sendMessage("You bought " + bought + " of " + args[1] + " for " + price * (double)bought + " money.");
               return true;
            } else {
               sender.sendMessage("Invalid or unavailable item: " + args[1]);
               return false;
            }
         }
      } else {
         sender.sendMessage("This command can only be run by a player.");
         return false;
      }
   }
}
