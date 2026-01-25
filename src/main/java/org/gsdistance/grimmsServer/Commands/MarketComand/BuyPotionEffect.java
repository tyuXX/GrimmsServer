package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.gsdistance.grimmsServer.Data.Market.PotionEffectBaseValues;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class BuyPotionEffect {
    public static boolean SubCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length < 4) {
                sender.sendMessage("Usage: /market buyPotionEffect <effect> <duration> <level>");
                return false;
            }

            PotionEffectType effectType = PotionEffectType.getByName(args[1].toUpperCase());
            if (effectType == null || !PotionEffectBaseValues.potionEffectBaseValues.containsKey(effectType)) {
                sender.sendMessage("Potion effect not listed or nonexistent.");
                return false;
            }

            int duration;
            int amplifier;

            try {
                duration = Integer.parseInt(args[2]);
                if (duration <= 0) {
                    sender.sendMessage("Duration must be a positive number.");
                    return false;
                }
            } catch (NumberFormatException e) {
                sender.sendMessage("Invalid duration. Must be a positive number.");
                return false;
            }

            try {
                amplifier = Integer.parseInt(args[3]);
                if (amplifier < 1) {
                    sender.sendMessage("Level must be at least 1.");
                    return false;
                }
            } catch (NumberFormatException e) {
                sender.sendMessage("Invalid level. Must be a positive number.");
                return false;
            }

            // Check if the effect has an amplitude limit and if the requested level exceeds it
            if (PotionEffectBaseValues.potionEffectAmplitudeLimits.containsKey(effectType)) {
                int maxAmplifier = PotionEffectBaseValues.potionEffectAmplitudeLimits.get(effectType);
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
            }

            if (playerStats.getStat("money", Double.class) < cost) {
                sender.sendMessage("Not enough money. Cost: " + Shared.formatNumber(cost) + " money.");
                return false;
            }

            // Apply the potion effect
            PotionEffect potionEffect = new PotionEffect(effectType, duration * 20, amplifier - 1); // Convert seconds to ticks, amplifier is 0-based
            player.addPotionEffect(potionEffect);

            // Deduct cost
            playerStats.setStat("money", playerStats.getStat("money", Double.class) - cost);

            sender.sendMessage("Purchased " + effectType.getName() + " level " + amplifier + " for " + duration + " seconds for " + Shared.formatNumber(cost) + " money.");
            return true;

        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}
