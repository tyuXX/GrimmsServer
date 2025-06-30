package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.FactionRank;

public class UnClaimAll {
    public static boolean subCommand(Player player) {
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        Faction faction = Faction.getFaction(playerMetadata.factionUUID);
        if (faction == null) {
            player.sendMessage("§cYou are not in a faction.");
            return false;
        }
        if (faction.getMemberRank(player.getUniqueId()) != FactionRank.LEADER) {
            player.sendMessage("§cYou must be the leader of the faction to unclaim all land.");
            return false;
        }
        faction.unClaimAllChunks();
        player.sendMessage("§aYou have successfully unclaimed all land for your faction.");
        return true;
    }
}
