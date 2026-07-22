package org.gsdistance.grimmsServer.Data.Player;

public enum PlayerCapability {
    AUTOSELL("Auto Sell", "Allows the player to automatically sell items in their inventory when they reach a certain threshold.", "autosell"),
    MAGNET("Magnet", "Magnetises items in a 10 block radius", "magnet"),
    VEINMINER("Veinminer", "Breaks all adjacent ores of the same type within 8 blocks and teleports drops to you.", "veinminer"),
    SATURATION_PERK("Saturation Perk", "Gives 10 seconds of saturation that refreshes every second when enabled.", "saturation_perk");

    public final String displayName;
    public final String description;
    public final String capabilityId;

    PlayerCapability(String displayName, String description, String capabilityId) {
        this.displayName = displayName;
        this.description = description;
        this.capabilityId = capabilityId;
    }
}
