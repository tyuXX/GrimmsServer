package org.gsdistance.grimmsServer.Events.Registers;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CustomEntityDamageByEntityRegister extends Event {
   private static final HandlerList handlers = new HandlerList();

   public CustomEntityDamageByEntityRegister() {
   }

   @NotNull
   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }
}
