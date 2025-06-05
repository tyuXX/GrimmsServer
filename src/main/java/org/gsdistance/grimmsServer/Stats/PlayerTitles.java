package org.gsdistance.grimmsServer.Stats;

import com.google.gson.Gson;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Data.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.PlayerTitleManager;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.Map;

public class PlayerTitles {
    public static final Map<String, String> titles = Map.ofEntries(
            Map.entry("Dictator", "The title of a dictator, not to be messed with."),
            Map.entry("Executioner", "The title and power of execution."),
            Map.entry("Murderer", "Someone whose crimes follow him till death."),
            Map.entry("Victim", "Got murdered, unfortunate."),
            Map.entry("DragonSlayer", "The fabled warrior to defeat the fabled dragon or something like that."),
            Map.entry("Newbie", "Wowie! Joining the game gives a title!"),
            Map.entry("Richie", "Money talks, well at least that's what I've been told!"),
            Map.entry("Millionaire", "Wow, that's quite a lot ya' know!"),
            Map.entry("Billionaire", "Capitalism at it's finest."),
            Map.entry("Trillionaire", "Literally insane, no comment."),
            Map.entry("Miner", "Minin' to the earths core."),
            Map.entry("Anti-block", "DEMOLISH 10000 blocks"),
            Map.entry("UltimateMiner", "Mine 100000 blocks"),
            Map.entry("Titlemaxxer", "Get more than 5 titles"),
            Map.entry("KillingMachine", "Kill 100 things(?)."),
            Map.entry("ProGamer", "Kill 1000 things(?)."),
            Map.entry("Hitman", "Professional killerguy."),
            Map.entry("Leader", "You got on the leaderboard."),
            Map.entry("Null", "Not a title bro.")
    );
    private final Player player;

    public PlayerTitles(Player player) {
        this.player = player;
    }

    public static PlayerTitles getPlayerTitles(Player player) {
        return new PlayerTitles(player);
    }

    public String[] getTitles() {
        return PlayerMetadata.getPlayerMetadata(player).titles;
    }

    public void addTitle(String title) {
        PlayerMetadata metadata = PlayerMetadata.getPlayerMetadata(player);
        if (!hasTitle(title)) {
            String[] newTitles = new String[metadata.titles.length + 1];
            System.arraycopy(metadata.titles, 0, newTitles, 0, metadata.titles.length);
            newTitles[metadata.titles.length] = title;
            metadata.titles = newTitles;
            GrimmsServer.instance.getServer().broadcastMessage("Title " + title + " was bestowed upon " + player.getDisplayName() + ".");
            PlayerTitleManager.checkTitles(player);
            metadata.saveToPDS();
        }
    }

    public void removeTitle(String title) {
        PlayerMetadata metadata = PlayerMetadata.getPlayerMetadata(player);
        if (hasTitle(title)) {
            String[] newTitles = new String[metadata.titles.length - 1];
            int index = 0;
            for (String playerTitle : metadata.titles) {
                if (!playerTitle.equals(title)) {
                    newTitles[index++] = playerTitle;
                }
            }
            metadata.titles = newTitles;
            GrimmsServer.instance.getServer().broadcastMessage("Title " + title + " was revoked from " + player.getDisplayName() + ".");
            PlayerTitleManager.checkTitles(player);
            metadata.saveToPDS();
        }
    }

    public boolean hasTitle(String title) {
        PlayerMetadata metadata = PlayerMetadata.getPlayerMetadata(player);
        for (String playerTitle : metadata.titles) {
            if (playerTitle.equals(title)) {
                return true;
            }
        }
        return false;
    }

}
