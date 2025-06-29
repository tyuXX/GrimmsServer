package org.gsdistance.grimmsServer.Indexers;

import com.google.common.base.Stopwatch;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.gsdistance.grimmsServer.Config.ConfigKey;
import org.gsdistance.grimmsServer.Constructable.WorldConstructor;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.List;

import static org.gsdistance.grimmsServer.Config.ActiveConfig.getConfigValue;

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
            if (worldConstructor.seed() != null) {
                worldCreator.seed(worldConstructor.seed());
            }
            if(worldConstructor.generatorSettings() != null && !worldConstructor.generatorSettings().isEmpty()) {
                worldCreator.generatorSettings(worldConstructor.generatorSettings());

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

    public static void unLoadWorlds() {
        Stopwatch sw = Stopwatch.createStarted();
        GrimmsServer.logger.info("Unloading worlds...");
        for (String worldName : (List<String>) getConfigValue(ConfigKey.DISABLED_DIMENSIONS, List.class)) {
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                GrimmsServer.logger.info("Unloading world: " + worldName);
                Bukkit.unloadWorld(world, false);
            } else {
                GrimmsServer.logger.warning("World with name '" + worldName + "' does not exist or is already unloaded.");
            }
        }
        GrimmsServer.logger.info("Unloaded worlds (" + sw.stop() + ")");
    }
}
