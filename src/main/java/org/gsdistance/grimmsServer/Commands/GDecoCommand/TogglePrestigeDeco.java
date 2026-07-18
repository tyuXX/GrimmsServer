package org.gsdistance.grimmsServer.Commands.GDecoCommand;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class TogglePrestigeDeco {
    public static boolean subCommand(Player player) {
        PlayerMetadata metadata = PlayerMetadata.getPlayerMetadata(player);
        PlayerStats playerStats = PlayerStats.getPlayerStats(player);

        int prestige = playerStats.getStat("prestige", Integer.class);

        if (prestige == 0) {
            player.sendMessage(ChatColor.RED + "You must have at least prestige 1 to use the prestige decoration.");
            return false;
        }

        metadata.showPrestigeDeco = !metadata.showPrestigeDeco;
        metadata.saveToPDS();

        if (metadata.showPrestigeDeco) {
            player.sendMessage(ChatColor.GREEN + "Prestige decoration enabled. Your chat messages will show [" + prestige + "] in dark purple.");
        } else {
            player.sendMessage(ChatColor.GREEN + "Prestige decoration disabled.");
        }

        return true;
    }
}
