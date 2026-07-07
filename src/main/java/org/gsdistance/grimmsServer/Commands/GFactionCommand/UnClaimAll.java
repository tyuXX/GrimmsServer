package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.FactionRank;

public class UnClaimAll {
    public UnClaimAll() {
    }

    public static boolean subCommand(Player player) {
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        Faction faction = Faction.getFaction(playerMetadata.factionUUID);
        if (faction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return false;
        } else if (faction.getMemberRank(player.getUniqueId()) != FactionRank.LEADER) {
            player.sendMessage(ChatColor.RED + "You must be the leader of the faction to unclaim all land.");
            return false;
        } else {
            faction.unClaimAllChunks();
            player.sendMessage(ChatColor.GREEN + "You have successfully unclaimed all land for your faction.");
            return true;
        }
    }
}
