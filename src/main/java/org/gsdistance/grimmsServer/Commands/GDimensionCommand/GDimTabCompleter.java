package org.gsdistance.grimmsServer.Commands.GDimensionCommand;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.gsdistance.grimmsServer.Constructable.World.WorldConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GDimTabCompleter implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            // Provide subcommands for the first argument
            return Stream.of("create", "delete", "tp", "list", "info")
                    .filter(sub -> sub.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2) {
            String subCommand = args[0].toLowerCase();
            return switch (subCommand) {
                case "create" ->
                    // Suggest a new world name
                        List.of("<worldName>");
                case "info", "tp" ->
                    // Suggest existing world names
                        Bukkit.getWorlds().stream()
                                .map(World::getName)
                                .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                                .collect(Collectors.toList());
                case "delete" ->
                        Arrays.stream(WorldConstructor.getAllWorldConstructors()).map(WorldConstructor::name).toList();
                default -> List.of();
            };
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("create")) {
            // Suggest world types for the "create" command
            return Stream.of("NORMAL", "NETHER", "THE_END")
                    .filter(type -> type.toLowerCase().startsWith(args[2].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 4 && args[0].equalsIgnoreCase("create")) {
            // Suggest true/false for "generateStructures" in the "create" command
            return Stream.of("true", "false")
                    .filter(option -> option.toLowerCase().startsWith(args[3].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 5 && args[0].equalsIgnoreCase("create")) {
            // Suggest environment types for the "create" command
            return Stream.of("normal", "flat", "amplified", "large_biomes")
                    .filter(env -> env.toUpperCase().startsWith(args[4].toUpperCase()))
                    .collect(Collectors.toList());
        }
        if (args.length == 6 && args[0].equalsIgnoreCase("create")) {
            // Suggest a seed for the "create" command
            return List.of("<seed>");
        }
        if (args.length == 7 && args[0].equalsIgnoreCase("create")) {
            // Suggest generator settings for the "create" command
            return List.of("<generatorSettings>");
        }

        return List.of();
    }
}
