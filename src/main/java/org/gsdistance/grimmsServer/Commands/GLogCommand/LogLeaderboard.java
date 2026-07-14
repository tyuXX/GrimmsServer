package org.gsdistance.grimmsServer.Commands.GLogCommand;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Stats.PlayerStatLeaderBoard;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import java.util.List;

public class LogLeaderboard {
    public LogLeaderboard() {
    }

    public static boolean subCommand(Player player) {
        PlayerStatLeaderBoard leaderBoard = PlayerStatLeaderBoard.getPlayerStatLeaderBoard();
        leaderBoard.checkPlayer(player);
        
        player.sendMessage(ChatColor.GOLD + "=" + ChatColor.YELLOW + "=".repeat(40) + ChatColor.GOLD + "=");
        player.sendMessage(ChatColor.GOLD + "=" + ChatColor.YELLOW + "=".repeat(15) + ChatColor.BOLD + " LEADERBOARD " + ChatColor.YELLOW + "=".repeat(16) + ChatColor.GOLD + "=");
        player.sendMessage(ChatColor.GOLD + "=" + ChatColor.YELLOW + "=".repeat(40) + ChatColor.GOLD + "=");
        player.sendMessage("");

        for (String stat : leaderBoard.leaderboard.keySet()) {
            String statName = PlayerStats.StatNames.get(stat);
            List<Data<String, Number>> statLeaders = leaderBoard.leaderboard.get(stat);
            
            player.sendMessage(ChatColor.GREEN + "[" + statName + "]");
            
            if (statLeaders.isEmpty()) {
                player.sendMessage(ChatColor.GRAY + "  No data yet");
            } else {
                for (int i = 0; i < statLeaders.size(); i++) {
                    Data<String, Number> entry = statLeaders.get(i);
                    String rankColor;
                    String rankSymbol;
                    
                    switch (i) {
                        case 0:
                            rankColor = ChatColor.GOLD.toString();
                            rankSymbol = "🥇";
                            break;
                        case 1:
                            rankColor = ChatColor.GRAY.toString();
                            rankSymbol = "🥈";
                            break;
                        case 2:
                            rankColor = ChatColor.RED.toString();
                            rankSymbol = "🥉";
                            break;
                        default:
                            rankColor = ChatColor.WHITE.toString();
                            rankSymbol = "  ";
                    }
                    
                    String formattedValue;
                    if (stat.equals("money") || stat.equals("tPoint") || stat.equals("block_break_count") || stat.equals("prestigePoints") || stat.equals("sent_messages")) {
                        formattedValue = Shared.formatNumber(entry.value().doubleValue());
                    } else {
                        formattedValue = entry.value().toString();
                    }
                    
                    player.sendMessage(rankColor + rankSymbol + " " + ChatColor.YELLOW + entry.key() + ChatColor.GRAY + " - " + ChatColor.AQUA + formattedValue);
                }
            }
            player.sendMessage("");
        }

        player.sendMessage(ChatColor.GOLD + "=" + ChatColor.YELLOW + "=".repeat(40) + ChatColor.GOLD + "=");

        return true;
    }
}
