package org.gsdistance.grimmsServer.Commands.GAuthCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Config.ConfigKey;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.jetbrains.annotations.NotNull;

public class GAuthBaseCommand implements CommandExecutor {
    public GAuthBaseCommand() {
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /gAuth <login|register|unregister|autologin|clearcooldown> [args]");
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "login":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                    return false;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "This command requires a password.");
                    return false;
                }
                return Login.subCommand((Player) sender, args[1]);
            case "register":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                    return false;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "This command requires a password.");
                    return false;
                }
                return Register.subCommand((Player) sender, args[1]);
            case "unregister":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                    return false;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "This command requires a password.");
                    return false;
                }
                return Unregister.subCommand((Player) sender, args[1]);
            case "autologin":
                if (!(sender instanceof Player player)) {
                    sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                    return false;
                }
                if (!isLoggedIn(player)) {
                    player.sendMessage(ChatColor.RED + "Failed to login.");
                    return false;
                } else {
                    PlayerStats playerStats = PlayerStats.getPlayerStats(player);
                    playerStats.setStat("autologin", !(Boolean) playerStats.getStat("autologin", Boolean.class));
                    player.sendMessage(ChatColor.GREEN + "Toggled autologin, remember that this is considered unsafe.");
                    return true;
                }
            case "clearcooldown":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /gAuth clearcooldown <player>");
                    return false;
                }
                Player target = GrimmsServer.instance.getServer().getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found.");
                    return false;
                }
                String playerKey = target.getUniqueId() + "-login";
                PerSessionDataStorage.softSave(0, Integer.class, playerKey + "-attempts");
                PerSessionDataStorage.softSave(null, Long.class, playerKey + "-cooldown");
                PerSessionDataStorage.softSave(1, Integer.class, playerKey + "-multiplier");
                sender.sendMessage(ChatColor.GREEN + "Cleared login cooldown for " + target.getName());
                return true;
            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand. Usage: /gAuth <login|register|unregister|autologin|clearcooldown> [args]");
                return false;
        }
    }

    public static boolean isLoggedIn(Player player) {
        if (Boolean.FALSE.equals(ActiveConfig.getConfigValue(ConfigKey.FORCE_AUTH, Boolean.class)) && PlayerStats.getPlayerStats(player).getStat("pass", String.class).isEmpty()) {
            return true;
        } else {
            return GrimmsServer.instance.getServer().isEnforcingSecureProfiles() || Boolean.TRUE.equals(PerSessionDataStorage.getData(player.getUniqueId() + "-login", Boolean.class));
        }
    }

    public static void login(Player player, boolean in) {
        PerSessionDataStorage.softSave(in, Boolean.class, player.getUniqueId() + "-login");
    }
}
