package org.gsdistance.grimmsServer.Constructable;

import org.bukkit.generator.BiomeProvider;
import org.gsdistance.grimmsServer.GrimmsServer;

import javax.annotation.Nullable;

public record WorldConstructor(String name, String type, boolean generateStructures, String worldType) {

    public static WorldConstructor getWorldConstructor(String name) {
        return GrimmsServer.pds.retrieveData(name + ".json", "worldConstructors", WorldConstructor.class);
    }

    public static WorldConstructor[] getAllWorldConstructors() {
        Object[] rawData = GrimmsServer.pds.retrieveAllData(WorldConstructor.class, "worldConstructors");
        if (rawData == null) {
            return new WorldConstructor[0];
        }
        WorldConstructor[] worldConstructors = new WorldConstructor[rawData.length];
        for (int i = 0; i < rawData.length; i++) {
            worldConstructors[i] = (WorldConstructor) rawData[i];
        }
        return worldConstructors;
    }

    public static void saveWorldConstructor(WorldConstructor worldConstructor) {
        GrimmsServer.pds.saveData(worldConstructor, WorldConstructor.class, worldConstructor.name + ".json", "worldConstructors");
    }
}
