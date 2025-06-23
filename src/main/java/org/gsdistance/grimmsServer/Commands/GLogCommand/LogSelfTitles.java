package org.gsdistance.grimmsServer.Commands.GLogCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Stats.PlayerTitles;

public class LogSelfTitles {
    public static boolean subCommand(Player player){
        if(!player.hasPermission("grimmsserver.log.self")) {
            return false;
        }
        PlayerTitles playerTitles = PlayerTitles.getPlayerTitles(player);
        player.sendMessage("__Your Titles:");
        for (String title : playerTitles.getTitles()) {
            player.sendMessage("|" + title + ": " + PlayerTitles.titles.get(title));
        }
        return true;
    }
}
