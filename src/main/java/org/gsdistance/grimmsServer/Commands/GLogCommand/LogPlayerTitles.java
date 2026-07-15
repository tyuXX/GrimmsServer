package org.gsdistance.grimmsServer.Commands.GLogCommand;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerTitles;

import java.util.UUID;

public class LogPlayerTitles {
    public LogPlayerTitles() {
    }

    public static boolean subCommand(Player player, String[] args) {
        if (!player.hasPermission("grimmsserver.log.other")) {
            return false;
        } else if (args.length < 2) {
            return false;
        } else {
            Player tplayer = GrimmsServer.instance.getServer().getPlayer(args[1]);
            PlayerTitles playerTitles;
            String displayName;

            if (tplayer != null) {
                playerTitles = PlayerTitles.getPlayerTitles(tplayer);
                displayName = tplayer.getDisplayName();
            } else {
                UUID uuid = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
                playerTitles = PlayerTitles.getOfflinePlayerTitles(uuid);
                if (playerTitles == null) {
                    player.sendMessage("Player not found.");
                    return false;
                }
                displayName = args[1];
            }

            player.sendMessage("__" + displayName + "'s Titles:");

            for (String title : playerTitles.getTitles()) {
                player.sendMessage("|" + title + ": " + PlayerTitles.titles.get(title));
            }

            return true;
        }
    }
}
