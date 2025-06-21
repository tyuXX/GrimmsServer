package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Data.MarketBaseValues;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class BuyRipoff {
    public static boolean SubCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("grimmsserver.market.ripoff")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
        if (args.length < 3) {
            sender.sendMessage("Usage: /market ripoff <item> <amount>");
            return false;
        }
        Material material = Material.matchMaterial(args[1]);
        if (material == null || !MarketBaseValues.marketBaseValues.containsKey(material)) {
            sender.sendMessage("Invalid or unavailable item: " + args[1]);
            return false;
        }
        int amount = Integer.parseInt(args[2]);
        if (amount < 1) {
            sender.sendMessage("You must buy at least 1 item.");
            return false;
        }
        PlayerStats stats = PlayerStats.getPlayerStats(player);
        double price = MarketBaseValues.marketBaseValues.get(material) * 10;
        int bought = 0;
        for (int i = 0; i < amount; i++) {
            if ((Double) stats.getStat("money") >= price) {
                stats.setStat("money", (Double) stats.getStat("money") - price);
                bought++;
            } else {
                break;
            }
        }
        player.getInventory().addItem(new ItemStack(material, bought));
        sender.sendMessage("You bought " + bought + " of " + args[1] + " for " + (price * bought) + " money.");
        return true;
    }
}
