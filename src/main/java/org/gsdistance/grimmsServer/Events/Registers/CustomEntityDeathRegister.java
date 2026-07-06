package org.gsdistance.grimmsServer.Events.Registers;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CustomEntityDeathRegister extends Event {
   private static final HandlerList handlers = new HandlerList();
   private final Entity entity;

   public CustomEntityDeathRegister(Entity entity) {
      this.entity = entity;
   }

   @NotNull
   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public Entity getEntity() {
      return this.entity;
   }
}
