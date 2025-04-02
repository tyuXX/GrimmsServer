package org.gsdistance.grimmsServer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class SendMoney implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length < 2) {
                sender.sendMessage("Usage: /sendmoney <player> <amount>");
                return false;
            }
            if (GrimmsServer.instance.getServer().getPlayer(args[0]) == null) {
                sender.sendMessage("Player not found.");
                return false;
            }
            if(Double.parseDouble(args[1]) < 0) {
                sender.sendMessage("You cannot send negative money.");
                return false;
            }
            PlayerStats sending = PlayerStats.getPlayerStats((Player) sender);
            if ((Double) sending.getStat("money") < Double.parseDouble(args[1])) {
                sender.sendMessage("You do not have enough money.");
                return false;
            }
            PlayerStats receiving = PlayerStats.getPlayerStats(GrimmsServer.instance.getServer().getPlayer(args[0]));
            sending.changeStat("money", -Integer.parseInt(args[1]));
            receiving.changeStat("money", Integer.parseInt(args[1]));
            sender.sendMessage("You have sent " + args[1] + " to " + args[0]);
            GrimmsServer.instance.getServer().getPlayer(args[0]).sendMessage("You have received " + args[1] + " from " + sender.getName());
            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}
