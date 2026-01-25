package org.gsdistance.grimmsServer.Commands.GLogCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Manage.GeneralChatHandler;
import org.gsdistance.grimmsServer.Stats.PlayerTitles;

import java.util.ArrayList;

public class LogSelfTitles {
    public static boolean subCommand(Player player) {
        if (!player.hasPermission("grimmsserver.log.self")) {
            return false;
        }
        PlayerTitles playerTitles = PlayerTitles.getPlayerTitles(player);
        ArrayList<String> titlesList = new ArrayList<String>();
        titlesList.add("__Your Titles:");
        for (String title : playerTitles.getTitles()) {
            titlesList.add("|" + title + ": " + PlayerTitles.titles.get(title));
        }
        GeneralChatHandler.sendArray(player, titlesList.toArray(new String[0]));
        return true;
    }
}
