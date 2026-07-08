package org.gsdistance.grimmsServer.Events.Registers;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CustomEntityDeathRegister extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Entity entity;
    private final Entity killer;

    public CustomEntityDeathRegister(Entity entity, Entity killer) {
        this.entity = entity;
        this.killer = killer;
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

    public Entity getKiller() {
        return this.killer;
    }
}
