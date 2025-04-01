package org.gsdistance.grimmsServer.Commands;

import org.gsdistance.grimmsServer.GrimmsServer;

public class CommandRegistry {
    public static void registerCommands() {
        // Register all commands here
        GrimmsServer.instance.getCommand("logselfstats").setExecutor(new LogSelfStatsCommand());
        GrimmsServer.instance.getCommand("logworldstats").setExecutor(new LogWorldStats());
        GrimmsServer.instance.getCommand("sellItem").setExecutor(new SellItem());
        GrimmsServer.instance.getCommand("buyItem").setExecutor(new BuyItem());
        GrimmsServer.instance.getCommand("getMarketStock").setExecutor(new GetStockOfItem());
        GrimmsServer.instance.getCommand("getMarket").setExecutor(new LogEntireMarket());
        GrimmsServer.instance.getCommand("logLeaderboard").setExecutor(new LogLeaderBoard());
    }
}
