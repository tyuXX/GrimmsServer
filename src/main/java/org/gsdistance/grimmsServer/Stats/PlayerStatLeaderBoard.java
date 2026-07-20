package org.gsdistance.grimmsServer.Stats;

import com.google.gson.Gson;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Data.Player.PlayerTitleChecker;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Shared;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerStatLeaderBoard {
    public static final Map<String, Type> Stats = Map.of("total_kill_count", Integer.class, "death_count", Integer.class, "join_count", Integer.class, "tPoint", Double.class, "block_break_count", Long.class, "money", Double.class, "level", Integer.class, "sent_messages", Long.class, "prestige", Integer.class, "prestigePoints", Long.class);
    public final Map<String, List<Data<String, Number>>> leaderboard = new HashMap<>();

    public PlayerStatLeaderBoard() {
        for (String stat : Stats.keySet()) {
            this.leaderboard.put(stat, new ArrayList<>());
        }

    }

    public static PlayerStatLeaderBoard getPlayerStatLeaderBoard() {
        PlayerStatLeaderBoard playerStatLeaderBoard;
        try {
            playerStatLeaderBoard = GrimmsServer.pds.retrieveData("leaderboard.json", "", PlayerStatLeaderBoard.class);
        } catch (Exception e) {
            // If JSON parsing fails (e.g., corrupted data or wrong structure), create a new empty leaderboard
            playerStatLeaderBoard = new PlayerStatLeaderBoard();
            playerStatLeaderBoard.savePlayerStatLeaderBoard();
        }

        if (playerStatLeaderBoard == null) {
            playerStatLeaderBoard = new PlayerStatLeaderBoard();
        }

        for (String stat : Stats.keySet()) {
            if (!playerStatLeaderBoard.leaderboard.containsKey(stat)) {
                playerStatLeaderBoard.leaderboard.put(stat, new ArrayList<>());
            }
        }

        return playerStatLeaderBoard;
    }

    public void savePlayerStatLeaderBoard() {
        GrimmsServer.pds.saveData(this, PlayerStatLeaderBoard.class, "leaderboard.json", "");
    }

    public void checkPlayer(Player player) {
        PlayerStats playerStats = PlayerStats.getPlayerStats(player);
        boolean pass = false;
        List<String> overtakes = new ArrayList();

        for (String stat : this.leaderboard.keySet()) {
            Number playerStatValue = playerStats.getStat(stat, Number.class);
            List<Data<String, Number>> statLeaders = this.leaderboard.get(stat);

            if (playerStatValue != null) {
                // Check if player was already #1 before update
                boolean wasAlreadyLeader = !statLeaders.isEmpty() && statLeaders.get(0).key().equalsIgnoreCase(player.getName());

                // Check if player should be in top 3
                boolean shouldAdd = true;
                if (statLeaders.size() >= 3) {
                    Number lowestValue = statLeaders.get(statLeaders.size() - 1).value();
                    if (playerStatValue.doubleValue() <= lowestValue.doubleValue()) {
                        shouldAdd = false;
                    }
                }

                if (shouldAdd) {
                    // Remove existing entry for this player if present
                    statLeaders.removeIf(entry -> entry.key().equalsIgnoreCase(player.getName()));

                    // Add new entry
                    statLeaders.add(Data.of(player.getName(), playerStatValue));

                    // Sort by value descending
                    statLeaders.sort((a, b) -> Double.compare(b.value().doubleValue(), a.value().doubleValue()));

                    // Keep only top 3
                    while (statLeaders.size() > 3) {
                        statLeaders.remove(statLeaders.size() - 1);
                    }

                    // Check if player is now #1 (for backwards compatibility)
                    if (!statLeaders.isEmpty() && statLeaders.get(0).key().equalsIgnoreCase(player.getName())) {
                        pass = true;
                        // Only broadcast if player wasn't already the leader
                        if (!wasAlreadyLeader) {
                            overtakes.add(stat);
                        }
                    }
                }
            }
        }

        if (pass) {
            this.savePlayerStatLeaderBoard();
            for (String stat : overtakes) {
                Shared.Broadcast("The leader of stat " + PlayerStats.StatNames.get(stat) + " is now " + player.getDisplayName(), null);
            }

            PlayerTitleChecker.gotOnLeaderboard(player);
        }

    }
}
