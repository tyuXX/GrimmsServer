package org.gsdistance.grimmsServer.Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Data.MarketBaseValues;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class BuyRipoffItem implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length < 2) {
                return false;
            }
            if (Material.matchMaterial(args[0]) == null) {
                sender.sendMessage("Invalid item id:" + args[0]);
                return false;
            }
            if (!MarketBaseValues.marketBaseValues.containsKey(Material.matchMaterial(args[0]))) {
                sender.sendMessage("This item is not available in the market.");
                return false;
            }
            if(MarketBaseValues.marketBaseValues.get(Material.matchMaterial(args[0])) > 10000.0){
                sender.sendMessage("You cannot buy the ripoff of something which has a base value above 10000");
                return false;
            }
            if(Integer.parseInt(args[1]) < 1){
                sender.sendMessage("Can buy a minimum of 1.");
                return false;
            }
            PlayerStats playerStats = PlayerStats.getPlayerStats((Player) sender);
            double bought = 0;
            for (int i = 0; i < Integer.parseInt(args[1]); i++) {
                if ((Double) playerStats.getStat("money") > MarketBaseValues.marketBaseValues.get(Material.matchMaterial(args[0])) * 10) {
                    playerStats.setStat("money", (Double) playerStats.getStat("money") - MarketBaseValues.marketBaseValues.get(Material.matchMaterial(args[0])) * 10);
                    bought++;
                } else {
                    break;
                }
            }
            ((Player) sender).getInventory().addItem(new ItemStack(Material.matchMaterial(args[0]),Integer.parseInt(args[1])));
            sender.sendMessage("You bought " + bought + " many of " + args[0] + " for " + MarketBaseValues.marketBaseValues.get(Material.matchMaterial(args[0])) * 10 * bought + " money.");
            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}
