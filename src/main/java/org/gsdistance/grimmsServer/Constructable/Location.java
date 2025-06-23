package org.gsdistance.grimmsServer.Constructable;

import java.util.Objects;

public class Location {
    public final double x;
    public final double y;
    public final double z;
    public final String world;

    public Location(org.bukkit.Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.world = Objects.requireNonNull(location.getWorld()).getName();
    }

    public org.bukkit.Location toBukkitLocation() {
        return new org.bukkit.Location(org.bukkit.Bukkit.getWorld(world), x, y, z);
    }
}
