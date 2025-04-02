package org.gsdistance.grimmsServer.Stats;

import com.google.gson.Gson;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.LeaderboardEntry;
import org.gsdistance.grimmsServer.Data.PlayerTitleManager;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
            "level", Integer.class
    );
    public Map<String, LeaderboardEntry> leaderboard;

    public PlayerStatLeaderBoard() {
        this.leaderboard = Map.of(
                "total_kill_count", new LeaderboardEntry("None", 0),
                "death_count", new LeaderboardEntry("None", 0),
                "join_count", new LeaderboardEntry("None", 0),
                "tPoint", new LeaderboardEntry("None", 0),
                "block_break_count", new LeaderboardEntry("None", 0),
                "money", new LeaderboardEntry("None", 0),
                "level", new LeaderboardEntry("None", 0)
        );
    }

    public static PlayerStatLeaderBoard getPlayerStatLeaderBoard() {
        return new Gson().fromJson((String) ServerStats.getServerStats().getStat("leaderboard"), PlayerStatLeaderBoard.class);
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
                if(!player.getName().equalsIgnoreCase(leaderboard.get(stat).getPlayerName())){
                    overtakes.add(stat);
                }
                leaderboard.put(stat, new LeaderboardEntry(player.getName(), playerStatValue.intValue()));
                savePlayerStatLeaderBoard();
                pass = true;
            }
        }
        if(pass){
            for (String stat : overtakes){
                GrimmsServer.instance.getServer().broadcastMessage("The leader of stat " + stat + " is now " + player.getDisplayName());
            }
            PlayerTitleManager.gotOnLeaderboard(player);
        }
        return pass;
    }
}
