package org.gsdistance.grimmsServer.Commands.GDimensionCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.WorldConstructor;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Indexers.DynamicDimensionGen;

import java.util.Arrays;

public class Create {
    public static boolean subCommand(Player player, String[] args) {
        try {
            WorldConstructor constructor = new WorldConstructor(
                    args[1],
                    args.length > 2 ? args[2] : "NORMAL",
                    args.length > 3 && Boolean.parseBoolean(args[3]),
                    args.length > 4 ? args[4].toUpperCase() : "NORMAL",
                    args.length > 5 ? Long.parseLong(args[5]) : null,
                    args.length > 6 ? args[6] : null
            );
            DynamicDimensionGen.newDimension(constructor);
            player.sendMessage("World '" + constructor.name() + "' has been created successfully.");
            player.sendMessage("Keep in mind that every world has separate game rules, so you might need to set them up again.");
            WorldConstructor.saveWorldConstructor(constructor);
        } catch (Exception e) {
            player.sendMessage("Error creating world: " + e.getMessage());
            GrimmsServer.logger.warning(Arrays.toString(e.getStackTrace()));
            return false;
        }
        return true;
    }
}
