package org.gsdistance.grimmsServer.Stats;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;

public class LevelHandler {
    private final Player player;
    private final PlayerStats playerStats;
    public LevelHandler(Player player) {
        this.playerStats = PlayerStats.getPlayerStats(player);
        this.player = player;
    }
    public static LevelHandler getLevelHandler(Player player) {
        return new LevelHandler(player);
    }
    public int getLevel() {
        return (int) playerStats.getStat("level");
    }
    public double getXp() {
        return (Double) playerStats.getStat("xp");
    }
    public double getXpToLevel() {
        return Math.pow(getLevel(),1.7) * 100;
    }
    public int addExp(double xp){
        double exp = xp + getXp();
        int lvlups = 0;
        double tMoney = 0;
        while(exp > getXpToLevel()){
            exp -= getXpToLevel();
            playerStats.changeStat("level",1);
            tMoney += Math.pow(getLevel(),1.5) * 10;
            lvlups++;
        }
        playerStats.setStat("xp",exp);
        playerStats.setStat("xp_required",getXpToLevel());
        if(lvlups>0){
            playerStats.setStat("money", (Double) playerStats.getStat("money") + tMoney);
            GrimmsServer.instance.getServer().broadcastMessage("[" + player.getDisplayName() + "] " + "Has leveled up to " + getLevel() + "!");
            player.sendMessage("By leveling up " + lvlups + " times, you have gained " + Math.round(tMoney) + " money!");
        }
        return lvlups;
    }
}
