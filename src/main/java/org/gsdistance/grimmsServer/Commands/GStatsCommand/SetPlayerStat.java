package org.gsdistance.grimmsServer.Commands.GStatsCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.jetbrains.annotations.NotNull;

public class SetPlayerStat implements CommandExecutor {
    public SetPlayerStat() {
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (args.length < 3) {
                return false;
            } else {
                Player targetPlayer = GrimmsServer.instance.getServer().getPlayer(args[0]);
                if (targetPlayer == null) {
                    sender.sendMessage("Player not found.");
                    return false;
                } else {
                    PlayerStats playerStats = PlayerStats.getPlayerStats(targetPlayer);
                    String stat = args[1];
                    String value = args[2];
                    PersistentDataType<?, ?> dataType = PlayerStats.Stats.get(stat);
                    if (dataType == null) {
                        sender.sendMessage("Invalid stat name: " + stat);
                        return false;
                    } else {
                        Object dataValue;
                        if (dataType.equals(PersistentDataType.INTEGER)) {
                            try {
                                dataValue = Integer.parseInt(value);
                            } catch (NumberFormatException var15) {
                                sender.sendMessage("Invalid value for stat " + stat + ". Expected an integer.");
                                return false;
                            }
                        } else if (dataType.equals(PersistentDataType.DOUBLE)) {
                            try {
                                dataValue = Double.parseDouble(value);
                            } catch (NumberFormatException var14) {
                                sender.sendMessage("Invalid value for stat " + stat + ". Expected a double.");
                                return false;
                            }
                        } else if (dataType.equals(PersistentDataType.LONG)) {
                            try {
                                dataValue = Long.parseLong(value);
                            } catch (NumberFormatException var13) {
                                sender.sendMessage("Invalid value for stat " + stat + ". Expected a long.");
                                return false;
                            }
                        } else if (dataType.equals(PersistentDataType.BOOLEAN)) {
                            try {
                                dataValue = Boolean.parseBoolean(value);
                            } catch (IllegalArgumentException var12) {
                                sender.sendMessage("Invalid value for stat " + stat + ". Expected a boolean.");
                                return false;
                            }
                        } else {
                            if (!dataType.equals(PersistentDataType.STRING)) {
                                sender.sendMessage("Invalid stat type for " + stat + ".");
                                return false;
                            }

                            dataValue = value;
                        }

                        if (dataValue != null) {
                            playerStats.setStat(stat, dataValue);
                            sender.sendMessage("Set " + stat + " to " + dataValue + " for player " + targetPlayer.getName());
                            return true;
                        } else {
                            sender.sendMessage("Failed to set stat.");
                            return false;
                        }
                    }
                }
            }
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}
