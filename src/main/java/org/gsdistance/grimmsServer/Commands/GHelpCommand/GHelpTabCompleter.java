package org.gsdistance.grimmsServer.Commands.GHelpCommand;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GHelpTabCompleter implements TabCompleter {
   public GHelpTabCompleter() {
   }

   @Nullable
   public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      if (args.length == 1) {
         String partial = args[0].toLowerCase();
         List<String> suggestions = new ArrayList();

         for(String cmdName : GrimmsServer.instance.getDescription().getCommands().keySet()) {
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
