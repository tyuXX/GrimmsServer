package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.FactionRank;

public class UnClaim {
    public static boolean subCommand(Player player){
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        Faction faction = Faction.getFaction(playerMetadata.factionUUID);
        if (faction == null) {
            player.sendMessage("You are not in a faction.");
            return false;
        }
        return faction.unClaimChunk(player.getLocation(), player);
    }
}
