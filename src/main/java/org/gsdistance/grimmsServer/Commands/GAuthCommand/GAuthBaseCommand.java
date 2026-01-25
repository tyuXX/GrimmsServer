package org.gsdistance.grimmsServer.Commands.GAuthCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.jetbrains.annotations.NotNull;

public class GAuthBaseCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length < 2) {
            return false;
        }
        Player player = (Player) sender;
        return switch (args[0].toLowerCase()) {
            case "login" -> Login.subCommand(player, args[1]);
            case "register" -> Register.subCommand(player, args[1]);
            case "unregister" -> Unregister.subCommand(player, args[1]);
            default -> false;
        };
    }

    public static boolean isLoggedIn(Player player){
        return GrimmsServer.instance.getServer().isEnforcingSecureProfiles() || Boolean.TRUE.equals(PerSessionDataStorage.getData(player.getUniqueId() + "-login", Boolean.class)) || Boolean.TRUE.equals(PlayerStats.getPlayerStats(player).getStat("fauth", Boolean.class));
    }

    public static void login(Player player, boolean in){
        PerSessionDataStorage.softSave(in, Boolean.class, player.getUniqueId() + "-login");
    }
}
