package org.gsdistance.grimmsServer.Constructable;

import org.bukkit.World;

public class WorldMetadata {
    public String name;
    public String worldType;
    public long seed;
    public int dimension;

    public WorldMetadata(World world) {
        this.name = world.getName();
        this.worldType = world.getWorldType().getName();
        this.seed = world.getSeed();
        this.dimension = world.getEnvironment().getId();
    }
}
