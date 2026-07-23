package org.gsdistance.grimmsServer.Stats;

import org.bukkit.entity.Player;

import java.util.function.Predicate;

public enum TitleCriteria {
    NEWBIE("Newbie", "Wowie! Joining the game gives a title!", player -> true),

    MURDERER("Murderer", "Someone whose crimes follow him till death.", player ->
        PlayerStats.getPlayerStats(player).getStat("total_kill_count", Integer.class) > 0),

    VICTIM("Victim", "Got murdered, unfortunate.", player ->
        PlayerStats.getPlayerStats(player).getStat("death_count", Integer.class) > 0),

    DRAGON_SLAYER("DragonSlayer", "The fabled warrior to defeat the fabled dragon or something like that.", player ->
        false), // Dragon kills not tracked in persistent stats

    LEADER("Leader", "You got on the leaderboard.", player ->
        false), // Leaderboard status not tracked in persistent stats

    TITLEMAXXER("Titlemaxxer", "Get more than 5 titles", player ->
        PlayerTitles.getPlayerTitles(player).getTitles().size() > 5);
    
    private final String titleId;
    private final String description;
    private final Predicate<Player> condition;
    
    TitleCriteria(String titleId, String description, Predicate<Player> condition) {
        this.titleId = titleId;
        this.description = description;
        this.condition = condition;
    }
    
    public String getTitleId() {
        return titleId;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean test(Player player) {
        return condition.test(player);
    }
    
    // Special titles that are manually awarded (not auto-checked)
    public static final String[] MANUAL_TITLES = {
        "Dictator", "Executioner", "BugSlayer", "ExploitDestroyer", "Disgrace."
    };
}
