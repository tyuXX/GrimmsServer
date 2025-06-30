package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.PlayerMetadata;

public class Claim {
    public static boolean subCommand(Player player){
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        Faction faction = Faction.getFaction(playerMetadata.factionUUID);
        if (faction == null) {
            player.sendMessage("You are not in a faction.");
            return false;
        }
        return faction.claimChunk(player.getLocation(), player);
    }
}
