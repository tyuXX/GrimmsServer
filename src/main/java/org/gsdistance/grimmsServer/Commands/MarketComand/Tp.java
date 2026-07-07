package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Request;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class Tp {
    public Tp() {
    }

    public static boolean SubCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                return false;
            } else {
                Player player = GrimmsServer.instance.getServer().getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage("Player not found.");
                    return false;
                } else {
                    PlayerStats playerStats = PlayerStats.getPlayerStats((Player) sender);
                    if (playerStats.getStat("money", Double.class) < PerSessionDataStorage.tpCost) {
                        sender.sendMessage("Not enough money.");
                        return false;
                    } else {
                        Request.newRequest((object) -> {
                            Player targetPlayer = (Player) object;
                            targetPlayer.teleport(player.getLocation());
                            targetPlayer.sendMessage("You have teleported to " + player.getDisplayName() + ".");
                            return null;
                        }, player, ((Player) sender).getDisplayName() + " wants to teleport to you.", sender);
                        String var10001 = player.getDisplayName();
                        sender.sendMessage("Bought teleportation to " + var10001 + " for " + PerSessionDataStorage.tpCost + " money.");
                        PerSessionDataStorage.tpCost = PerSessionDataStorage.tpCost + (double) 500.0F;
                        return true;
                    }
                }
            }
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}
