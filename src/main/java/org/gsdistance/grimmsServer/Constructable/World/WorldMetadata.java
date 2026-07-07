package org.gsdistance.grimmsServer.Constructable.World;

import org.bukkit.World;

public class WorldMetadata {
    public final String name;
    public final String worldType;
    public final long seed;
    public final int dimension;

    public WorldMetadata(World world) {
        this.name = world.getName();
        this.worldType = world.getWorldType().getName();
        this.seed = world.getSeed();
        this.dimension = world.getEnvironment().getId();
    }
}
