package org.gsdistance.grimmsServer.Stats;

import com.google.gson.Gson;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.LeaderboardEntry;
import org.gsdistance.grimmsServer.Data.PlayerTitleChecker;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerStatLeaderBoard {
    public static final Map<String, Type> Stats = Map.of(
            "total_kill_count", Integer.class,
            "death_count", Integer.class,
            "join_count", Integer.class,
            "tPoint", Double.class,
            "block_break_count", Long.class,
            "money", Double.class,
            "level", Integer.class,
            "sent_messages", Long.class
    );
    public Map<String, LeaderboardEntry> leaderboard;

    public PlayerStatLeaderBoard() {
        leaderboard = new HashMap<>();
        for (String stat : Stats.keySet()) {
            leaderboard.put(stat, new LeaderboardEntry("None", 0));
        }
    }

    public static PlayerStatLeaderBoard getPlayerStatLeaderBoard() {
        PlayerStatLeaderBoard playerStatLeaderBoard = new Gson().fromJson((String) ServerStats.getServerStats().getStat("leaderboard"), PlayerStatLeaderBoard.class);
        for (String stat : Stats.keySet()) {
            if (!playerStatLeaderBoard.leaderboard.containsKey(stat)) {
                playerStatLeaderBoard.leaderboard.put(stat, new LeaderboardEntry("None", 0));
            }
        }
        return playerStatLeaderBoard;
    }

    public void savePlayerStatLeaderBoard() {
        ServerStats.getServerStats().setStat("leaderboard", new Gson().toJson(this));
    }

    public boolean checkPlayer(Player player) {
        PlayerStats playerStats = PlayerStats.getPlayerStats(player);
        boolean pass = false;
        List<String> overtakes = new ArrayList<>();
        for (String stat : leaderboard.keySet()) {
            Number playerStatValue = (Number) playerStats.getStat(stat);
            Number leaderboardStatValue = leaderboard.get(stat).getStatValue();
            if (playerStatValue != null && playerStatValue.doubleValue() > leaderboardStatValue.doubleValue()) {
                if (!player.getName().equalsIgnoreCase(leaderboard.get(stat).getPlayerName())) {
                    overtakes.add(stat);
                }
                leaderboard.put(stat, new LeaderboardEntry(player.getName(), playerStatValue.intValue()));
                savePlayerStatLeaderBoard();
                pass = true;
            }
        }
        if (pass) {
            for (String stat : overtakes) {
                GrimmsServer.instance.getServer().broadcastMessage("The leader of stat " + PlayerStats.StatNames.get(stat) + " is now " + player.getDisplayName());
            }
            PlayerTitleChecker.gotOnLeaderboard(player);
        }
        return pass;
    }
}
