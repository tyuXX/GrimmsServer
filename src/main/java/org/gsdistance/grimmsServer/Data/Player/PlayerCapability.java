package org.gsdistance.grimmsServer.Data.Player;

public enum PlayerCapability {
   AUTOSELL("Auto Sell", "Allows the player to automatically sell items in their inventory when they reach a certain threshold.", "autosell"),
   MAGNET("Magnet", "Magnetises items in a 10 block radius", "magnet");

   public final String displayName;
   public final String description;
   public final String capabilityId;

   private PlayerCapability(String displayName, String description, String capabilityId) {
      this.displayName = displayName;
      this.description = description;
      this.capabilityId = capabilityId;
   }

   // $FF: synthetic method
   private static PlayerCapability[] $values() {
      return new PlayerCapability[]{AUTOSELL, MAGNET};
   }
}
