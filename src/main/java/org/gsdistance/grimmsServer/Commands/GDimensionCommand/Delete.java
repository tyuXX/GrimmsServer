package org.gsdistance.grimmsServer.Commands.GDimensionCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;

public class Delete {
    public static boolean subCommand(Player player, String[] args) {
        if (args.length < 2) {
            return false;
        }
        String worldName = args[1];
        if (GrimmsServer.instance.getServer().getWorlds().stream().noneMatch(world -> world.getName().equalsIgnoreCase(worldName))) {
            player.sendMessage("World '" + worldName + "' does not exist.");
            return false;
        }

        GrimmsServer.instance.getServer().unloadWorld(worldName, true);
        GrimmsServer.pds.deleteData(worldName + ".json", "worldConstructors");
        player.sendMessage("World '" + worldName + "' has been deleted.");
        return true;
    }
}
