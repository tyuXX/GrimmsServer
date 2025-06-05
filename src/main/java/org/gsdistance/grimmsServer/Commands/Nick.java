package org.gsdistance.grimmsServer.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Data.PlayerMetadata;

public class Nick implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                // /nick <nickname>
                if (!player.hasPermission("grimmsserver.nick")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to change your nickname.");
                    return true;
                }
                String newNick = ChatColor.translateAlternateColorCodes('&', args[0]);
                PlayerMetadata meta = PlayerMetadata.getPlayerMetadata(player);
                meta.nickname = newNick;
                meta.saveToPDS();
                player.sendMessage(ChatColor.GREEN + "Your nickname has been set to " + newNick);
                return true;
            } else if (args.length >= 2) {
                // /nick <player> <nickname>
                if (!player.hasPermission("grimmsserver.nick.others")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to set others' nicknames.");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(ChatColor.RED + "Player not found.");
                    return true;
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    sb.append(args[i]);
                    if (i < args.length - 1) sb.append(" ");
                }
                String newNick = ChatColor.translateAlternateColorCodes('&', sb.toString());
                PlayerMetadata meta = PlayerMetadata.getPlayerMetadata(target);
                meta.nickname = newNick;
                meta.saveToPDS();
                player.sendMessage(ChatColor.GREEN + "Set " + target.getName() + "'s nickname to " + newNick);
                target.sendMessage(ChatColor.YELLOW + "Your nickname has been set to " + newNick + " by " + player.getName());
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "Usage: /nick <nickname> OR /nick <player> <nickname>");
                return true;
            }
        }else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}
