package org.gsdistance.grimmsServer.Events;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Commands.CommandRegistry;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerStatLeaderBoard;

import java.util.logging.Level;

public class ServerStartupEvent {
    public static int ticks = 0;

    public static void Event() {
        CommandRegistry.registerCommands();
        GrimmsServer.instance.getServer().getScheduler().scheduleSyncRepeatingTask(GrimmsServer.instance, () -> {
            // none
            ticks++;
            if (ticks % 1000 == 0) {
                for (Player player : GrimmsServer.instance.getServer().getOnlinePlayers()) {
                    PlayerStatLeaderBoard.getPlayerStatLeaderBoard().checkPlayer(player);
                }
            }
        }, 100L, 1L);
        GrimmsServer.logger.log(Level.INFO, "GrimmsServer has started successfully.");
    }
}
