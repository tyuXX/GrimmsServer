package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.FactionRank;

import java.util.Arrays;

public class SetRank {
    public static boolean subCommand(Player player, String[] args) {
        if (args.length < 3) {
            return false;
        }
        Player targetPlayer = Bukkit.getPlayer(args[1]);
        if (targetPlayer == null) {
            player.sendMessage("§cThe player you are trying to set the rank for is not found.");
            return false;
        }
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        Faction faction = Faction.getFaction(playerMetadata.factionUUID);
        if (faction == null) {
            player.sendMessage("§cYou are not a member of any faction.");
            return false;
        }
        if (!faction.isMember(targetPlayer.getUniqueId())) {
            player.sendMessage("§cThe player is not a member of your faction.");
            return false;
        }
        if (faction.getMemberRank(player.getUniqueId()).weight <= faction.getMemberRank(targetPlayer.getUniqueId()).weight) {
            player.sendMessage("§cYou cannot set the rank of a player with the same or higher rank.");
            return false;
        }
        FactionRank newRank = FactionRank.fromString(args[2]);
        if (newRank == null) {
            player.sendMessage("§cInvalid rank specified. Valid ranks are: " + Arrays.stream(FactionRank.values()).map(FactionRank::toString));
            return false;
        }
        faction.removeMember(targetPlayer.getUniqueId());
        faction.addMember(targetPlayer.getUniqueId(), newRank);
        return true;
    }
}
