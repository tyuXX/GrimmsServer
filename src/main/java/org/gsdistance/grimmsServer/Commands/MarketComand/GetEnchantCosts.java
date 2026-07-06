package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Data.Market.EnchantBaseValues;

public class GetEnchantCosts {
   public GetEnchantCosts() {
   }

   public static boolean SubCommand(CommandSender sender, String[] args) {
      sender.sendMessage("__Enchant costs:");

      for(Enchantment enchantment : EnchantBaseValues.enchantBaseValues.keySet()) {
         String var10001 = String.valueOf(enchantment.getKey());
         sender.sendMessage("|" + var10001 + ": " + Shared.formatNumber((Double)EnchantBaseValues.enchantBaseValues.get(enchantment)) + "*level");
      }

      return true;
   }
}
