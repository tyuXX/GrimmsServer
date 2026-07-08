package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;

public class Chat {
    public Chat() {
    }

    public static boolean subCommand(Player player) {
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        Faction faction = Faction.getFaction(playerMetadata.factionUUID);
        if (faction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return false;
        } else {
            playerMetadata.factionChatEnabled = !playerMetadata.factionChatEnabled;
            playerMetadata.saveToPDS();
            if (playerMetadata.factionChatEnabled) {
                player.sendMessage(ChatColor.GREEN + "Faction chat enabled.");
            } else {
                player.sendMessage(ChatColor.RED + "Faction chat disabled.");
            }
            return true;
        }
    }
}
