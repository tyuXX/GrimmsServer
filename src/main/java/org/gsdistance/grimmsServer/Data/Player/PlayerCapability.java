package org.gsdistance.grimmsServer.Data.Player;

public enum PlayerCapability {
    AUTOSELL("Auto Sell", "Allows the player to automatically sell items in their inventory when they reach a certain threshold.", "autosell");

    public final String displayName;
    public final String description;
    public final String capabilityId;
    PlayerCapability(String displayName, String description, String capabilityId) {
        this.displayName = displayName;
        this.description = description;
        this.capabilityId = capabilityId;
    }
}
