package org.gsdistance.grimmsServer.Commands.GLogCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Stats.PlayerStatLeaderBoard;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class LogLeaderboard {
    public static boolean subCommand(Player player) {
        PlayerStatLeaderBoard leaderBoard = PlayerStatLeaderBoard.getPlayerStatLeaderBoard();
        leaderBoard.checkPlayer(player);
        player.sendMessage("__Leaderboard:");
        for (String stat : leaderBoard.leaderboard.keySet()) {
            player.sendMessage("|Leader of stat " + PlayerStats.StatNames.get(stat) + " is " + leaderBoard.leaderboard.get(stat).key() + " with " + leaderBoard.leaderboard.get(stat).value());
        }
        return true;
    }
}
