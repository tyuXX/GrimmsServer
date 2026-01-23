package org.gsdistance.grimmsServer.Data;

import java.util.Map;

public class HelpStrings {
    public static final Map<String, String> helpStrings = Map.ofEntries(
            Map.entry("sendmoney", "Send money to another player"),
            Map.entry("makeitemlevelable", "Make the item in your hand levelable (costs 3500 money, requires level 10+)"),
            Map.entry("setplayerstat", "Set a specific stat for a player"),
            Map.entry("addtitle", "Add a title to a player"),
            Map.entry("executeplayer", "Execute a player (requires Executioner title)"),
            Map.entry("grimsservercommands", "List all available GrimmsServer commands"),
            Map.entry("withdrawmoney", "Withdraw money as physical banknotes (minimum 25 money)"),
            Map.entry("depositmoney", "Deposit banknotes by holding them in your main hand"),
            Map.entry("acceptrequest", "Accept a pending request by ID"),
            Map.entry("removetitle", "Remove a title from a player"),
            Map.entry("home", "Manage your homes - set, teleport, list, or delete"),
            Map.entry("nick", "Set your nickname or another player's nickname"),
            Map.entry("market", "Access the market system for buying, selling, and enchanting"),
            Map.entry("gutil", "Utility commands for version info, relics, capabilities, and settings"),
            Map.entry("glog", "View logs for stats, titles, world info, leaderboards, and chunk data"),
            Map.entry("gdim", "Dimension management - create, delete, teleport, list, and get info"),
            Map.entry("gfaction", "Faction management - invite, join, leave, kick, claim, and manage ranks"),
            Map.entry("gconfig", "Configuration management - dump config or reload settings"),
            Map.entry("job", "Job system - log jobs, take jobs, or buy education"),
            Map.entry("ghelp", "Get help for GrimmsServer commands")
    );
    public static final Map<String, String> helpUsages = Map.ofEntries(
            Map.entry("sendmoney", "/sendmoney <player> <amount>"),
            Map.entry("makeitemlevelable", "/makeitemlevelable"),
            Map.entry("setplayerstat", "/setplayerstat <player> <stat> <value>"),
            Map.entry("addtitle", "/addtitle <player> <title>"),
            Map.entry("executeplayer", "/executeplayer <player>"),
            Map.entry("grimsservercommands", "/grimsservercommands"),
            Map.entry("withdrawmoney", "/withdrawmoney <amount> [count]"),
            Map.entry("depositmoney", "/depositmoney"),
            Map.entry("acceptrequest", "/acceptrequest <request_id>"),
            Map.entry("removetitle", "/removetitle <player> <title>"),
            Map.entry("home", "/home <sethome|tp|homes|delhome> [args...]"),
            Map.entry("nick", "/nick <nickname> OR /nick <player> <nickname>"),
            Map.entry("market", "/market <get|stock|ripoff|enchant|tp|sell|sellall|buy|enchcosts|info> [args...]"),
            Map.entry("gutil", "/gutil <version|relic|capability|setting> [args...]"),
            Map.entry("glog", "/glog <self_stats|other_stats|self_titles|other_titles|world|leaderboard|commands|chunk> [args...]"),
            Map.entry("gdim", "/gdim <create|delete|tp|list|info> [args...]"),
            Map.entry("gfaction", "/gfaction <invite|join|leave|kick|info|new|claim|unclaim|setrank|unclaimall> [args...]"),
            Map.entry("gconfig", "/gconfig <dump|reload>"),
            Map.entry("job", "/job <log|take|buyedu> [args...]"),
            Map.entry("ghelp", "/ghelp <command>")
    );
    public static String getHelpString(String commandName) {
        return  helpStrings.getOrDefault(commandName.toLowerCase(), "Not found.");
    }
    public static String getHelpUsage(String commandName) {
        return  helpUsages.getOrDefault(commandName.toLowerCase(), "Not found.");
    }
}
