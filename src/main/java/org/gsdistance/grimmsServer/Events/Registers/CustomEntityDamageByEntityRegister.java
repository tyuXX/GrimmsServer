package org.gsdistance.grimmsServer.Events.Registers;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CustomEntityDamageByEntityRegister extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final org.bukkit.event.entity.EntityDamageByEntityEvent originalEvent;

    public CustomEntityDamageByEntityRegister(org.bukkit.event.entity.EntityDamageByEntityEvent originalEvent) {
        this.originalEvent = originalEvent;
    }

    public org.bukkit.event.entity.EntityDamageByEntityEvent getOriginalEvent() {
        return originalEvent;
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
