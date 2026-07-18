package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.Data.Market.MarketBaseValues;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class Ripoff {
    public Ripoff() {
    }

    public static boolean SubCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
            return false;
        }

        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /market ripoff <item> <amount>");
            return false;
        }

        Material material = Material.matchMaterial(args[1]);
        if (material == null) {
            sender.sendMessage(ChatColor.RED + "Invalid item: '" + args[1] + "'. Use /market stock to see available items.");
            return false;
        }

        if (!MarketBaseValues.marketBaseValues.containsKey(material)) {
            sender.sendMessage(ChatColor.RED + "Item '" + args[1] + "' is not available for ripoff purchase.");
            return false;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[2]);
            if (amount < 1) {
                sender.sendMessage(ChatColor.RED + "You must buy at least 1 item.");
                return false;
            }
        } catch (NumberFormatException var10) {
            sender.sendMessage(ChatColor.RED + "Invalid amount: '" + args[2] + "'. Must be a whole number.");
            return false;
        }

        PlayerStats stats = PlayerStats.getPlayerStats(player);
        double ripoffMultiplier = org.gsdistance.grimmsServer.Config.ActiveConfig.getConfigValue(org.gsdistance.grimmsServer.Config.ConfigKey.MARKET_RIPOFF_MULTIPLIER, Double.class);
        double price = MarketBaseValues.marketBaseValues.get(material) * ripoffMultiplier + Market.getMarket().NegMarketSaturation;
        int bought = 0;

        for (int i = 0; i < amount && stats.getStat("money", Double.class) >= price; ++i) {
            stats.setStat("money", stats.getStat("money", Double.class) - price);
            ++bought;
        }

        if (bought > 0) {
            player.getInventory().addItem(new ItemStack(material, bought));
            sender.sendMessage(ChatColor.GREEN + "You bought " + ChatColor.GOLD + bought + ChatColor.GREEN + " of " + ChatColor.YELLOW + args[1] + ChatColor.GREEN + " for " + ChatColor.GOLD + Shared.formatNumber(price * bought) + ChatColor.GREEN + " money.");
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Not enough money. Cost per item: " + ChatColor.GOLD + Shared.formatNumber(price));
            return false;
        }
    }
}
