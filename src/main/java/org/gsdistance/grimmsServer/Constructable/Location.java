package org.gsdistance.grimmsServer.Constructable;

public class Location {
    public double x;
    public double y;
    public double z;
    public String world;

    public Location(org.bukkit.Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.world = location.getWorld().getName();
    }

    public org.bukkit.Location toBukkitLocation() {
        return new org.bukkit.Location(org.bukkit.Bukkit.getWorld(world), x, y, z);
    }
}
