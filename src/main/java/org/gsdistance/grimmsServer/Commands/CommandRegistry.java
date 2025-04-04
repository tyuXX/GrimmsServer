package org.gsdistance.grimmsServer.Commands;

import org.gsdistance.grimmsServer.GrimmsServer;

public class CommandRegistry {
    public static void registerCommands() {
        // Register all commands here
        GrimmsServer.instance.getCommand("logSelfStats").setExecutor(new LogSelfStats());
        GrimmsServer.instance.getCommand("logWorldStats").setExecutor(new LogWorldStats());
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
        GrimmsServer.instance.getCommand("logSelfTitles").setExecutor(new LogSelfTitles());
        GrimmsServer.instance.getCommand("logPlayerTitles").setExecutor(new LogPlayerTitles());
        GrimmsServer.instance.getCommand("addTitle").setExecutor(new AddTitle());
        GrimmsServer.instance.getCommand("executePlayer").setExecutor(new ExecutePlayer());
        GrimmsServer.instance.getCommand("buyEnchantment").setExecutor(new BuyEnchantment());
        GrimmsServer.instance.getCommand("logEnchantmentCosts").setExecutor(new LogEnchantmentCosts());
        GrimmsServer.instance.getCommand("logJobs").setExecutor(new LogJobs());
        GrimmsServer.instance.getCommand("takeJob").setExecutor(new TakeJob());
        GrimmsServer.instance.getCommand("grimmsServerCommands").setExecutor(new LogGrimmsServerCommands());
        GrimmsServer.instance.getCommand("withdrawMoney").setExecutor(new WithdrawMoney());
        GrimmsServer.instance.getCommand("depositMoney").setExecutor(new DepositMoney());
        GrimmsServer.instance.getCommand("acceptRequest").setExecutor(new AcceptRequest());
        GrimmsServer.instance.getCommand("buyTp").setExecutor(new BuyTp());
        GrimmsServer.instance.getCommand("removeTitle").setExecutor(new RemoveTitle());
    }
}
