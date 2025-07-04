package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerLevelHandler;
import org.gsdistance.grimmsServer.Data.Player.PlayerTitleChecker;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.ServerStats;
import org.gsdistance.grimmsServer.Stats.WorldStats;

public class EntityDeathEvent {
    public static void Event(org.bukkit.event.entity.EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            if (event.getEntity().getKiller().getType() == EntityType.PLAYER) {
                PlayerStats playerStats = PlayerStats.getPlayerStats(event.getEntity().getKiller());
                WorldStats worldStats = WorldStats.getWorldStats(event.getEntity().getWorld());

                playerStats.changeStat("total_kill_count", 1);
                playerStats.changeStat("money", (int) Math.round(3 * PlayerLevelHandler.getLevelHandler(event.getEntity().getKiller()).getLesserMoneyMultiplier()));
                playerStats.changeStat("tPoint", (int) Math.round(25 * PlayerLevelHandler.getLevelHandler(event.getEntity().getKiller()).getMoneyMultiplier()));
                worldStats.changeStat("wPoint", 40);
                PlayerLevelHandler.getLevelHandler(event.getEntity().getKiller()).addExp(50);

                if (event.getEntity().getType() == EntityType.PLAYER) {
                    playerStats.changeStat("money", (int) Math.round(12 * PlayerLevelHandler.getLevelHandler(event.getEntity().getKiller()).getLesserMoneyMultiplier()));
                    playerStats.changeStat("tPoint", (int) Math.round(50 * PlayerLevelHandler.getLevelHandler(event.getEntity().getKiller()).getMoneyMultiplier()));
                    worldStats.changeStat("wPoint", 110);
                    PlayerLevelHandler.getLevelHandler(event.getEntity().getKiller()).addExp(150);
                    PlayerTitleChecker.killedPlayer(event.getEntity().getKiller());
                    PlayerTitleChecker.gotKilledByPlayer((Player) event.getEntity());
                } else if (event.getEntity().getType() == EntityType.ENDER_DRAGON) {
                    PlayerTitleChecker.killedDragon(event.getEntity().getKiller());
                }
            }
        }
        if (event.getEntity().getType() == EntityType.PLAYER) {
            PlayerStats.getPlayerStats((Player) event.getEntity()).changeStat("death_count", 1);
        }
        WorldStats.getWorldStats(event.getEntity().getWorld()).changeStat("death_count", 1);
        ServerStats.getServerStats().changeStat("death_count", 1);
    }
}
