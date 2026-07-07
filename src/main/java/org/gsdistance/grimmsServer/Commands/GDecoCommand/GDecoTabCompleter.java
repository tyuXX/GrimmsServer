package org.gsdistance.grimmsServer.Commands.GDecoCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Stats.PlayerTitles;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GDecoTabCompleter implements TabCompleter {
    public static final List<String> subCommands = List.of("selectdecotitle", "cleardecotitle");

    public GDecoTabCompleter() {
    }

    @Nullable
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                return subCommands.stream().filter((subCommand) -> subCommand.startsWith(args[0].toLowerCase())).toList();
            } else if (args.length == 2) {
                switch (args[0].toLowerCase()) {
                    case "selectdecotitle":
                        PlayerTitles playerTitles = PlayerTitles.getPlayerTitles(player);
                        List<String> availableTitles = new ArrayList();

                        for (String title : PlayerTitles.titles.keySet()) {
                            if (playerTitles.hasTitle(title)) {
                                availableTitles.add(title);
                            }
                        }

                        return availableTitles.stream().filter((titlex) -> titlex.toLowerCase().startsWith(args[1].toLowerCase())).toList();
                    case "cleardecotitle":
                        return List.of();
                    default:
                        return List.of();
                }
            } else {
                return List.of();
            }
        } else {
            return List.of();
        }
    }
}
