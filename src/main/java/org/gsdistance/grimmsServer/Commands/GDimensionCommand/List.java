package org.gsdistance.grimmsServer.Commands.GDimensionCommand;

import org.gsdistance.grimmsServer.Constructable.WorldConstructor;

public class List {
    public static boolean subCommand(org.bukkit.entity.Player player) {
        player.sendMessage("Constructed Worlds:");
        for (WorldConstructor worldConstructor : WorldConstructor.getAllWorldConstructors()) {
            String worldName = worldConstructor.name();
            String type = worldConstructor.type();
            boolean generateStructures = worldConstructor.generateStructures();
            player.sendMessage("|World: " + worldName + ", Type: " + type + ", Generate Structures: " + generateStructures);
        }
        return true;
    }
}
