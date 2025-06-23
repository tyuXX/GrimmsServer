package org.gsdistance.grimmsServer.Commands.HomeCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Location;
import org.gsdistance.grimmsServer.Constructable.PlayerMetadata;

public class TpHome {
    @SuppressWarnings("SameReturnValue")
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
        Location loc = meta.homes.get(homeName);
        player.teleport(loc.toBukkitLocation());
        player.sendMessage("Teleported to home '" + homeName + "'.");
        return true;
    }
}
