package org.gsdistance.grimmsServer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerTitles;
import org.jetbrains.annotations.NotNull;

public class RemoveTitle implements CommandExecutor {
    public RemoveTitle() {
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            return false;
        } else if (!PlayerTitles.titles.containsKey(args[1])) {
            sender.sendMessage("Title not found.");
            return false;
        } else {
            Player player = GrimmsServer.instance.getServer().getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage("Player not found.");
                return false;
            } else {
                PlayerTitles playerTitles = PlayerTitles.getPlayerTitles(player);
                if (!playerTitles.hasTitle(args[1])) {
                    sender.sendMessage("Player doesn't have this title.");
                    return false;
                } else {
                    playerTitles.removeTitle(args[1]);
                    sender.sendMessage("Title " + args[1] + " revoked from " + player.getDisplayName() + ".");
                    return true;
                }
            }
        }
    }
}
