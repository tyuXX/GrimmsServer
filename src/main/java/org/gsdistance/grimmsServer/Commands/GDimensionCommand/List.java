package org.gsdistance.grimmsServer.Commands.GDimensionCommand;

import org.bukkit.command.CommandSender;
import org.gsdistance.grimmsServer.Constructable.World.WorldConstructor;

public class List {
    public List() {
    }

    public static boolean subCommand(CommandSender player) {
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
