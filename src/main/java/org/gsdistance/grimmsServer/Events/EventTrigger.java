package org.gsdistance.grimmsServer.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventTrigger implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        org.gsdistance.grimmsServer.Events.EntityDeathEvent.Event(event);
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        org.gsdistance.grimmsServer.Events.PlayerJoinEvent.Event(event);
    }
}
