package org.gsdistance.grimmsServer.Commands.GAuthCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GAuthTabCompleter implements TabCompleter {
    public static final List<String> subCommands = List.of("register","unregister","login");
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            List<String> suggestions = new ArrayList<>();

            // Dynamically fetch all commands from the plugin
            for (String cmdName : subCommands) {
                if (cmdName.toLowerCase().startsWith(partial)) {
                    suggestions.add(cmdName);
                }
            }

            return suggestions;
        }
        return List.of();
    }
}
