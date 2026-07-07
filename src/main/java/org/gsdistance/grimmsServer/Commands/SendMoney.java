package org.gsdistance.grimmsServer.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class SendMoney implements CommandExecutor {
    public SendMoney() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /sendMoney <player> <amount>");
                return false;
            }
            
            Player target = GrimmsServer.instance.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player '" + args[0] + "' not found or not online.");
                return false;
            }
            
            if (target.getName().equals(sender.getName())) {
                sender.sendMessage(ChatColor.RED + "You cannot send money to yourself.");
                return false;
            }
            
            double amount;
            try {
                amount = Double.parseDouble(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid amount: '" + args[1] + "'. Must be a number.");
                return false;
            }
            
            if (amount <= 0) {
                sender.sendMessage(ChatColor.RED + "You cannot send zero or negative money.");
                return false;
            }
            
            PlayerStats sending = PlayerStats.getPlayerStats((Player) sender);
            double currentBalance = sending.getStat("money", Double.class);
            
            if (currentBalance < amount) {
                sender.sendMessage(ChatColor.RED + "You do not have enough money. Current balance: " + ChatColor.GOLD + Shared.formatNumber(currentBalance));
                return false;
            }
            
            PlayerStats receiving = PlayerStats.getPlayerStats(target);
            sending.changeStat("money", -amount);
            receiving.changeStat("money", amount);
            
            sender.sendMessage(ChatColor.GREEN + "You have sent " + ChatColor.GOLD + Shared.formatNumber(amount) + ChatColor.GREEN + " to " + ChatColor.YELLOW + target.getName());
            target.sendMessage(ChatColor.GREEN + "You have received " + ChatColor.GOLD + Shared.formatNumber(amount) + ChatColor.GREEN + " from " + ChatColor.YELLOW + sender.getName());
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
            return false;
        }
    }
}
