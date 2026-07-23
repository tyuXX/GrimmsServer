package org.gsdistance.grimmsServer.Commands.GStatsCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.gsdistance.grimmsServer.Shared.getOnlinePlayers;

public class GStatsTabCompleter implements TabCompleter {
    private static final List<String> SUBCOMMANDS = Arrays.asList("self_history", "others_history", "commands");
    private static final List<String> TIME_RANGES = Arrays.asList("hour", "day", "week", "month", "all");
    private static final List<String> GRAPH_OPTIONS = List.of("largegraph");

    public GStatsTabCompleter() {
    }

    @Nullable
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            List<String> suggestions = new ArrayList<>();

            for (String subcommand : SUBCOMMANDS) {
                if (subcommand.startsWith(partial) && hasPermissionForSubcommand(sender, subcommand)) {
                    suggestions.add(subcommand);
                }
            }

            return suggestions;
        } else if (args.length == 2) {
            String subcommand = args[0].toLowerCase();
            if (subcommand.equals("others_history")) {
                String partialPlayer = args[1].toLowerCase();
                List<String> playerSuggestions = new ArrayList<>();

                return getOnlinePlayers(partialPlayer, playerSuggestions);
            } else if (subcommand.equals("self_history")) {
                String partialTime = args[1].toLowerCase();
                List<String> timeSuggestions = new ArrayList<>();

                for (String timeRange : TIME_RANGES) {
                    if (timeRange.startsWith(partialTime)) {
                        timeSuggestions.add(timeRange);
                    }
                }

                return timeSuggestions;
            }
        } else if (args.length == 3) {
            String subcommand = args[0].toLowerCase();
            if (subcommand.equals("others_history")) {
                String partialTime = args[2].toLowerCase();
                List<String> timeSuggestions = new ArrayList<>();

                for (String timeRange : TIME_RANGES) {
                    if (timeRange.startsWith(partialTime)) {
                        timeSuggestions.add(timeRange);
                    }
                }

                return timeSuggestions;
            } else if (subcommand.equals("self_history")) {
                String partialStat = args[2].toLowerCase();
                List<String> statSuggestions = new ArrayList<>();

                for (String stat : PlayerStats.StatOrder) {
                    if (stat.startsWith(partialStat)) {
                        statSuggestions.add(stat);
                    }
                }

                return statSuggestions;
            }
        } else if (args.length == 4) {
            String subcommand = args[0].toLowerCase();
            if (subcommand.equals("others_history")) {
                String partialStat = args[3].toLowerCase();
                List<String> statSuggestions = new ArrayList<>();

                for (String stat : PlayerStats.StatOrder) {
                    if (stat.startsWith(partialStat)) {
                        statSuggestions.add(stat);
                    }
                }

                return statSuggestions;
            }
        } else if (args.length == 5) {
            String subcommand = args[0].toLowerCase();
            if (subcommand.equals("self_history") || subcommand.equals("others_history")) {
                int argIndex = subcommand.equals("self_history") ? 3 : 4;
                String partialGraph = args[argIndex].toLowerCase();
                List<String> graphSuggestions = new ArrayList<>();

                for (String graphOption : GRAPH_OPTIONS) {
                    if (graphOption.startsWith(partialGraph)) {
                        graphSuggestions.add(graphOption);
                    }
                }

                return graphSuggestions;
            }
        }

        return List.of();
    }

    private boolean hasPermissionForSubcommand(CommandSender sender, String subcommand) {
        return switch (subcommand) {
            case "self_history" -> sender.hasPermission("grimmsserver.stats.self");
            case "others_history" -> sender.hasPermission("grimmsserver.stats.other");
            case "commands" -> true;
            default -> false;
        };
    }
}
