package org.gsdistance.grimmsServer.Commands.HomeCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Data.PlayerMetadata;
import org.gsdistance.grimmsServer.Constructable.Location;

public class Sethome {
    public static boolean SubCommand(Player player, String[] args) {
        String homeName = "home";
        if (args.length > 1) {
            homeName = args[1].toLowerCase();
        }
        PlayerMetadata meta = PlayerMetadata.getPlayerMetadata(player);

        // Check multiHome permission
        boolean hasMultiHome = player.hasPermission("grimmsserver.multiHome");
        if (!hasMultiHome) {
            // Only allow one home (named "home" or the first set)
            if (!meta.homes.isEmpty() && !meta.homes.containsKey(homeName)) {
                player.sendMessage("You do not have permission to set multiple homes.");
                return true;
            }
        }

        meta.homes.put(homeName, new Location(player.getLocation()));
        meta.saveToPDS();
        player.sendMessage("Home '" + homeName + "' set!");
        return true;
    }
}
