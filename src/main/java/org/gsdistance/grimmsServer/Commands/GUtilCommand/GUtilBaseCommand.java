package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GUtilBaseCommand implements CommandExecutor {
    public GUtilBaseCommand() {
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            return false;
        } else {
            switch (args[0].toLowerCase()) {
                case "version" -> {
                    return Version.subCommand(sender, args);
                }
                case "relic" -> {
                    if (sender instanceof Player player) {
                        return Relic.subCommand(player, args);
                    }
                    return false;
                }
                case "enchant" -> {
                    if (sender instanceof Player player) {
                        return Enchant.subCommand(player, args);
                    }
                    return false;
                }
                case "givecustomitem" -> {
                    if (sender instanceof Player player) {
                        return GiveCustomItem.subCommand(player, args);
                    }
                    return false;
                }
                case "capability" -> {
                    if (sender instanceof Player player) {
                        return Capability.subCommand(player, args);
                    }
                    return false;
                }
                case "setting" -> {
                    if (sender instanceof Player player) {
                        return Setting.subCommand(player, args);
                    }
                    return false;
                }
                case "broadcast" -> {
                    return Broadcast.subCommand(sender, args);
                }
                case "inventoryrestore" -> {
                    if (sender instanceof Player player) {
                        return InventoryRestore.subCommand(player, args);
                    }
                    return false;
                }
                case "fly" -> {
                    if (sender instanceof Player player) {
                        return Fly.subCommand(player, args);
                    }
                    return false;
                }
                case "god" -> {
                    if (sender instanceof Player player) {
                        return God.subCommand(player, args);
                    }
                    return false;
                }
                case "heal" -> {
                    if (sender instanceof Player player) {
                        return Heal.subCommand(player, args);
                    }
                    return false;
                }
                case "spawn" -> {
                    if (sender instanceof Player player) {
                        return Spawn.subCommand(player, args);
                    }
                    return false;
                }
                case "speed" -> {
                    if (sender instanceof Player player) {
                        return Speed.subCommand(player, args);
                    }
                    return false;
                }
                case "enderchest" -> {
                    if (sender instanceof Player player) {
                        return Enderchest.subCommand(player, args);
                    }
                    return false;
                }
                case "invsee" -> {
                    if (sender instanceof Player player) {
                        return Invsee.subCommand(player, args);
                    }
                    return false;
                }
                case "addtitle" -> {
                    return AddTitle.subCommand(sender, args);
                }
                case "removetitle" -> {
                    return RemoveTitle.subCommand(sender, args);
                }
                case "unlevelentity" -> {
                    if (sender instanceof Player player) {
                        return UnlevelEntity.subCommand(player, args);
                    }
                    return false;
                }
                case "levelentity" -> {
                    if (sender instanceof Player player) {
                        return LevelEntity.subCommand(player, args);
                    }
                    return false;
                }
                case "sudo" -> {
                    return Sudo.subCommand(sender, args);
                }
                default -> {
                    return false;
                }
            }
        }
    }
}
