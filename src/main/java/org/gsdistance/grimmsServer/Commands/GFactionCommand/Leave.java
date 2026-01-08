package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.FactionRank;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Shared;

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
            faction.delete();
            // Remove the boss bar associated with the faction
            player.sendMessage("§aYou have successfully disbanded the faction " + faction.name + ".");
            return false;
        }
        faction.removeMember(player.getUniqueId());
        playerMetadata.factionUUID = null;
        player.sendMessage("§aYou have successfully left the faction " + faction.name + ".");
        return true;
    }
}
