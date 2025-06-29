package org.gsdistance.grimmsServer.Commands.GLogCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerTitles;

public class LogPlayerTitles {
    public static boolean subCommand(Player player, String[] args) {
        if (!player.hasPermission("grimmsserver.log.other")) {
            return false;
        }
        if (args.length < 2) {
            return false;
        }
        Player tplayer = GrimmsServer.instance.getServer().getPlayer(args[1]);
        if (tplayer == null) {
            player.sendMessage("Player not found.");
            return false;
        }
        PlayerTitles playerTitles = PlayerTitles.getPlayerTitles(tplayer);
        player.sendMessage("__Your Titles:");
        for (String title : playerTitles.getTitles()) {
            player.sendMessage("|" + title + ": " + PlayerTitles.titles.get(title));
        }
        return true;
    }
}
