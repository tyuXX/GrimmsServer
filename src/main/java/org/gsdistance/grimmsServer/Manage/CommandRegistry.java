package org.gsdistance.grimmsServer.Manage;

import org.gsdistance.grimmsServer.Commands.*;
import org.gsdistance.grimmsServer.Commands.GConfigCommand.GConfigBaseCommand;
import org.gsdistance.grimmsServer.Commands.GConfigCommand.GConfigTabCompleter;
import org.gsdistance.grimmsServer.Commands.GDimensionCommand.GDimBaseCommand;
import org.gsdistance.grimmsServer.Commands.GDimensionCommand.GDimTabCompleter;
import org.gsdistance.grimmsServer.Commands.GFactionCommand.GFactionBaseCommand;
import org.gsdistance.grimmsServer.Commands.GFactionCommand.GFactionTabCompleter;
import org.gsdistance.grimmsServer.Commands.GHelpCommand.GHelp;
import org.gsdistance.grimmsServer.Commands.GHelpCommand.GHelpTabCompleter;
import org.gsdistance.grimmsServer.Commands.GLogCommand.GLogBaseCommand;
import org.gsdistance.grimmsServer.Commands.GLogCommand.GLogTabCompleter;
import org.gsdistance.grimmsServer.Commands.GUtilCommand.GUtilBaseCommand;
import org.gsdistance.grimmsServer.Commands.GUtilCommand.GUtilTabCompleter;
import org.gsdistance.grimmsServer.Commands.HomeCommand.HomeBaseCommand;
import org.gsdistance.grimmsServer.Commands.HomeCommand.HomeTabCompleter;
import org.gsdistance.grimmsServer.Commands.JobCommand.JobBaseCommand;
import org.gsdistance.grimmsServer.Commands.JobCommand.JobTabCompleter;
import org.gsdistance.grimmsServer.Commands.MarketComand.MarketBaseCommand;
import org.gsdistance.grimmsServer.Commands.MarketComand.MarketTabCompleter;
import org.gsdistance.grimmsServer.Commands.RequestCommand.AcceptRequest;
import org.gsdistance.grimmsServer.Commands.RequestCommand.RequestTabCompleter;
import org.gsdistance.grimmsServer.Config.ConfigKey;
import org.gsdistance.grimmsServer.Data.ConfigRequirements;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.List;

import static org.gsdistance.grimmsServer.Config.ActiveConfig.getConfigValue;

public class CommandRegistry {
    @SuppressWarnings("DataFlowIssue")
    public static void registerCommands() {
        // Register all commands here
        GrimmsServer.instance.getCommand("sendMoney").setExecutor(new SendMoney());
        GrimmsServer.instance.getCommand("makeItemLevelable").setExecutor(new MakeItemLevelable());
        GrimmsServer.instance.getCommand("setPlayerStat").setExecutor(new SetPlayerStat());
        GrimmsServer.instance.getCommand("addTitle").setExecutor(new AddTitle());
        GrimmsServer.instance.getCommand("executePlayer").setExecutor(new ExecutePlayer());
        GrimmsServer.instance.getCommand("grimmsServerCommands").setExecutor(new LogGrimmsServerCommands());
        GrimmsServer.instance.getCommand("withdrawMoney").setExecutor(new WithdrawMoney());
        GrimmsServer.instance.getCommand("depositMoney").setExecutor(new DepositMoney());
        GrimmsServer.instance.getCommand("acceptRequest").setExecutor(new AcceptRequest());
        GrimmsServer.instance.getCommand("acceptRequest").setTabCompleter(new RequestTabCompleter());
        GrimmsServer.instance.getCommand("removeTitle").setExecutor(new RemoveTitle());
        GrimmsServer.instance.getCommand("home").setExecutor(new HomeBaseCommand());
        GrimmsServer.instance.getCommand("home").setTabCompleter(new HomeTabCompleter());
        GrimmsServer.instance.getCommand("nick").setExecutor(new Nick());
        GrimmsServer.instance.getCommand("market").setExecutor(new MarketBaseCommand());
        GrimmsServer.instance.getCommand("market").setTabCompleter(new MarketTabCompleter());
        GrimmsServer.instance.getCommand("gUtil").setExecutor(new GUtilBaseCommand());
        GrimmsServer.instance.getCommand("gUtil").setTabCompleter(new GUtilTabCompleter());
        GrimmsServer.instance.getCommand("gLog").setExecutor(new GLogBaseCommand());
        GrimmsServer.instance.getCommand("gLog").setTabCompleter(new GLogTabCompleter());
        GrimmsServer.instance.getCommand("gDim").setExecutor(new GDimBaseCommand());
        GrimmsServer.instance.getCommand("gDim").setTabCompleter(new GDimTabCompleter());
        GrimmsServer.instance.getCommand("gFaction").setExecutor(new GFactionBaseCommand());
        GrimmsServer.instance.getCommand("gFaction").setTabCompleter(new GFactionTabCompleter());
        GrimmsServer.instance.getCommand("gConfig").setExecutor(new GConfigBaseCommand());
        GrimmsServer.instance.getCommand("gConfig").setTabCompleter(new GConfigTabCompleter());
        GrimmsServer.instance.getCommand("job").setExecutor(new JobBaseCommand());
        GrimmsServer.instance.getCommand("job").setTabCompleter(new JobTabCompleter());
        GrimmsServer.instance.getCommand("gHelp").setExecutor(new GHelp());
        GrimmsServer.instance.getCommand("gHelp").setTabCompleter(new GHelpTabCompleter());

    }

    public static boolean CanExecute(String command) {
        if (command == null || command.isEmpty()) {
            return false;
        }
        // Retrieve the disabled commands as a List
        @SuppressWarnings("unchecked")
        List<String> disabledCommands = getConfigValue(ConfigKey.DISABLED_COMMANDS, List.class);
        if (disabledCommands != null) {
            for (String disabledCommand : disabledCommands) {
                if (disabledCommand.equalsIgnoreCase(command)) {
                    return false;
                }
            }
        }
        return ConfigRequirements.isCommandEnabled(command);
    }
}
