package org.gsdistance.grimmsServer.Stats;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;

public class LevelHandler {
    private final String playerName;
    private final PlayerStats playerStats;
    public LevelHandler(Player player) {
        this.playerStats = PlayerStats.getPlayerStats(player);
        this.playerName = player.getName();
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
        while(exp > getXpToLevel()){
            exp -= getXpToLevel();
            playerStats.changeStat("level",1);
            playerStats.changeStat("money", (int) Math.min(2147483645,Math.pow(getLevel(),1.5) * 10));
            lvlups++;
        }
        playerStats.setStat("xp",exp);
        playerStats.setStat("xp_required",getXpToLevel());
        if(lvlups>0){
            GrimmsServer.instance.getServer().broadcastMessage("[" + playerName + "] " + "Has leveled up to " + getLevel() + "!");
        }
        return lvlups;
    }
}
