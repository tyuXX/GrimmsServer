package org.gsdistance.grimmsServer.Leveling;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;
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
        return (int) playerStats.getStat("level");
    }

    public double getXp() {
        Object xp = playerStats.getStat("xp");
        if (xp instanceof Integer) {
            return ((Integer) xp).doubleValue();
        } else if (xp instanceof Double) {
            return (Double) xp;
        } else {
            return 0.0;
        }
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

    public int addExp(double xp) {
        double exp = xp + getXp();
        int lvlups = 0;
        double tMoney = 0;
        while (exp > getXpToLevel()) {
            exp -= getXpToLevel();
            playerStats.changeStat("level", 1);
            tMoney += Math.pow(getLevel(), 1.5) * 100;
            lvlups++;
        }
        setXp(exp);
        playerStats.setStat("xp_required", getXpToLevel());
        if (lvlups > 0) {
            playerStats.setStat("money", (Double) playerStats.getStat("money") + tMoney);
            GrimmsServer.instance.getServer().broadcastMessage("[" + player.getDisplayName() + "] " + "Has leveled up to " + getLevel() + "!");
            player.sendMessage("By leveling up " + lvlups + " times, you have gained " + Math.round(tMoney) + " money!");
        }
        return lvlups;
    }
}