package org.gsdistance.grimmsServer.Indexers;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.gsdistance.grimmsServer.Constructable.WorldConstructor;
import org.gsdistance.grimmsServer.GrimmsServer;

public class DynamicDimensionGen {
    public static void newDimension(WorldConstructor worldConstructor) {
        GrimmsServer.logger.info("Creating new world: " + worldConstructor.name() + " with type: " + worldConstructor.type());
        if (Bukkit.getWorld(worldConstructor.name()) != null) {
            GrimmsServer.logger.info("World with name '" + worldConstructor.name() + "' already exists.");
            Bukkit.getWorld(worldConstructor.name());
        } else {
            // Create a new world with the specified name and type
            WorldCreator worldCreator = new WorldCreator(worldConstructor.name());
            try {
                worldCreator.environment(World.Environment.valueOf(worldConstructor.type()));
            } catch (EnumConstantNotPresentException e) {
                // If the environment type is not recognized, default to NORMAL
                worldCreator.environment(World.Environment.NORMAL);
            }
            worldCreator.generateStructures(worldConstructor.generateStructures());
            try {
                worldCreator.type(WorldType.valueOf(worldConstructor.worldType()));
            } catch (EnumConstantNotPresentException e) {
                // If the world type is not recognized, default to NORMAL
                worldCreator.type(WorldType.NORMAL);
            }
            GrimmsServer.logger.info("World created with name: " + worldCreator.name() + ", type: " + worldCreator.type() + ", environment: " + worldCreator.environment());
            worldCreator.createWorld();
        }
    }

    public static void loadWorlds() {
        GrimmsServer.logger.info("Loading worlds from worldConstructors...");
        WorldConstructor[] worldConstructors = WorldConstructor.getAllWorldConstructors();
        if (worldConstructors.length == 0) {
            GrimmsServer.logger.warning("No world constructors found to load.");
            return;
        }
        for (WorldConstructor worldConstructor : worldConstructors) {
            if (Bukkit.getWorld(worldConstructor.name()) == null) {
                newDimension(worldConstructor);
            }
        }
    }
}
