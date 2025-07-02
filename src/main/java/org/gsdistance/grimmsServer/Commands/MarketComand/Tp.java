package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Request;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class Tp {
    public static boolean SubCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                return false;
            }
            Player player = GrimmsServer.instance.getServer().getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage("Player not found.");
                return false;
            }
            PlayerStats playerStats = PlayerStats.getPlayerStats((Player) sender);
            if ((Double) playerStats.getStat("money") < PerSessionDataStorage.tpCost) {
                sender.sendMessage("Not enough money.");
                return false;
            }
            Request.newRequest((Object object) -> {
                Player targetPlayer = (Player) object;
                targetPlayer.teleport(player.getLocation());
                targetPlayer.sendMessage("You have teleported to " + player.getDisplayName() + ".");
                return null;
            }, player, ((Player) sender).getDisplayName() + " wants to teleport to you.", sender);
            sender.sendMessage("Bought teleportation to " + player.getDisplayName() + " for " + PerSessionDataStorage.tpCost + " money.");
            PerSessionDataStorage.tpCost += 500.0;
            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}
