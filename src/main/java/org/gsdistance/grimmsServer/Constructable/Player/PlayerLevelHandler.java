package org.gsdistance.grimmsServer.Constructable.Player;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Events.Registers.PlayerLevelUpEvent;
import org.gsdistance.grimmsServer.Manage.EventRegistry;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class PlayerLevelHandler {
    private final Player player;
    private final PlayerStats playerStats;

    public PlayerLevelHandler(Player player) {
        this.playerStats = PlayerStats.getPlayerStats(player);
        this.player = player;
    }

    public static PlayerLevelHandler getLevelHandler(Player player) {
        return new PlayerLevelHandler(player);
    }

    public int getLevel() {
        return playerStats.getStat("level", Integer.class);
    }

    public double getXp() {
        return playerStats.getStat("xp", Double.class);
    }

    public void setXp(double xp) {
        playerStats.setStat("xp", xp);
    }

    public double getXpToLevel() {
        return Math.pow(getLevel(), 1.7) * 100;
    }

    public double getMoneyMultiplier() {
        return Math.max(1, Math.floor(Math.sqrt(getLevel())));
    }

    public double getLesserMoneyMultiplier() {
        return Math.max(1, Math.floor(Math.sqrt((double) getLevel() / 2)));
    }

    public void addExp(double xp) {
        double exp = xp + getXp();
        int lvlups = 0;
        double tMoney = 0;
        while (exp > getXpToLevel()) {
            exp -= getXpToLevel();
            playerStats.changeStat("level", 1);
            tMoney += Math.pow(getLevel(), 1.8) * 100;
            lvlups++;
        }
        setXp(exp);
        playerStats.setStat("xp_required", getXpToLevel());
        if (lvlups > 0) {
            playerStats.setStat("money", playerStats.getStat("money", Double.class) + tMoney);
            EventRegistry.callEvent(new PlayerLevelUpEvent(player, lvlups, tMoney));
        }
    }
}