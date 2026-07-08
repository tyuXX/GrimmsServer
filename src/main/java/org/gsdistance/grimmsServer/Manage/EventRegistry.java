package org.gsdistance.grimmsServer.Manage;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.gsdistance.grimmsServer.Commands.MarketComand.MarketGUIListener;
import org.gsdistance.grimmsServer.Events.Listeners.*;
import org.gsdistance.grimmsServer.Events.Listeners.PlayerChatEvent;
import org.gsdistance.grimmsServer.Events.Registers.CustomEntityDamageByEntityRegister;
import org.gsdistance.grimmsServer.Events.Registers.CustomEntityDeathRegister;
import org.gsdistance.grimmsServer.Events.Registers.PlayerLevelUpRegister;
import org.gsdistance.grimmsServer.GrimmsServer;

public class EventRegistry implements Listener {
    public EventRegistry() {
    }

    public static void callEvent(Event event) {
        GrimmsServer.instance.getServer().getPluginManager().callEvent(event);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.EntityDeathEvent.Event(event);
        if (event.getEntity().getType() != org.bukkit.entity.EntityType.PLAYER) {
            callEvent(new CustomEntityDeathRegister(event.getEntity(), event.getEntity().getKiller()));
        }
    }

    @EventHandler
    public void onCustomEntityDeath(CustomEntityDeathRegister event) {
        CustomEntityDeathEvent.Event(event);
    }

    @EventHandler
    public void onCustomEntityDamageByEntity(CustomEntityDamageByEntityRegister event) {
        CustomEntityDamageByEntityEvent.Event(event);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.PlayerDeathEvent.Event(event);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.BlockBreakEvent.Event(event);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.BlockPlaceEvent.Event(event);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.PlayerInteractEvent.Event(event);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.PlayerJoinEvent.Event(event);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.PlayerRespawnEvent.Event(event);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.PlayerQuitEvent.Event(event);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.EntityDamageEvent.Event(event);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.EntityDamageByEntityEvent.Event(event);
        if (event.getEntity().getType() != org.bukkit.entity.EntityType.PLAYER) {
            callEvent(new CustomEntityDamageByEntityRegister());
        }
    }

    @EventHandler
    public void onChatUse(AsyncPlayerChatEvent event) {
        PlayerChatEvent.Event(event);
    }

    @EventHandler
    public void onCommandUse(PlayerCommandPreprocessEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.PlayerCommandPreprocessEvent.Event(event);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.ChunkLoadEvent.Event(event);
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.WorldLoadEvent.Event(event);
    }

    @EventHandler
    public void onWorldSave(WorldSaveEvent event) {
        OnWorldSaveEvent.Event(event);
    }

    @EventHandler
    public void PlayerLevelUp(PlayerLevelUpRegister event) {
        PlayerLevelUpEvent.Event(event);
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.EntityPickupItemEvent.Event(event);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.EntitySpawnEvent.Event(event);
    }

    @EventHandler
    public void onEntityRemove(EntityRemoveEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.EntityRemoveEvent.Event(event);
    }

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.PlayerItemDamageEvent.Event(event);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.PlayerMoveEvent.Event(event);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        org.gsdistance.grimmsServer.Events.Listeners.PlayerDropItemEvent.Event(event);
    }
}
