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
        if (args.length < 2 && (args.length == 0 || !args[0].equals("autologin"))) {
            sender.sendMessage(ChatColor.RED + "This command requires a password.");
            return false;
        } else {
            Player player = (Player) sender;
            boolean var10000;
            switch (args[0].toLowerCase()) {
                case "login":
                    var10000 = Login.subCommand(player, args[1]);
                    break;
                case "register":
                    var10000 = Register.subCommand(player, args[1]);
                    break;
                case "unregister":
                    var10000 = Unregister.subCommand(player, args[1]);
                    break;
                case "autologin":
                    if (!isLoggedIn(player)) {
                        player.sendMessage(ChatColor.RED + "Failed to login.");
                        var10000 = false;
                    } else {
                        PlayerStats playerStats = PlayerStats.getPlayerStats(player);
                        playerStats.setStat("autologin", !(Boolean) playerStats.getStat("autologin", Boolean.class));
                        player.sendMessage(ChatColor.GREEN + "Toggled autologin, remember that this is considered unsafe.");
                        var10000 = true;
                    }
                    break;
                default:
                    var10000 = false;
            }

            return var10000;
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
