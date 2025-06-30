package org.gsdistance.grimmsServer.Commands.GLogCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GLogTabCompleter implements TabCompleter {

    private static final List<String> SUBCOMMANDS = Arrays.asList(
            "self_stats",
            "other_stats",
            "self_titles",
            "other_titles",
            "world",
            "leaderboard",
            "commands",
            "chunk"
    );

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            // Suggest subcommands based on the current input
            String partial = args[0].toLowerCase();
            List<String> suggestions = new ArrayList<>();
            for (String subcommand : SUBCOMMANDS) {
                if (subcommand.startsWith(partial)) {
                    suggestions.add(subcommand);
                }
            }
            return suggestions;
        } else if (args.length == 2) {
            // Suggest player names for specific subcommands
            String subcommand = args[0].toLowerCase();
            if (subcommand.equals("other_stats") || subcommand.equals("other_titles")) {
                String partialPlayer = args[1].toLowerCase();
                List<String> playerSuggestions = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().toLowerCase().startsWith(partialPlayer)) {
                        playerSuggestions.add(player.getName());
                    }
                }
                return playerSuggestions;
            }
        }
        return List.of();
    }
}