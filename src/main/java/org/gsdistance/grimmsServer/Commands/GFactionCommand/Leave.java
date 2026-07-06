package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.FactionRank;

public class Leave {
   public Leave() {
   }

   public static boolean subCommand(Player player) {
      PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
      Faction faction = Faction.getFaction(playerMetadata.factionUUID);
      if (faction == null) {
         player.sendMessage("§cThe faction you are trying to leave does not exist.");
         return false;
      } else if (faction.getMemberRank(player.getUniqueId()) == FactionRank.LEADER) {
         faction.delete();
         player.sendMessage("§aYou have successfully disbanded the faction " + faction.name + ".");
         return false;
      } else {
         faction.removeMember(player.getUniqueId());
         playerMetadata.factionUUID = null;
         player.sendMessage("§aYou have successfully left the faction " + faction.name + ".");
         return true;
      }
   }
}
