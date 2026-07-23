package org.gsdistance.grimmsServer.Stats;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Shared;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PlayerTitles {
    // Static titles (manually awarded or event-based)
    private static final Map<String, String> staticTitles = Map.ofEntries(
        Map.entry("Dictator", "The title of a dictator, not to be messed with."),
        Map.entry("Executioner", "The title and power of execution."),
        Map.entry("Murderer", "Someone whose crimes follow him till death."),
        Map.entry("Victim", "Got murdered, unfortunate."),
        Map.entry("DragonSlayer", "The fabled warrior to defeat the fabled dragon or something like that."),
        Map.entry("Newbie", "Wowie! Joining the game gives a title!"),
        Map.entry("Titlemaxxer", "Get more than 5 titles"),
        Map.entry("Leader", "You got on the leaderboard."),
        Map.entry("Null", "Not a title bro."),
        Map.entry("BugSlayer", "Reported a bug."),
        Map.entry("ExploitDestroyer", "Reported an exploit."),
        Map.entry("Disgrace.", "Abused an exploit.")
    );
    
    // Dynamic titles (based on stats) - populated on-demand
    public static Map<String, String> titles = new HashMap<>();
    
    static {
        titles.putAll(staticTitles);
    }
    
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
    
    // Populate dynamic titles from TitleGenerator
    public static void populateDynamicTitles() {
        titles.putAll(TitleGenerator.getAllMoneyTitles());
        for (Map.Entry<Long, String> entry : TitleGenerator.getAllBlockBreakTitles().entrySet()) {
            titles.put(entry.getKey().toString(), entry.getValue());
        }
        for (Map.Entry<Long, String> entry : TitleGenerator.getAllKillTitles().entrySet()) {
            titles.put(entry.getKey().toString(), entry.getValue());
        }
        // Add descriptions for dynamic titles
        titles.putAll(TitleGenerator.getAllTitleDescriptions());
    }

    public Set<String> getTitles() {
        return PlayerMetadata.getPlayerMetadata(this.player).titles;
    }

    public void addTitle(String title) {
        PlayerMetadata metadata = PlayerMetadata.getPlayerMetadata(this.player);
        if (!this.hasTitle(title)) {
            metadata.titles.add(title);
            Shared.Broadcast("Title " + title + " was bestowed upon " + this.player.getDisplayName() + ".", null);
            metadata.saveToPDS();
        }
    }

    public void removeTitle(String title) {
        PlayerMetadata metadata = PlayerMetadata.getPlayerMetadata(this.player);
        if (this.hasTitle(title)) {
            metadata.titles.remove(title);
            Shared.Broadcast("Title " + title + " was revoked from " + this.player.getDisplayName() + ".", null);
            metadata.saveToPDS();
        }
    }

    public boolean hasTitle(String title) {
        PlayerMetadata metadata = PlayerMetadata.getPlayerMetadata(this.player);
        return metadata.titles.contains(title);
    }

    private static class OfflinePlayerTitles extends PlayerTitles {
        private final PlayerMetadata metadata;

        public OfflinePlayerTitles(PlayerMetadata metadata) {
            super(null);
            this.metadata = metadata;
        }

        @Override
        public Set<String> getTitles() {
            return this.metadata.titles;
        }

        @Override
        public boolean hasTitle(String title) {
            return this.metadata.titles.contains(title);
        }
    }
}
