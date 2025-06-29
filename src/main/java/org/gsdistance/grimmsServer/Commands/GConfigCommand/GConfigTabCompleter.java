package org.gsdistance.grimmsServer.Commands.GConfigCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GConfigTabCompleter implements TabCompleter {

    private static final List<String> SUBCOMMANDS = Arrays.asList("dump", "reload");

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            List<String> suggestions = new ArrayList<>();
            for (String subcommand : SUBCOMMANDS) {
                if (subcommand.startsWith(partial)) {
                    suggestions.add(subcommand);
                }
            }
            return suggestions;
        }
        return List.of();
    }
}