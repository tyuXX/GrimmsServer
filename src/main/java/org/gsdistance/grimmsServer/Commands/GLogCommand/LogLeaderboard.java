package org.gsdistance.grimmsServer.Commands.GLogCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Stats.PlayerStatLeaderBoard;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class LogLeaderboard {
    public LogLeaderboard() {
    }

    public static boolean subCommand(Player player) {
        PlayerStatLeaderBoard leaderBoard = PlayerStatLeaderBoard.getPlayerStatLeaderBoard();
        leaderBoard.checkPlayer(player);
        player.sendMessage("__Leaderboard:");

        for (String stat : leaderBoard.leaderboard.keySet()) {
            String var10001 = PlayerStats.StatNames.get(stat);
            player.sendMessage("|Leader of stat " + var10001 + " is " + ((Data) leaderBoard.leaderboard.get(stat)).key() + " with " + ((Data) leaderBoard.leaderboard.get(stat)).value());
        }

        return true;
    }
}
