package org.gsdistance.grimmsServer.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventTrigger implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        org.gsdistance.grimmsServer.Events.EntityDeathEvent.Event(event);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        org.gsdistance.grimmsServer.Events.BlockBreakEvent.Event(event);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        org.gsdistance.grimmsServer.Events.PlayerJoinEvent.Event(event);
    }
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        org.gsdistance.grimmsServer.Events.EntityDamageEvent.Event(event);
    }
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        org.gsdistance.grimmsServer.Events.EntityDamageByEntityEvent.Event(event);
    }
}
