package org.gsdistance.grimmsServer.Commands.GHelpCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.Plugin;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GHelpTabCompleter implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            List<String> suggestions = new ArrayList<>();
            
            // Dynamically fetch all commands from the plugin
            for (String cmdName : GrimmsServer.instance.getDescription().getCommands().keySet()) {
                if (cmdName.toLowerCase().startsWith(partial)) {
                    suggestions.add(cmdName);
                }
            }
            
            return suggestions;
        }
        return List.of();
    }
}
