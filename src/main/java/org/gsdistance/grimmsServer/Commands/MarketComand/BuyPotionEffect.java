package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Data.Market.PotionEffectBaseValues;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class BuyPotionEffect {
   public BuyPotionEffect() {
   }

   public static boolean SubCommand(CommandSender sender, String[] args) {
      if (sender instanceof Player player) {
         if (args.length < 4) {
            sender.sendMessage("Usage: /market buyPotionEffect <effect> <duration> <level>");
            return false;
         } else {
            PotionEffectType effectType = PotionEffectType.getByName(args[1].toUpperCase());
            if (effectType != null && PotionEffectBaseValues.potionEffectBaseValues.containsKey(effectType)) {
               int duration;
               try {
                  duration = Integer.parseInt(args[2]);
                  if (duration <= 0) {
                     sender.sendMessage("Duration must be a positive number.");
                     return false;
                  }
               } catch (NumberFormatException var10) {
                  sender.sendMessage("Invalid duration. Must be a positive number.");
                  return false;
               }

               int amplifier;
               try {
                  amplifier = Integer.parseInt(args[3]);
                  if (amplifier < 1) {
                     sender.sendMessage("Level must be at least 1.");
                     return false;
                  }
               } catch (NumberFormatException var11) {
                  sender.sendMessage("Invalid level. Must be a positive number.");
                  return false;
               }

               if (PotionEffectBaseValues.potionEffectAmplitudeLimits.containsKey(effectType)) {
                  int maxAmplifier = (Integer)PotionEffectBaseValues.potionEffectAmplitudeLimits.get(effectType);
                  if (amplifier > maxAmplifier) {
                     sender.sendMessage("Level " + amplifier + " exceeds the maximum allowed level of " + maxAmplifier + " for " + effectType.getName() + ".");
                     return false;
                  }
               }

               PlayerStats playerStats = PlayerStats.getPlayerStats(player);
               double cost = PotionEffectBaseValues.getPotionEffectBaseValue(effectType, duration, amplifier);
               if (cost == Double.MAX_VALUE) {
                  sender.sendMessage("Level exceeds maximum allowed for this potion effect.");
                  return false;
               } else if ((Double)playerStats.getStat("money", Double.class) < cost) {
                  sender.sendMessage("Not enough money. Cost: " + Shared.formatNumber(cost) + " money.");
                  return false;
               } else {
                  PotionEffect potionEffect = new PotionEffect(effectType, duration * 20, amplifier - 1);
                  player.addPotionEffect(potionEffect);
                  playerStats.setStat("money", (Double)playerStats.getStat("money", Double.class) - cost);
                  sender.sendMessage("Purchased " + effectType.getName() + " level " + amplifier + " for " + duration + " seconds for " + Shared.formatNumber(cost) + " money.");
                  return true;
               }
            } else {
               sender.sendMessage("Potion effect not listed or nonexistent.");
               return false;
            }
         }
      } else {
         sender.sendMessage("This command can only be run by a player.");
         return false;
      }
   }
}
