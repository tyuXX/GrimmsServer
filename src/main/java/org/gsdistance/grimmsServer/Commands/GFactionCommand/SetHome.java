package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.Location;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.FactionRank;

public class SetHome {
    public SetHome() {
    }

    public static boolean subCommand(Player player) {
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        Faction faction = Faction.getFaction(playerMetadata.factionUUID);
        if (faction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return false;
        } else if (faction.getMemberRank(player.getUniqueId()).weight < FactionRank.OFFICER.weight) {
            player.sendMessage(ChatColor.RED + "You must be an officer or higher to set the faction home.");
            return false;
        } else {
            faction.homeLocation = new Location(player.getLocation());
            faction.saveToFile();
            player.sendMessage(ChatColor.GREEN + "Faction home set to your current location.");
            return true;
        }
    }
}
