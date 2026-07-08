package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;

public class Home {
    public Home() {
    }

    public static boolean subCommand(Player player) {
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        Faction faction = Faction.getFaction(playerMetadata.factionUUID);
        if (faction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return false;
        } else if (faction.homeLocation == null) {
            player.sendMessage(ChatColor.RED + "Your faction does not have a home set.");
            return false;
        } else {
            player.teleport(faction.homeLocation.toBukkitLocation());
            player.sendMessage(ChatColor.GREEN + "Teleported to faction home.");
            return true;
        }
    }
}
