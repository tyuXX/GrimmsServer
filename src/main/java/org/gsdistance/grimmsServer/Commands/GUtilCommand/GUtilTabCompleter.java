package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GUtilTabCompleter implements TabCompleter {
    public static final List<String> defSubCommands = List.of(
            "version",
            "setting"
    );
    public static final List<String> adminSubCommands = List.of(
            "relic",
            "capability"
    );

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> subCommands = new ArrayList<>();
        if (!(sender instanceof Player player)) {
            return List.of();
        }
        if (sender.hasPermission("grimmsserver.gutil.admin")) {
            subCommands.addAll(adminSubCommands);
        }
        subCommands.addAll(defSubCommands);
        if (args.length == 1) {
            return subCommands.stream()
                    .filter(subCommand -> subCommand.startsWith(args[0].toLowerCase()))
                    .toList();
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "relic" -> {
                    return List.of("make", "set", "recalc", "reroll");
                }
                case "version" -> {
                    return List.of("check", "update");
                }
                case "setting" -> {
                    return PlayerMetadata.getPlayerMetadata(player).settings.stream().toList();
                }
                default -> {
                    return List.of();
                }
            }
        }
        return List.of();
    }
}
