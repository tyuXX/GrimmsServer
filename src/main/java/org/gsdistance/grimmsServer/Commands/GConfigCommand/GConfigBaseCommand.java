package org.gsdistance.grimmsServer.Commands.GConfigCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GConfigBaseCommand implements CommandExecutor {
   public GConfigBaseCommand() {
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      if (args.length < 1) {
         return false;
      } else {
         boolean var10000;
         switch (args[0].toLowerCase()) {
            case "dump" -> var10000 = Dump.subCommand((Player)sender);
            case "reload" -> var10000 = Reload.subCommand();
            default -> var10000 = false;
         }

         return var10000;
      }
   }
}
