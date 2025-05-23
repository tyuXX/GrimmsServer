package org.gsdistance.grimmsServer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Stats.PlayerStatLeaderBoard;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class LogLeaderBoard implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PlayerStatLeaderBoard leaderBoard = PlayerStatLeaderBoard.getPlayerStatLeaderBoard();
        leaderBoard.checkPlayer((Player) sender);
        sender.sendMessage("__Leaderboard:");
        for (String stat : leaderBoard.leaderboard.keySet()) {
            sender.sendMessage("|Leader of stat " + PlayerStats.StatNames.get(stat) + " is " + leaderBoard.leaderboard.get(stat).getPlayerName() + " with " + leaderBoard.leaderboard.get(stat).getStatValue());
        }
        return true;
    }
}
