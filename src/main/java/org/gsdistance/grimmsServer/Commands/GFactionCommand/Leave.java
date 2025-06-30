package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.FactionRank;

import java.util.ArrayList;
import java.util.UUID;

public class Leave {
    public static boolean subCommand(Player player) {
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        Faction faction = Faction.getFaction(playerMetadata.factionUUID);
        if (faction == null) {
            player.sendMessage("§cThe faction you are trying to leave does not exist.");
            return false;
        }
        if (faction.getMemberRank(player.getUniqueId()) == FactionRank.LEADER) {
            // Disband the faction if the player is the leader
            for (Data<UUID, FactionRank> member : new ArrayList<>(faction.members)) {
                PlayerMetadata memberMetadata = PlayerMetadata.getOfflinePlayerMetadata(member.key);
                if (memberMetadata != null) {
                    memberMetadata.factionUUID = null;
                    memberMetadata.saveToPDS();
                }
            }
            faction.delete();
            player.sendMessage("§aYou have successfully disbanded the faction " + faction.name + ".");
            return false;
        }
        faction.removeMember(player.getUniqueId());
        playerMetadata.factionUUID = null;
        player.sendMessage("§aYou have successfully left the faction " + faction.name + ".");
        return true;
    }
}
