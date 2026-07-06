package org.gsdistance.grimmsServer.Commands.GLogCommand;

import com.google.gson.Gson;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.World.ChunkMetadata;
import org.jetbrains.annotations.NotNull;

public class GLogBaseCommand implements CommandExecutor {
   public GLogBaseCommand() {
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      if (sender instanceof Player player) {
         if (args.length == 0) {
            return false;
         } else {
            boolean var10000;
            switch (args[0].toLowerCase()) {
               case "self_stats":
                  var10000 = LogSelfStats.subCommand(player);
                  break;
               case "other_stats":
                  var10000 = LogPlayerStats.subCommand(player, args);
                  break;
               case "self_titles":
                  var10000 = LogSelfTitles.subCommand(player);
                  break;
               case "other_titles":
                  var10000 = LogPlayerTitles.subCommand(player, args);
                  break;
               case "world":
                  var10000 = LogWorldStats.subCommand(player);
                  break;
               case "leaderboard":
                  var10000 = LogLeaderboard.subCommand(player);
                  break;
               case "commands":
                  var10000 = player.performCommand("grimmsserver:grimmsServerCommands");
                  break;
               case "chunk":
                  player.sendMessage((new Gson()).toJson(ChunkMetadata.getChunkMetadata(player.getLocation().getChunk())));
                  var10000 = true;
                  break;
               default:
                  var10000 = false;
            }

            return var10000;
         }
      } else {
         sender.sendMessage("Only players can use this command.");
         return true;
      }
   }
}
