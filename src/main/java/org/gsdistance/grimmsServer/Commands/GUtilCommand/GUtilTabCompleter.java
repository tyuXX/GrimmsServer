package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Item.CustomItemRegistry;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.CustomEnchantment;
import org.gsdistance.grimmsServer.Data.Player.PlayerInventoryData;
import org.gsdistance.grimmsServer.Stats.PlayerTitles;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GUtilTabCompleter implements TabCompleter {
    public static final List<String> defSubCommands = List.of("version", "setting", "spawn");
    public static final List<String> adminSubCommands = List.of("relic", "enchant", "givecustomitem", "capability", "broadcast", "inventoryrestore", "fly", "god", "heal", "speed", "enderchest", "invsee", "addtitle", "removetitle", "unlevelentity", "sudo", "levelentity");

    private CommandMap cachedCommandMap;

    public GUtilTabCompleter() {
    }

    private CommandMap getCommandMap() {
        if (cachedCommandMap != null) {
            return cachedCommandMap;
        }
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            cachedCommandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            return cachedCommandMap;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to get command map", e);
        }
    }

    private TabCompleter getTabCompleter(Command command) {
        try {
            Field tabCompleterField = command.getClass().getDeclaredField("tabCompleter");
            tabCompleterField.setAccessible(true);
            return (TabCompleter) tabCompleterField.get(command);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    private java.util.Map<String, Command> getKnownCommands(CommandMap commandMap) {
        try {
            Field knownCommandsField = commandMap.getClass().getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            @SuppressWarnings("unchecked")
            java.util.Map<String, Command> knownCommands = (java.util.Map<String, Command>) knownCommandsField.get(commandMap);
            return knownCommands;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return new java.util.HashMap<>();
        }
    }

    @Nullable
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> subCommands = new ArrayList<>();
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
                    case "enchant" -> {
                        return List.of("add", "remove", "set", "list", "clear", "has");
                    }
                    case "givecustomitem" -> {
                        return CustomItemRegistry.getRegisteredItemIds().stream()
                                .filter(id -> id.startsWith(args[1].toLowerCase()))
                                .collect(Collectors.toList());
                    }
                    case "version" -> {
                        return List.of("check", "update");
                    }
                    case "setting" -> {
                        return PlayerMetadata.getPlayerMetadata(player).settings.stream().toList();
                    }
                    case "inventoryrestore", "heal", "spawn", "enderchest", "invsee", "addtitle", "removetitle", "sudo" -> {
                        return Bukkit.getOnlinePlayers().stream()
                                .map(Player::getName)
                                .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                                .collect(Collectors.toList());
                    }
                    case "fly", "god" -> {
                        return Stream.of("on", "off", "true", "false")
                                .filter(option -> option.startsWith(args[1].toLowerCase()))
                                .toList();
                    }
                    case "speed" -> {
                        return Stream.of("walk", "fly")
                                .filter(type -> type.startsWith(args[1].toLowerCase()))
                                .toList();
                    }
                    case "levelentity", "unlevelentity" -> {
                        return player.getWorld().getEntities().stream()
                                .map(entity -> entity.getUniqueId().toString())
                                .filter(uuid -> uuid.toLowerCase().startsWith(args[1].toLowerCase()))
                                .collect(Collectors.toList());
                    }
                    default -> {
                        return List.of();
                    }
                }
            } else if (args.length > 3 && args[0].equalsIgnoreCase("sudo")) {
                // Handle nested tab completion for sudo command
                String commandName = args[2];
                org.bukkit.command.Command targetCommand = getCommandMap().getCommand(commandName);
                if (targetCommand != null) {
                    TabCompleter tabCompleter = getTabCompleter(targetCommand);
                    if (tabCompleter != null) {
                        // Build the args array for the target command
                        String[] targetArgs = new String[args.length - 3];
                        System.arraycopy(args, 3, targetArgs, 0, targetArgs.length);
                        return tabCompleter.onTabComplete(sender, targetCommand, commandName, targetArgs);
                    }
                }
                return List.of();
            } else if (args.length == 3) {
                switch (args[0].toLowerCase()) {
                    case "enchant" -> {
                        if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove") || 
                            args[1].equalsIgnoreCase("set") || args[1].equalsIgnoreCase("has")) {
                            return java.util.Arrays.stream(CustomEnchantment.values())
                                    .map(enchant -> enchant.name().toLowerCase())
                                    .filter(name -> name.startsWith(args[2].toLowerCase()))
                                    .collect(Collectors.toList());
                        }
                        return List.of();
                    }
                    case "givecustomitem" -> {
                        return java.util.Arrays.stream(Material.values())
                                .map(material -> material.name().toLowerCase())
                                .filter(name -> name.startsWith(args[2].toLowerCase()))
                                .collect(Collectors.toList());
                    }
                    case "relic" -> {
                        if (args[1].equalsIgnoreCase("set")) {
                            List<String> tiers = new ArrayList<>();
                            for (int i = 1; i <= 10; i++) {
                                tiers.add(String.valueOf(i));
                            }
                            return tiers.stream()
                                    .filter(tier -> tier.startsWith(args[2]))
                                    .toList();
                        }
                        return List.of();
                    }
                    case "speed" -> {
                        List<String> speeds = new ArrayList<>();
                        for (int i = 1; i <= 10; i++) {
                            speeds.add(String.valueOf(i));
                        }
                        return speeds.stream()
                                .filter(speed -> speed.startsWith(args[2]))
                                .toList();
                    }
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
                    case "addtitle" -> {
                        // Ensure dynamic titles are populated
                        PlayerTitles.populateDynamicTitles();
                        return PlayerTitles.titles.keySet().stream()
                                .filter(title -> title.toLowerCase().startsWith(args[2].toLowerCase()))
                                .collect(Collectors.toList());
                    }
                    case "removetitle" -> {
                        Player targetPlayer = Bukkit.getPlayer(args[1]);
                        if (targetPlayer != null) {
                            return PlayerTitles.getPlayerTitles(targetPlayer).getTitles().stream()
                                    .filter(title -> title.toLowerCase().startsWith(args[2].toLowerCase()))
                                    .collect(Collectors.toList());
                        }
                        return List.of();
                    }
                    case "sudo" -> {
                        return getKnownCommands(getCommandMap()).keySet().stream()
                                .filter(cmd -> cmd.startsWith(args[2].toLowerCase()))
                                .collect(Collectors.toList());
                    }
                    default -> {
                        return List.of();
                    }
                }
            } else if (args.length == 4) {
                switch (args[0].toLowerCase()) {
                    case "enchant" -> {
                        if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("set")) {
                            try {
                                CustomEnchantment enchantment = CustomEnchantment.valueOf(args[2].toUpperCase());
                                List<String> levels = new ArrayList<>();
                                for (int i = 1; i <= enchantment.maxLevel; i++) {
                                    levels.add(String.valueOf(i));
                                }
                                return levels.stream()
                                        .filter(level -> level.startsWith(args[3]))
                                        .collect(Collectors.toList());
                            } catch (IllegalArgumentException e) {
                                return List.of();
                            }
                        }
                        return List.of();
                    }
                    case "relic" -> {
                        if (args[1].equalsIgnoreCase("set")) {
                            List<String> grades = new ArrayList<>();
                            for (int i = 1; i <= 10; i++) {
                                grades.add(String.valueOf(i));
                            }
                            return grades.stream()
                                    .filter(grade -> grade.startsWith(args[3]))
                                    .toList();
                        }
                        return List.of();
                    }
                    default -> {
                        return List.of();
                    }
                }
            } else if (args.length == 5) {
                switch (args[0].toLowerCase()) {
                    case "relic" -> {
                        if (args[1].equalsIgnoreCase("set")) {
                            List<String> resistances = new ArrayList<>();
                            for (int i = 1; i <= 100; i++) {
                                resistances.add(String.valueOf(i));
                            }
                            return resistances.stream()
                                    .filter(res -> res.startsWith(args[4]))
                                    .toList();
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
