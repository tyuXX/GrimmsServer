package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerTitles;
import org.jetbrains.annotations.NotNull;

public class RemoveTitle {
    public RemoveTitle() {
    }

    public static boolean subCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length < 3) {
            return false;
        } else if (!PlayerTitles.titles.containsKey(args[2])) {
            sender.sendMessage("Title not found.");
            return false;
        } else {
            Player player = GrimmsServer.instance.getServer().getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage("Player not found.");
                return false;
            } else {
                PlayerTitles playerTitles = PlayerTitles.getPlayerTitles(player);
                if (!playerTitles.hasTitle(args[2])) {
                    sender.sendMessage("Player doesn't have this title.");
                    return false;
                } else {
                    playerTitles.removeTitle(args[2]);
                    sender.sendMessage("Title " + args[2] + " revoked from " + player.getDisplayName() + ".");
                    return true;
                }
            }
        }
    }
}
