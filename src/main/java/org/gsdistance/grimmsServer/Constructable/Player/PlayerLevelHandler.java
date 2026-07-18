package org.gsdistance.grimmsServer.Constructable.Player;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Events.Registers.PlayerLevelUpRegister;
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
        return this.playerStats.getStat("level", Integer.class);
    }

    public double getXp() {
        return this.playerStats.getStat("xp", Double.class);
    }

    public void setXp(double xp) {
        this.playerStats.setStat("xp", xp);
    }

    public double getXpToLevel() {
        return Math.pow(this.getLevel(), 1.7) * (double) 100.0F;
    }

    public double getMoneyMultiplier() {
        return Math.max(1.0F, Math.floor(Math.sqrt(this.getLevel())));
    }

    public double getLesserMoneyMultiplier() {
        return Math.max(1.0F, Math.floor(Math.sqrt((double) this.getLevel() / (double) 2.0F)));
    }

    public void addExp(double xp) {
        double exp = xp + this.getXp();
        int lvlups = 0;

        double tMoney;
        for (tMoney = 0.0F; exp > this.getXpToLevel(); ++lvlups) {
            exp -= this.getXpToLevel();
            this.playerStats.changeStat("level", 1);
            tMoney += Math.pow(this.getLevel(), 1.8) * (double) 100.0F * Math.pow(playerStats.getStat("prestige", Integer.class) + 1, 2);
        }

        this.setXp(exp);
        this.playerStats.setStat("xp_required", this.getXpToLevel());
        if (lvlups > 0) {
            this.playerStats.setStat("money", this.playerStats.getStat("money", Double.class) + tMoney);
            double prestige = Math.max(1, this.playerStats.getStat("prestige", Integer.class));
            double level = this.playerStats.getStat("level", Integer.class);
            this.playerStats.setStat("maximum_balance", Math.floor(Math.pow(level, 1.5) * Math.pow(prestige, 2) * 10000 * Math.log(level) / Math.log(7)));
            EventRegistry.callEvent(new PlayerLevelUpRegister(this.player, lvlups, tMoney));
        }
    }
}
