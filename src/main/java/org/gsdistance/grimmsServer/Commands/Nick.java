package org.gsdistance.grimmsServer.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.jetbrains.annotations.NotNull;

public class Nick implements CommandExecutor {
    public Nick() {
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
            return false;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /nick <nickname> OR /nick <player> <nickname>");
            return true;
        }

        if (args.length == 1) {
            if (!player.hasPermission("grimmsserver.nick")) {
                player.sendMessage(ChatColor.RED + "You don't have permission to change your nickname.");
                return true;
            }

            String newNick = ChatColor.translateAlternateColorCodes('&', args[0]);
            if (newNick.isEmpty()) {
                player.sendMessage(ChatColor.RED + "Nickname cannot be empty.");
                return true;
            }

            PlayerMetadata meta = PlayerMetadata.getPlayerMetadata(player);
            meta.nickname = newNick;
            meta.saveToPDS();
            player.sendMessage(ChatColor.GREEN + "Your nickname has been set to " + ChatColor.YELLOW + newNick);
        } else {
            if (!player.hasPermission("grimmsserver.nick.others")) {
                player.sendMessage(ChatColor.RED + "You don't have permission to set others' nicknames.");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Player '" + args[0] + "' not found or not online.");
                return true;
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; ++i) {
                sb.append(args[i]);
                if (i < args.length - 1) {
                    sb.append(" ");
                }
            }

            String newNick = ChatColor.translateAlternateColorCodes('&', sb.toString());
            if (newNick.isEmpty()) {
                player.sendMessage(ChatColor.RED + "Nickname cannot be empty.");
                return true;
            }

            PlayerMetadata meta = PlayerMetadata.getPlayerMetadata(target);
            meta.nickname = newNick;
            meta.saveToPDS();
            player.sendMessage(ChatColor.GREEN + "Set " + ChatColor.YELLOW + target.getName() + ChatColor.GREEN + "'s nickname to " + ChatColor.YELLOW + newNick);
            target.sendMessage(ChatColor.YELLOW + "Your nickname has been set to " + newNick + ChatColor.YELLOW + " by " + ChatColor.GREEN + player.getName());
        }
        return true;
    }
}
