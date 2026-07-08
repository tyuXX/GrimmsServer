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
    private static final List<String> SUBCOMMANDS = Arrays.asList("invite", "join", "leave", "kick", "info", "new", "claim", "unclaim", "setrank", "unclaimall", "sethome", "home", "chat", "showclaims");

    public GFactionTabCompleter() {
    }

    @Nullable
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return List.of();
        } else if (args.length == 1) {
            return SUBCOMMANDS.stream().filter((subcommand) -> subcommand.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        } else if (args.length == 2) {
            return switch (args[0].toLowerCase()) {
                case "invite", "kick" ->
                        Bukkit.getOnlinePlayers().stream().map(Player::getName).filter((name) -> name.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
                case "join" ->
                        this.getFactionNames().stream().filter((name) -> name.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
                case "setrank" ->
                        this.getFactionMembers((Player) sender).stream().filter((name) -> name.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
                default -> List.of();
            };
        } else {
            return args.length == 3 && args[0].equalsIgnoreCase("setrank") ? Arrays.stream(FactionRank.values()).map(FactionRank::toString).filter((rank) -> rank.toLowerCase().startsWith(args[2].toLowerCase())).collect(Collectors.toList()) : List.of();
        }
    }

    private List<String> getFactionNames() {
        Faction[] factions = GrimmsServer.pds.retrieveAllData(Faction.class, "factions");
        return (List<String>) (factions == null ? new ArrayList() : (List) Arrays.stream(factions).map((faction) -> faction.id).filter((name) -> name != null && !name.isEmpty()).collect(Collectors.toList()));
    }

    private List<String> getFactionMembers(Player player) {
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        Faction faction = Faction.getFaction(playerMetadata.factionUUID);
        return faction == null ? List.of() : faction.members.stream().map((data) -> data.key().toString()).map((uuidString) -> {
            try {
                UUID uuid = UUID.fromString(uuidString);
                Player factionMember = Bukkit.getPlayer(uuid);
                return factionMember != null ? factionMember.getName() : null;
            } catch (IllegalArgumentException var3) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
