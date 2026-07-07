package org.gsdistance.grimmsServer.Constructable.World;

import org.bukkit.World;
import org.bukkit.WorldType;

public class WorldMetadata {
    public final String name;
    public final String worldType;
    public final long seed;
    public final int dimension;

    public WorldMetadata(World world) {
        this.name = world.getName();
        WorldType worldType = world.getWorldType();
        if (worldType == null) {
            throw new IllegalArgumentException("World type cannot be null");
        }
        this.worldType = worldType.getName();
        this.seed = world.getSeed();
        this.dimension = world.getEnvironment().getId();
    }
}
