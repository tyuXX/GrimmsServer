package org.gsdistance.grimmsServer.Manage;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.gsdistance.grimmsServer.Events.PlayerChatEvent;
import org.gsdistance.grimmsServer.Events.PlayerCommandPreprocessEvent;

public class EventRegistry implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        org.gsdistance.grimmsServer.Events.EntityDeathEvent.Event(event);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        org.gsdistance.grimmsServer.Events.BlockBreakEvent.Event(event);
    }

    @EventHandler
    public void onBlockPlace(org.bukkit.event.block.BlockPlaceEvent event) {
        org.gsdistance.grimmsServer.Events.BlockPlaceEvent.Event(event);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        org.gsdistance.grimmsServer.Events.PlayerJoinEvent.Event(event);
    }

    @EventHandler
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        org.gsdistance.grimmsServer.Events.PlayerQuitEvent.Event(event);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        org.gsdistance.grimmsServer.Events.EntityDamageEvent.Event(event);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        org.gsdistance.grimmsServer.Events.EntityDamageByEntityEvent.Event(event);
    }

    @EventHandler
    public void onChatUse(AsyncPlayerChatEvent event) {
        PlayerChatEvent.Event(event);
    }


    @EventHandler
    public void onCommandUse(org.bukkit.event.player.PlayerCommandPreprocessEvent event) {
        PlayerCommandPreprocessEvent.Event(event);
    }

    @EventHandler
    public void onChunkLoad(org.bukkit.event.world.ChunkLoadEvent event) {
        org.gsdistance.grimmsServer.Events.ChunkLoadEvent.Event(event);
    }

    @EventHandler
    public void onWorldLoad(org.bukkit.event.world.WorldLoadEvent event) {
        org.gsdistance.grimmsServer.Events.WorldLoadEvent.Event(event);
    }

    @EventHandler
    public void onWorldSave(org.bukkit.event.world.WorldSaveEvent event) {
        org.gsdistance.grimmsServer.Events.OnWorldSaveEvent.Event(event);
    }
}
