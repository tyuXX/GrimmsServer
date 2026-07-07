package org.gsdistance.grimmsServer.Constructable;

import org.bukkit.Bukkit;
import org.bukkit.World;

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

    public Location(String world, double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public org.bukkit.Location toBukkitLocation() {
        return new org.bukkit.Location(Bukkit.getWorld(this.world), this.x, this.y, this.z);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && this.getClass() == obj.getClass()) {
            Location location = (Location) obj;
            return this.x == location.x && this.y == location.y && this.z == location.z && Objects.equals(this.world, location.world);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(this.world, this.x, this.y, this.z);
    }
}
