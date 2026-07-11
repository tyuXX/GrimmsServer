package org.gsdistance.grimmsServer.Commands.HomeCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Location;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class Sethome {
    public Sethome() {
    }

    public static boolean SubCommand(Player player, String[] args) {
        String homeName = "home";
        if (args.length > 1) {
            homeName = args[1].toLowerCase();
        }

        PlayerMetadata meta = PlayerMetadata.getPlayerMetadata(player);
        PlayerStats playerStats = PlayerStats.getPlayerStats(player);
        boolean hasMultiHome = player.hasPermission("grimmsserver.multiHome");
        if (!hasMultiHome && (long) meta.homes.size() >= Math.round(Math.cbrt((double) playerStats.getStat("level", Integer.class))) + playerStats.getStat("prestige", Integer.class) && !meta.homes.containsKey(homeName)) {
            player.sendMessage("You do not have permission to set multiple homes and your level isn't enough.");
            return true;
        } else {
            meta.homes.put(homeName, new Location(player.getLocation()));
            meta.saveToPDS();
            player.sendMessage("Home '" + homeName + "' set!");
            return true;
        }
    }
}
