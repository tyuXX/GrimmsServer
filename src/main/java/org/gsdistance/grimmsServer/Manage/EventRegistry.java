package org.gsdistance.grimmsServer.Manage;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.gsdistance.grimmsServer.Events.Listeners.*;
import org.gsdistance.grimmsServer.GrimmsServer;

public class EventRegistry implements Listener {
    public static void callEvent(Event event) {
        GrimmsServer.instance.getServer().getPluginManager().callEvent(event);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.EntityDeathEvent.Event(event);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.BlockBreakEvent.Event(event);
    }

    @EventHandler
    public void onBlockPlace(org.bukkit.event.block.BlockPlaceEvent event) {
        BlockPlaceEvent.Event(event);
    }

    @EventHandler
    public void onPlayerInteract(org.bukkit.event.player.PlayerInteractEvent event) {
        PlayerInteractEvent.Event(event);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.PlayerJoinEvent.Event(event);
    }

    @EventHandler
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        PlayerQuitEvent.Event(event);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.EntityDamageEvent.Event(event);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.EntityDamageByEntityEvent.Event(event);
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
        ChunkLoadEvent.Event(event);
    }

    @EventHandler
    public void onWorldLoad(org.bukkit.event.world.WorldLoadEvent event) {
        WorldLoadEvent.Event(event);
    }

    @EventHandler
    public void onWorldSave(org.bukkit.event.world.WorldSaveEvent event) {
        OnWorldSaveEvent.Event(event);
    }

    @EventHandler
    public void PlayerLevelUp(org.gsdistance.grimmsServer.Events.Registers.PlayerLevelUpEvent event) {
        PlayerLevelUpEvent.Event(event);
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.EntityPickupItemEvent.Event(event);
    }

    @EventHandler
    public void onPlayerItemDamage(org.bukkit.event.player.PlayerItemDamageEvent event) {
        PlayerItemDamageEvent.Event(event);
    }
}
