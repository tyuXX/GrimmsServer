package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.FactionRank;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class GFactionTabCompleter implements TabCompleter {
    private static final List<String> SUBCOMMANDS = Arrays.asList("invite", "join", "leave", "kick", "info", "new", "claim", "unclaim", "setrank", "unclaimall");

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return List.of();
        }

        if (args.length == 1) {
            // Suggest subcommands for the first argument
            return SUBCOMMANDS.stream()
                    .filter(subcommand -> subcommand.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "invite", "kick" -> {
                    // Suggest online player names for "invite" and "kick"
                    return Bukkit.getOnlinePlayers().stream()
                            .map(Player::getName)
                            .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                            .collect(Collectors.toList());
                }
                case "join" -> {
                    // Suggest faction names for "join"
                    return getFactionNames().stream()
                            .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                            .collect(Collectors.toList());
                }
                case "setrank" -> {
                    // Suggest faction members for "setrank"
                    return getFactionMembers((Player) sender).stream()
                            .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                            .collect(Collectors.toList());
                }
                default -> {
                    return List.of();
                }
            }
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("setrank")) {
            // Suggest valid ranks for "setrank"
            return Arrays.stream(FactionRank.values())
                    .map(FactionRank::toString)
                    .filter(rank -> rank.toLowerCase().startsWith(args[2].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return List.of();
    }

    private List<String> getFactionNames() {
        Faction[] factions = GrimmsServer.pds.retrieveAllData(Faction.class, "factions");
        if (factions == null) {
            return new ArrayList<>();
        }
        return Arrays.stream(factions)
                .map((Faction faction) -> faction.id)
                .filter(name -> name != null && !name.isEmpty())
                .collect(Collectors.toList());
    }

    private List<String> getFactionMembers(Player player) {
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        Faction faction = Faction.getFaction(playerMetadata.factionUUID);
        if (faction == null) {
            return List.of();
        }
        return faction.members.stream()
                .map(data -> data.key.toString()) // Convert UUID to string
                .map(uuidString -> {
                    try {
                        UUID uuid = UUID.fromString(uuidString);
                        Player factionMember = Bukkit.getPlayer(uuid);
                        return factionMember != null ? factionMember.getName() : null;
                    } catch (IllegalArgumentException e) {
                        return null; // Skip invalid UUIDs
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
