package org.gsdistance.grimmsServer.Stats;

import com.google.gson.Gson;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Data.PlayerTitleManager;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.Map;

public class PlayerTitles {
    private final String[] playerTitles;
    public static final Map<String, String> titles = Map.ofEntries(
            Map.entry("Dictator","The title of a dictator, not to be messed with."),
            Map.entry("Executioner","The title and power of execution."),
            Map.entry("Murderer","Someone whose crimes follow him till death."),
            Map.entry("Victim","Got murdered, unfortunate."),
            Map.entry("DragonSlayer","The fabled warrior to defeat the fabled dragon or something like that."),
            Map.entry("Newbie","Wowie! Joining the game gives a title!"),
            Map.entry("Millionaire","Wow, that's quite a lot ya' know!"),
            Map.entry("Billionaire","Capitalism at it's finest."),
            Map.entry("Trillionaire","Literally insane, no comment."),
            Map.entry("Titlemaxxer", "Get more than 5 titles"),
            Map.entry("Leader","You got on the leaderboard."),
            Map.entry("Null","Not a title bro.")
    );
    private final Player player;
    public PlayerTitles(Player player) {
        this.player = player;
        this.playerTitles = new Gson().fromJson((String) PlayerStats.getPlayerStats(player).getStat("titles"),String[].class);
    }
    public static PlayerTitles getPlayerTitles(Player player) {
        return new PlayerTitles(player);
    }
    public String[] getTitles() {
        return playerTitles;
    }
    public void addTitle(String title) {
        if(!hasTitle(title)) {
            String[] newTitles = new String[playerTitles.length + 1];
            System.arraycopy(playerTitles, 0, newTitles, 0, playerTitles.length);
            newTitles[playerTitles.length] = title;
            PlayerStats.getPlayerStats(player).setStat("titles", new Gson().toJson(newTitles));
            GrimmsServer.instance.getServer().broadcastMessage("Title " + title + " was bestowed upon " + player.getDisplayName() + ".");
            PlayerTitleManager.checkTitles(player);
        }
    }
    public boolean hasTitle(String title) {
        for (String playerTitle : playerTitles) {
            if (playerTitle.equals(title)) {
                return true;
            }
        }
        return false;
    }

}
