package org.gsdistance.grimmsServer.Commands.GDecoCommand;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Stats.PlayerTitles;

public class SelectTitle {
    public SelectTitle() {
    }

    public static boolean subCommand(Player player, String title) {
        PlayerTitles playerTitles = PlayerTitles.getPlayerTitles(player);
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        if (title.isEmpty()) {
            playerMetadata.decoTitle = title;
            playerMetadata.saveToPDS();
            player.sendMessage(ChatColor.GREEN + "Successfully cleared deco title.");
            return true;
        } else if (playerTitles.hasTitle(title)) {
            playerMetadata.decoTitle = title;
            playerMetadata.saveToPDS();
            player.sendMessage(ChatColor.GREEN + "Successfully set deco title.");
            return true;
        } else {
            player.sendMessage(ChatColor.RED + "Failed to set deco title.");
            return false;
        }
    }
}
