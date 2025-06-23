package org.gsdistance.grimmsServer.Constructable;

import org.gsdistance.grimmsServer.GrimmsServer;

/**
 * @param generateStructures Default to true, can be set to false if needed
 */
public record WorldConstructor(String name, String type, boolean generateStructures) {

    public static WorldConstructor getWorldConstructor(String name) {
        return (WorldConstructor) GrimmsServer.pds.retrieveData(name, WorldConstructor.class, "worldConstructors");
    }

    public static WorldConstructor[] getAllWorldConstructors() {
        return (WorldConstructor[]) GrimmsServer.pds.retrieveAllData(WorldConstructor.class, "worldConstructors");
    }

    public static void saveWorldConstructor(WorldConstructor worldConstructor) {
        GrimmsServer.pds.saveData(worldConstructor, WorldConstructor.class, worldConstructor.name, "worldConstructors");
    }
}
