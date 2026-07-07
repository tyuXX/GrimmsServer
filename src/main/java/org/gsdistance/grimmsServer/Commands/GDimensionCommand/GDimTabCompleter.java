package org.gsdistance.grimmsServer.Commands.GDimensionCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.generator.WorldInfo;
import org.gsdistance.grimmsServer.Constructable.World.WorldConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GDimTabCompleter implements TabCompleter {
    public GDimTabCompleter() {
    }

    @Nullable
    public java.util.List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Stream.of("create", "delete", "tp", "list", "info").filter((sub) -> sub.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        } else if (args.length == 2) {
            java.util.List var10000;
            switch (args[0].toLowerCase()) {
                case "create":
                    var10000 = java.util.List.of("<worldName>");
                    break;
                case "info":
                case "tp":
                    var10000 = Bukkit.getWorlds().stream().map(WorldInfo::getName).filter((name) -> name.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
                    break;
                case "delete":
                    var10000 = Arrays.stream(WorldConstructor.getAllWorldConstructors()).map(WorldConstructor::name).toList();
                    break;
                default:
                    var10000 = java.util.List.of();
            }

            return var10000;
        } else if (args.length == 3 && args[0].equalsIgnoreCase("create")) {
            return Stream.of("NORMAL", "NETHER", "THE_END").filter((type) -> type.toLowerCase().startsWith(args[2].toLowerCase())).collect(Collectors.toList());
        } else if (args.length == 4 && args[0].equalsIgnoreCase("create")) {
            return Stream.of("true", "false").filter((option) -> option.toLowerCase().startsWith(args[3].toLowerCase())).collect(Collectors.toList());
        } else if (args.length == 5 && args[0].equalsIgnoreCase("create")) {
            return Stream.of("normal", "flat", "amplified", "large_biomes").filter((env) -> env.toUpperCase().startsWith(args[4].toUpperCase())).collect(Collectors.toList());
        } else if (args.length == 6 && args[0].equalsIgnoreCase("create")) {
            return java.util.List.of("<seed>");
        } else {
            return args.length == 7 && args[0].equalsIgnoreCase("create") ? java.util.List.of("<generatorSettings>") : java.util.List.of();
        }
    }
}
