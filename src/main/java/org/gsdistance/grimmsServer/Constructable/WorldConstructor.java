package org.gsdistance.grimmsServer.Constructable;

import org.gsdistance.grimmsServer.GrimmsServer;

public record WorldConstructor(String name, String type, boolean generateStructures, String worldType, Long seed,
                               String generatorSettings) {

    public static WorldConstructor[] getAllWorldConstructors() {
        return GrimmsServer.pds.retrieveAllData(WorldConstructor.class, "worldConstructors");
    }

    public static void saveWorldConstructor(WorldConstructor worldConstructor) {
        GrimmsServer.pds.saveData(worldConstructor, WorldConstructor.class, worldConstructor.name + ".json", "worldConstructors");
    }
}
