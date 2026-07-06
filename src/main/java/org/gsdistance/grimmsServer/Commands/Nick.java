package org.gsdistance.grimmsServer.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;

public class Nick implements CommandExecutor {
   public Nick() {
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (sender instanceof Player player) {
         if (args.length == 1) {
            if (!player.hasPermission("grimmsserver.nick")) {
               player.sendMessage(String.valueOf(ChatColor.RED) + "You don't have permission to change your nickname.");
               return true;
            } else {
               String newNick = ChatColor.translateAlternateColorCodes('&', args[0]);
               PlayerMetadata meta = PlayerMetadata.getPlayerMetadata(player);
               meta.nickname = newNick;
               meta.saveToPDS();
               String var13 = String.valueOf(ChatColor.GREEN);
               player.sendMessage(var13 + "Your nickname has been set to " + newNick);
               return true;
            }
         } else if (args.length >= 2) {
            if (!player.hasPermission("grimmsserver.nick.others")) {
               player.sendMessage(String.valueOf(ChatColor.RED) + "You don't have permission to set others' nicknames.");
               return true;
            } else {
               Player target = Bukkit.getPlayer(args[0]);
               if (target == null) {
                  player.sendMessage(String.valueOf(ChatColor.RED) + "Player not found.");
                  return true;
               } else {
                  StringBuilder sb = new StringBuilder();

                  for(int i = 1; i < args.length; ++i) {
                     sb.append(args[i]);
                     if (i < args.length - 1) {
                        sb.append(" ");
                     }
                  }

                  String newNick = ChatColor.translateAlternateColorCodes('&', sb.toString());
                  PlayerMetadata meta = PlayerMetadata.getPlayerMetadata(target);
                  meta.nickname = newNick;
                  meta.saveToPDS();
                  String var10001 = String.valueOf(ChatColor.GREEN);
                  player.sendMessage(var10001 + "Set " + target.getName() + "'s nickname to " + newNick);
                  target.sendMessage(String.valueOf(ChatColor.YELLOW) + "Your nickname has been set to " + newNick + " by " + player.getName());
                  return true;
               }
            }
         } else {
            player.sendMessage(String.valueOf(ChatColor.RED) + "Usage: /nick <nickname> OR /nick <player> <nickname>");
            return true;
         }
      } else {
         sender.sendMessage("This command can only be run by a player.");
         return false;
      }
   }
}
