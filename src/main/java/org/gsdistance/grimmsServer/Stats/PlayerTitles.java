package org.gsdistance.grimmsServer.Stats;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.Player.PlayerTitleChecker;
import org.gsdistance.grimmsServer.Shared;

import java.util.Map;
import java.util.UUID;

public class PlayerTitles {
    public static final Map<String, String> titles = Map.ofEntries(Map.entry("Dictator", "The title of a dictator, not to be messed with."), Map.entry("Executioner", "The title and power of execution."), Map.entry("Murderer", "Someone whose crimes follow him till death."), Map.entry("Victim", "Got murdered, unfortunate."), Map.entry("DragonSlayer", "The fabled warrior to defeat the fabled dragon or something like that."), Map.entry("Newbie", "Wowie! Joining the game gives a title!"), Map.entry("Richie", "Money talks, well at least that's what I've been told!"), Map.entry("Millionaire", "Wow, that's quite a lot ya' know!"), Map.entry("Billionaire", "Capitalism at it's finest."), Map.entry("Trillionaire", "Literally insane, no comment."), Map.entry("Quadrillionaire", "Why?"), Map.entry("Miner", "Minin' to the earths core."), Map.entry("Anti-block", "DEMOLISH 10000 blocks"), Map.entry("UltimateMiner", "Mine 100000 blocks"), Map.entry("Titlemaxxer", "Get more than 5 titles"), Map.entry("KillingMachine", "Kill 100 things(?)."), Map.entry("ProGamer", "Kill 1000 things(?)."), Map.entry("Hitman", "Professional killerguy."), Map.entry("Leader", "You got on the leaderboard."), Map.entry("Null", "Not a title bro."), Map.entry("BugSlayer", "Reported a bug."), Map.entry("ExploitDestroyer", "Reported an exploit."), Map.entry("Disgrace.", "Abused an exploit."));
    private final Player player;

    public PlayerTitles(Player player) {
        this.player = player;
    }

    public static PlayerTitles getPlayerTitles(Player player) {
        return new PlayerTitles(player);
    }

    public static PlayerTitles getOfflinePlayerTitles(UUID uuid) {
        PlayerMetadata metadata = PlayerMetadata.getOfflinePlayerMetadata(uuid);
        if (metadata == null) {
            return null;
        }
        return new OfflinePlayerTitles(metadata);
    }

    public String[] getTitles() {
        return PlayerMetadata.getPlayerMetadata(this.player).titles;
    }

    public void addTitle(String title) {
        PlayerMetadata metadata = PlayerMetadata.getPlayerMetadata(this.player);
        if (!this.hasTitle(title)) {
            String[] newTitles = new String[metadata.titles.length + 1];
            System.arraycopy(metadata.titles, 0, newTitles, 0, metadata.titles.length);
            newTitles[metadata.titles.length] = title;
            metadata.titles = newTitles;
            Shared.Broadcast("Title " + title + " was bestowed upon " + this.player.getDisplayName() + ".", null);
            PlayerTitleChecker.checkTitles(this.player);
            metadata.saveToPDS();
        }

    }

    public void removeTitle(String title) {
        PlayerMetadata metadata = PlayerMetadata.getPlayerMetadata(this.player);
        if (this.hasTitle(title)) {
            String[] newTitles = new String[metadata.titles.length - 1];
            int index = 0;

            for (String playerTitle : metadata.titles) {
                if (!playerTitle.equals(title)) {
                    newTitles[index++] = playerTitle;
                }
            }

            metadata.titles = newTitles;
            Shared.Broadcast("Title " + title + " was revoked from " + this.player.getDisplayName() + ".", null);
            PlayerTitleChecker.checkTitles(this.player);
            metadata.saveToPDS();
        }

    }

    public boolean hasTitle(String title) {
        PlayerMetadata metadata = PlayerMetadata.getPlayerMetadata(this.player);

        for (String playerTitle : metadata.titles) {
            if (playerTitle.equals(title)) {
                return true;
            }
        }

        return false;
    }

    private static class OfflinePlayerTitles extends PlayerTitles {
        private final PlayerMetadata metadata;

        public OfflinePlayerTitles(PlayerMetadata metadata) {
            super(null);
            this.metadata = metadata;
        }

        @Override
        public String[] getTitles() {
            return this.metadata.titles;
        }

        @Override
        public boolean hasTitle(String title) {
            for (String playerTitle : this.metadata.titles) {
                if (playerTitle.equals(title)) {
                    return true;
                }
            }
            return false;
        }
    }
}
