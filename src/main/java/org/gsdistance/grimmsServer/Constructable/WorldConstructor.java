package org.gsdistance.grimmsServer.Constructable;

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
}
