package org.gsdistance.grimmsServer.Commands.GLevelCommand;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GLevelTabCompleter implements TabCompleter {
   public GLevelTabCompleter() {
   }

   @Nullable
   public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
      return List.of();
   }
}
