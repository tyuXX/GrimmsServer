package org.gsdistance.grimmsServer.Indexers;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.gsdistance.grimmsServer.Constructable.WorldConstructor;
import org.gsdistance.grimmsServer.GrimmsServer;

public class DynamicDimensionGen {
    public static World newDimension(WorldConstructor worldConstructor) {
        if (Bukkit.getWorld(worldConstructor.name) != null) {
            return Bukkit.getWorld(worldConstructor.name);
        } else {
            // Create a new world with the specified name and type
            WorldCreator worldCreator = new WorldCreator(worldConstructor.name);
            try {
                worldCreator.environment(World.Environment.valueOf(worldConstructor.type));
            } catch (EnumConstantNotPresentException e) {
                // If the environment type is not recognized, default to NORMAL
                worldCreator.environment(World.Environment.NORMAL);
            }
            worldCreator.generateStructures(worldConstructor.generateStructures);
            return worldCreator.createWorld();
        }
    }

    public static void loadWorlds() {
        GrimmsServer.logger.info("Loading worlds from worldConstructors...");
        WorldConstructor[] worldConstructors = WorldConstructor.getAllWorldConstructors();
        if (worldConstructors == null || worldConstructors.length == 0) {
            GrimmsServer.logger.warning("No world constructors found to load.");
            return;
        }
        for (WorldConstructor worldConstructor : worldConstructors) {
            if (Bukkit.getWorld(worldConstructor.name) == null) {
                newDimension(worldConstructor);
            }
        }
    }
}
