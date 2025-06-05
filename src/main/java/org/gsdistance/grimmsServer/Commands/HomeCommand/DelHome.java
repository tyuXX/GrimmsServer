package org.gsdistance.grimmsServer.Commands.HomeCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Data.PlayerMetadata;

public class DelHome {
    public static boolean SubCommand(Player player, String[] args) {
        String homeName = "home";
        if (args.length > 1) {
            homeName = args[1].toLowerCase();
        }
        PlayerMetadata meta = PlayerMetadata.getPlayerMetadata(player);
        if (!meta.homes.containsKey(homeName)) {
            player.sendMessage("Home '" + homeName + "' does not exist.");
            return true;
        }
        meta.homes.remove(homeName);
        meta.saveToPDS();
        player.sendMessage("Home '" + homeName + "' deleted.");
        return true;
    }
}
