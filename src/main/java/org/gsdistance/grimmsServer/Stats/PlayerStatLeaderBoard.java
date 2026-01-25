package org.gsdistance.grimmsServer.Stats;

import com.google.gson.Gson;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Data.Player.PlayerTitleChecker;
import org.gsdistance.grimmsServer.Shared;

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
    public final Map<String, Data<String, Number>> leaderboard;

    public PlayerStatLeaderBoard() {
        leaderboard = new HashMap<>();
        for (String stat : Stats.keySet()) {
            leaderboard.put(stat, Data.of("None", 0));
        }
    }

    public static PlayerStatLeaderBoard getPlayerStatLeaderBoard() {
        PlayerStatLeaderBoard playerStatLeaderBoard = new Gson().fromJson((String) ServerStats.getServerStats().getStat("leaderboard"), PlayerStatLeaderBoard.class);
        for (String stat : Stats.keySet()) {
            if (!playerStatLeaderBoard.leaderboard.containsKey(stat)) {
                playerStatLeaderBoard.leaderboard.put(stat, Data.of("None", 0));
            }
        }
        return playerStatLeaderBoard;
    }

    public void savePlayerStatLeaderBoard() {
        ServerStats.getServerStats().setStat("leaderboard", new Gson().toJson(this));
    }

    public void checkPlayer(Player player) {
        PlayerStats playerStats = PlayerStats.getPlayerStats(player);
        boolean pass = false;
        List<String> overtakes = new ArrayList<>();
        for (String stat : leaderboard.keySet()) {
            Number playerStatValue = playerStats.getStat(stat, Number.class);
            Number leaderboardStatValue = leaderboard.get(stat).value();
            if (playerStatValue != null && playerStatValue.doubleValue() > leaderboardStatValue.doubleValue()) {
                if (!player.getName().equalsIgnoreCase(leaderboard.get(stat).key())) {
                    overtakes.add(stat);
                }
                leaderboard.put(stat, Data.of(player.getName(), playerStatValue.intValue()));
                savePlayerStatLeaderBoard();
                pass = true;
            }
        }
        if (pass) {
            for (String stat : overtakes) {
                Shared.Broadcast("The leader of stat " + PlayerStats.StatNames.get(stat) + " is now " + player.getDisplayName(), null);
            }
            PlayerTitleChecker.gotOnLeaderboard(player);
        }
    }
}
