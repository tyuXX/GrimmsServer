package org.gsdistance.grimmsServer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerTitles;

public class AddTitle implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length < 2) {
                sender.sendMessage(GrimmsServer.instance.getCommand("addTitle").getUsage());
                return false;
            }
            if (!PlayerTitles.titles.containsKey(args[1])) {
                sender.sendMessage("Title not found.");
                return false;
            }
            Player player = GrimmsServer.instance.getServer().getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage("Player not found.");
                return false;
            }
            PlayerTitles playerTitles = PlayerTitles.getPlayerTitles(player);
            if (playerTitles.hasTitle(args[1])) {
                sender.sendMessage("Player already has this title.");
                return false;
            }
            playerTitles.addTitle(args[1]);
            sender.sendMessage("Title " + args[1] + " added to " + player.getDisplayName() + ".");
            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}
