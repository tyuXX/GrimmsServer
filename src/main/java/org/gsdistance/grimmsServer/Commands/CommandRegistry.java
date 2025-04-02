package org.gsdistance.grimmsServer.Commands;

import org.gsdistance.grimmsServer.GrimmsServer;

public class CommandRegistry {
    public static void registerCommands() {
        // Register all commands here
        GrimmsServer.instance.getCommand("logselfstats").setExecutor(new LogSelfStats());
        GrimmsServer.instance.getCommand("logworldstats").setExecutor(new LogWorldStats());
        GrimmsServer.instance.getCommand("sellItem").setExecutor(new SellItem());
        GrimmsServer.instance.getCommand("buyItem").setExecutor(new BuyItem());
        GrimmsServer.instance.getCommand("getMarketStock").setExecutor(new GetStockOfItem());
        GrimmsServer.instance.getCommand("getMarket").setExecutor(new LogEntireMarket());
        GrimmsServer.instance.getCommand("logLeaderboard").setExecutor(new LogLeaderBoard());
        GrimmsServer.instance.getCommand("sendMoney").setExecutor(new SendMoney());
        GrimmsServer.instance.getCommand("buyRipoff").setExecutor(new BuyRipoffItem());
        GrimmsServer.instance.getCommand("makeItemLevelable").setExecutor(new MakeItemLevelable());
        GrimmsServer.instance.getCommand("setPlayerStat").setExecutor(new SetPlayerStat());
        GrimmsServer.instance.getCommand("logPlayerStats").setExecutor(new LogPlayerStats());
    }
}
