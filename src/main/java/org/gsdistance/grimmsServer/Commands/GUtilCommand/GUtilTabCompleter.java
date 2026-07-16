package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.Player.PlayerInventoryData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GUtilTabCompleter implements TabCompleter {
    public static final List<String> defSubCommands = List.of("version", "setting");
    public static final List<String> adminSubCommands = List.of("relic", "capability", "broadcast", "inventoryrestore");

    public GUtilTabCompleter() {
    }

    @Nullable
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> subCommands = new ArrayList();
        if (sender instanceof Player player) {
            if (sender.hasPermission("grimmsserver.gutil.admin")) {
                subCommands.addAll(adminSubCommands);
            }

            subCommands.addAll(defSubCommands);
            if (args.length == 1) {
                return subCommands.stream().filter((subCommand) -> subCommand.startsWith(args[0].toLowerCase())).toList();
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
                    case "inventoryrestore" -> {
                        return Bukkit.getOnlinePlayers().stream()
                            .map(Player::getName)
                            .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                            .collect(Collectors.toList());
                    }
                    default -> {
                        return List.of();
                    }
                }
            } else if (args.length == 3) {
                switch (args[0].toLowerCase()) {
                    case "inventoryrestore" -> {
                        Player targetPlayer = Bukkit.getPlayer(args[1]);
                        if (targetPlayer != null) {
                            PlayerInventoryData inventoryData = PlayerInventoryData.getPlayerInventoryData(targetPlayer.getUniqueId());
                            List<String> indices = new ArrayList<>();
                            for (int i = 0; i < inventoryData.previousInventories.size(); i++) {
                                indices.add(String.valueOf(i + 1));
                            }
                            return indices.stream()
                                .filter(index -> index.startsWith(args[2]))
                                .collect(Collectors.toList());
                        }
                        return List.of();
                    }
                    default -> {
                        return List.of();
                    }
                }
            } else {
                return List.of();
            }
        } else {
            return List.of();
        }
    }
}
