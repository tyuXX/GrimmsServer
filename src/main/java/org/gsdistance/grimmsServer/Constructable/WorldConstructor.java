package org.gsdistance.grimmsServer.Constructable;

import org.gsdistance.grimmsServer.GrimmsServer;

public class WorldConstructor {
    public String name;
    public String type;

    public boolean generateStructures; // Default to true, can be set to false if needed
    // Add other properties as needed


    public WorldConstructor(String name, String type, boolean generateStructures) {
        this.name = name;
        this.type = type;
        this.generateStructures = generateStructures;
    }

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
