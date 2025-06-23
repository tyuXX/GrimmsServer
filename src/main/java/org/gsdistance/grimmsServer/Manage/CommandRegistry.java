package org.gsdistance.grimmsServer.Manage;

import org.gsdistance.grimmsServer.Commands.*;
import org.gsdistance.grimmsServer.Commands.HomeCommand.HomeBaseCommand;
import org.gsdistance.grimmsServer.Commands.HomeCommand.HomeTabCompleter;
import org.gsdistance.grimmsServer.Commands.MarketComand.MarketBaseCommand;
import org.gsdistance.grimmsServer.Commands.MarketComand.MarketTabCompleter;
import org.gsdistance.grimmsServer.Commands.RequestCommand.AcceptRequest;
import org.gsdistance.grimmsServer.Commands.RequestCommand.RequestTabCompleter;
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Data.ConfigRequirements;
import org.gsdistance.grimmsServer.GrimmsServer;

public class CommandRegistry {
    @SuppressWarnings("DataFlowIssue")
    public static void registerCommands() {
        // Register all commands here
        GrimmsServer.instance.getCommand("logSelfStats").setExecutor(new LogSelfStats());
        GrimmsServer.instance.getCommand("logWorldStats").setExecutor(new LogWorldStats());
        GrimmsServer.instance.getCommand("logLeaderboard").setExecutor(new LogLeaderBoard());
        GrimmsServer.instance.getCommand("sendMoney").setExecutor(new SendMoney());
        GrimmsServer.instance.getCommand("makeItemLevelable").setExecutor(new MakeItemLevelable());
        GrimmsServer.instance.getCommand("setPlayerStat").setExecutor(new SetPlayerStat());
        GrimmsServer.instance.getCommand("logPlayerStats").setExecutor(new LogPlayerStats());
        GrimmsServer.instance.getCommand("logSelfTitles").setExecutor(new LogSelfTitles());
        GrimmsServer.instance.getCommand("logPlayerTitles").setExecutor(new LogPlayerTitles());
        GrimmsServer.instance.getCommand("addTitle").setExecutor(new AddTitle());
        GrimmsServer.instance.getCommand("executePlayer").setExecutor(new ExecutePlayer());
        GrimmsServer.instance.getCommand("logEnchantmentCosts").setExecutor(new LogEnchantmentCosts());
        GrimmsServer.instance.getCommand("logJobs").setExecutor(new LogJobs());
        GrimmsServer.instance.getCommand("takeJob").setExecutor(new TakeJob());
        GrimmsServer.instance.getCommand("grimmsServerCommands").setExecutor(new LogGrimmsServerCommands());
        GrimmsServer.instance.getCommand("withdrawMoney").setExecutor(new WithdrawMoney());
        GrimmsServer.instance.getCommand("depositMoney").setExecutor(new DepositMoney());
        GrimmsServer.instance.getCommand("acceptRequest").setExecutor(new AcceptRequest());
        GrimmsServer.instance.getCommand("acceptRequest").setTabCompleter(new RequestTabCompleter());
        GrimmsServer.instance.getCommand("removeTitle").setExecutor(new RemoveTitle());
        GrimmsServer.instance.getCommand("reloadGrimmsConfig").setExecutor(new ReloadConfig());
        GrimmsServer.instance.getCommand("home").setExecutor(new HomeBaseCommand());
        GrimmsServer.instance.getCommand("home").setTabCompleter(new HomeTabCompleter());
        GrimmsServer.instance.getCommand("nick").setExecutor(new Nick());
        GrimmsServer.instance.getCommand("market").setExecutor(new MarketBaseCommand());
        GrimmsServer.instance.getCommand("market").setTabCompleter(new MarketTabCompleter());
        //GrimmsServer.instance.getCommand("gUtil").setExecutor(new GUtilBaseCommand());
        //GrimmsServer.instance.getCommand("gUtil").setTabCompleter(new GUtilTabCompleter());
    }

    public static boolean CanExecute(String command) {
        if (command == null || command.isEmpty()) {
            return false;
        }
        for (String disabledCommand : ActiveConfig.disabledCommands) {
            if (disabledCommand.equalsIgnoreCase(command)) {
                return false;
            }
        }
        return ConfigRequirements.isCommandEnabled(command);
    }
}
