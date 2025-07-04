package org.gsdistance.grimmsServer.Commands.JobCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Data.JobTitlesBaseValues;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JobTabCompleter implements TabCompleter {
    private static final List<String> SUBCOMMANDS = Arrays.asList(
            "log",
            "take",
            "buyedu"
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
        }
        if (args.length == 2) {
            String partial = args[1].toLowerCase();
            List<String> suggestions = new ArrayList<>();
            for (String jobName : JobTitlesBaseValues.jobTitleBaseValues.keySet()) {
                switch (args[0].toLowerCase()) {
                    case "log":
                        if (jobName.toLowerCase().startsWith(partial)) {
                            suggestions.add(jobName);
                        }
                        break;
                    case "take":
                        if (jobName.toLowerCase().startsWith(partial) && JobTitlesBaseValues.jobTitleBaseValues.get(jobName).additionalRequirement().apply((Player) sender)) {
                            suggestions.add(jobName);
                        }
                        break;
                }
            }
            return suggestions; // Replace with actual job names if needed
        }
        return List.of();
    }
}
