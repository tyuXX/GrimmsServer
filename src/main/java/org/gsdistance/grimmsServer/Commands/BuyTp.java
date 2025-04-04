package org.gsdistance.grimmsServer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Request;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class BuyTp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                return false;
            }
            Player player = GrimmsServer.instance.getServer().getPlayer(args[0]);
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
