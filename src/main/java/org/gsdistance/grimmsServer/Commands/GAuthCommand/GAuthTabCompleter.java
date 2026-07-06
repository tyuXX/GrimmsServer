package org.gsdistance.grimmsServer.Commands.GAuthCommand;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GAuthTabCompleter implements TabCompleter {
   public static final List<String> subCommands = List.of("register", "unregister", "login", "autologin");

   public GAuthTabCompleter() {
   }

   @Nullable
   public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      if (args.length == 1) {
         String partial = args[0].toLowerCase();
         List<String> suggestions = new ArrayList();

         for(String cmdName : subCommands) {
            if (cmdName.toLowerCase().startsWith(partial)) {
               suggestions.add(cmdName);
            }
         }

         return suggestions;
      } else {
         return List.of();
      }
   }
}
