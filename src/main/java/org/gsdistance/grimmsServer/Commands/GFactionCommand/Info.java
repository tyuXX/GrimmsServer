package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.FactionRank;

import java.util.UUID;

public class Info {
    public static boolean subCommand(Player player) {
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        Faction faction = Faction.getFaction(playerMetadata.factionUUID);
        if (faction == null) {
            player.sendMessage("§cYou are not part of any faction.");
            return false;
        }
        for (Data<UUID, FactionRank> member : faction.members) {
            PlayerMetadata memberMetadata = PlayerMetadata.getOfflinePlayerMetadata(member.key);
            if (memberMetadata != null) {
                player.sendMessage("§aMember: " + memberMetadata.nickname + " - Rank: " + member.value.toString());
            } else {
                player.sendMessage("§cMember with UUID " + member.key + " not found.");
            }
        }
        player.sendMessage("§aFaction Name: " + faction.name);
        player.sendMessage("§aFaction UUID: " + faction.uuid);
        player.sendMessage("§aFaction Members Count: " + faction.members.size());
        player.sendMessage("§aFaction Claimed Chunks: " + faction.claims.size() + "/" + faction.getClaimLimit());
        return true;
    }
}
