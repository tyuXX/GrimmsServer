package org.gsdistance.grimmsServer.Commands.HomeCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Data.PlayerMetadata;

public class Homes {
    public static boolean SubCommand(Player player, String[] args) {
        PlayerMetadata meta = PlayerMetadata.getPlayerMetadata(player);
        if (meta.homes.isEmpty()) {
            player.sendMessage("You have no homes set.");
            return true;
        }
        player.sendMessage("Your homes: " + String.join(", ", meta.homes.keySet()));
        return true;
    }
}
