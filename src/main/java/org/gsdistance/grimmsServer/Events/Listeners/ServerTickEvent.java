package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.GrimmsServer;

public class ServerTickEvent {
    public static long ticks = 0L;

    public ServerTickEvent() {
    }

    public static void Event() {
        for (Player player : GrimmsServer.instance.getServer().getOnlinePlayers()) {
            PlayerTickEvent.Event(player);
        }

        PlayerTickEvent.processMagnets();

        // Execute champion mob abilities every 20 ticks (1 second) to reduce load
        if (ticks % 20L == 0L) {
            ChampionTickEvent.Event();
        }

        ++ticks;
        if (ticks % 1000L == 0L) {
            Market market = Market.getMarket();
            market.reCalcNegMarketSaturation();
            market.saveMarket();
        }
    }
}
