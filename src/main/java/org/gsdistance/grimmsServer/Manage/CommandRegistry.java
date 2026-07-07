package org.gsdistance.grimmsServer.Manage;

import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Commands.*;
import org.gsdistance.grimmsServer.Commands.GAuthCommand.GAuthBaseCommand;
import org.gsdistance.grimmsServer.Commands.GAuthCommand.GAuthTabCompleter;
import org.gsdistance.grimmsServer.Commands.GConfigCommand.GConfigBaseCommand;
import org.gsdistance.grimmsServer.Commands.GConfigCommand.GConfigTabCompleter;
import org.gsdistance.grimmsServer.Commands.GDecoCommand.GDecoBaseCommand;
import org.gsdistance.grimmsServer.Commands.GDecoCommand.GDecoTabCompleter;
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
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Config.ConfigKey;
import org.gsdistance.grimmsServer.Data.ConfigRequirements;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.List;

public class CommandRegistry {
    public CommandRegistry() {
    }

    public static void registerCommands() {
        PluginCommand sendMoneyCmd = GrimmsServer.instance.getCommand("sendMoney");
        if (sendMoneyCmd != null) sendMoneyCmd.setExecutor(new SendMoney());
        PluginCommand makeItemLevelableCmd = GrimmsServer.instance.getCommand("makeItemLevelable");
        if (makeItemLevelableCmd != null) makeItemLevelableCmd.setExecutor(new MakeItemLevelable());
        PluginCommand setPlayerStatCmd = GrimmsServer.instance.getCommand("setPlayerStat");
        if (setPlayerStatCmd != null) setPlayerStatCmd.setExecutor(new SetPlayerStat());
        PluginCommand addTitleCmd = GrimmsServer.instance.getCommand("addTitle");
        if (addTitleCmd != null) addTitleCmd.setExecutor(new AddTitle());
        PluginCommand executePlayerCmd = GrimmsServer.instance.getCommand("executePlayer");
        if (executePlayerCmd != null) executePlayerCmd.setExecutor(new ExecutePlayer());
        PluginCommand grimmsServerCommandsCmd = GrimmsServer.instance.getCommand("grimmsServerCommands");
        if (grimmsServerCommandsCmd != null) grimmsServerCommandsCmd.setExecutor(new LogGrimmsServerCommands());
        PluginCommand withdrawMoneyCmd = GrimmsServer.instance.getCommand("withdrawMoney");
        if (withdrawMoneyCmd != null) withdrawMoneyCmd.setExecutor(new WithdrawMoney());
        PluginCommand depositMoneyCmd = GrimmsServer.instance.getCommand("depositMoney");
        if (depositMoneyCmd != null) depositMoneyCmd.setExecutor(new DepositMoney());
        PluginCommand acceptRequestCmd = GrimmsServer.instance.getCommand("acceptRequest");
        if (acceptRequestCmd != null) {
            acceptRequestCmd.setExecutor(new AcceptRequest());
            acceptRequestCmd.setTabCompleter(new RequestTabCompleter());
        }
        PluginCommand removeTitleCmd = GrimmsServer.instance.getCommand("removeTitle");
        if (removeTitleCmd != null) removeTitleCmd.setExecutor(new RemoveTitle());
        PluginCommand homeCmd = GrimmsServer.instance.getCommand("home");
        if (homeCmd != null) {
            homeCmd.setExecutor(new HomeBaseCommand());
            homeCmd.setTabCompleter(new HomeTabCompleter());
        }
        PluginCommand nickCmd = GrimmsServer.instance.getCommand("nick");
        if (nickCmd != null) nickCmd.setExecutor(new Nick());
        PluginCommand marketCmd = GrimmsServer.instance.getCommand("market");
        if (marketCmd != null) {
            marketCmd.setExecutor(new MarketBaseCommand());
            marketCmd.setTabCompleter(new MarketTabCompleter());
        }
        PluginCommand gUtilCmd = GrimmsServer.instance.getCommand("gUtil");
        if (gUtilCmd != null) {
            gUtilCmd.setExecutor(new GUtilBaseCommand());
            gUtilCmd.setTabCompleter(new GUtilTabCompleter());
        }
        PluginCommand gLogCmd = GrimmsServer.instance.getCommand("gLog");
        if (gLogCmd != null) {
            gLogCmd.setExecutor(new GLogBaseCommand());
            gLogCmd.setTabCompleter(new GLogTabCompleter());
        }
        PluginCommand gDimCmd = GrimmsServer.instance.getCommand("gDim");
        if (gDimCmd != null) {
            gDimCmd.setExecutor(new GDimBaseCommand());
            gDimCmd.setTabCompleter(new GDimTabCompleter());
        }
        PluginCommand gFactionCmd = GrimmsServer.instance.getCommand("gFaction");
        if (gFactionCmd != null) {
            gFactionCmd.setExecutor(new GFactionBaseCommand());
            gFactionCmd.setTabCompleter(new GFactionTabCompleter());
        }
        PluginCommand gConfigCmd = GrimmsServer.instance.getCommand("gConfig");
        if (gConfigCmd != null) {
            gConfigCmd.setExecutor(new GConfigBaseCommand());
            gConfigCmd.setTabCompleter(new GConfigTabCompleter());
        }
        PluginCommand jobCmd = GrimmsServer.instance.getCommand("job");
        if (jobCmd != null) {
            jobCmd.setExecutor(new JobBaseCommand());
            jobCmd.setTabCompleter(new JobTabCompleter());
        }
        PluginCommand gHelpCmd = GrimmsServer.instance.getCommand("gHelp");
        if (gHelpCmd != null) {
            gHelpCmd.setExecutor(new GHelp());
            gHelpCmd.setTabCompleter(new GHelpTabCompleter());
        }
        PluginCommand gAuthCmd = GrimmsServer.instance.getCommand("gAuth");
        if (gAuthCmd != null) {
            gAuthCmd.setExecutor(new GAuthBaseCommand());
            gAuthCmd.setTabCompleter(new GAuthTabCompleter());
        }
        PluginCommand gDecoCmd = GrimmsServer.instance.getCommand("gDeco");
        if (gDecoCmd != null) {
            gDecoCmd.setExecutor(new GDecoBaseCommand());
            gDecoCmd.setTabCompleter(new GDecoTabCompleter());
        }
    }

    public static boolean CanExecute(String command, Player player) {
        if (command != null && !command.isEmpty()) {
            if (!GAuthBaseCommand.isLoggedIn(player) && !command.equalsIgnoreCase("gAuth")) {
                return false;
            } else {
                @SuppressWarnings("unchecked")
                List<String> disabledCommands = ActiveConfig.getConfigValue(ConfigKey.DISABLED_COMMANDS, List.class);
                if (disabledCommands != null) {
                    for (String disabledCommand : disabledCommands) {
                        if (disabledCommand.equalsIgnoreCase(command)) {
                            return false;
                        }
                    }
                }

                return ConfigRequirements.isCommandEnabled(command);
            }
        } else {
            return false;
        }
    }
}
