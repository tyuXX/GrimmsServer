package org.gsdistance.grimmsServer.Commands.GConfigCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GConfigTabCompleter implements TabCompleter {
   private static final List<String> SUBCOMMANDS = Arrays.asList("dump", "reload");

   public GConfigTabCompleter() {
   }

   @Nullable
   public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      if (args.length == 1) {
         String partial = args[0].toLowerCase();
         List<String> suggestions = new ArrayList();

         for(String subcommand : SUBCOMMANDS) {
            if (subcommand.startsWith(partial)) {
               suggestions.add(subcommand);
            }
         }

         return suggestions;
      } else {
         return List.of();
      }
   }
}
