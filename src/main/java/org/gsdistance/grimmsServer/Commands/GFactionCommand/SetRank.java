package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.FactionRank;

public class SetRank {

   public SetRank() {
   }

   public static boolean subCommand(Player player, String[] args) {
      // Guard: Ensure minimum argument length
      if (args.length < 3) {
         return false;
      }

      // Guard: Check if target player is online
      Player targetPlayer = Bukkit.getPlayer(args[1]);
      if (targetPlayer == null) {
         player.sendMessage("§cThe player you are trying to set the rank for is not found.");
         return false;
      }

      // Guard: Check if command executor is in a faction
      PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
      Faction faction = Faction.getFaction(playerMetadata.factionUUID);
      if (faction == null) {
         player.sendMessage("§cYou are not a member of any faction.");
         return false;
      }

      // Guard: Check if target player belongs to the same faction
      if (!faction.isMember(targetPlayer.getUniqueId())) {
         player.sendMessage("§cThe player is not a member of your faction.");
         return false;
      }

      // Guard: Hierarchy check (Cannot alter ranks of peers or superiors)
      if (faction.getMemberRank(player.getUniqueId()).weight <= faction.getMemberRank(targetPlayer.getUniqueId()).weight) {
         player.sendMessage("§cYou cannot set the rank of a player with the same or higher rank.");
         return false;
      }

      // Guard: Validate the newly requested rank
      FactionRank newRank = FactionRank.fromString(args[2]);
      if (newRank == null) {
         // FIXED: Correctly collect the stream into a clean comma-separated string
         String validRanks = Arrays.stream(FactionRank.values())
                 .map(FactionRank::toString)
                 .collect(Collectors.joining(", "));

         player.sendMessage("§cInvalid rank specified. Valid ranks are: " + validRanks);
         return false;
      }

      // Process rank update
      faction.removeMember(targetPlayer.getUniqueId());
      faction.addMember(targetPlayer.getUniqueId(), newRank);
      return true;
   }
}