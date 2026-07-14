package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.ChatColor;
import org.gsdistance.grimmsServer.Commands.GAuthCommand.GAuthBaseCommand;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.Data.Player.PlayerTitleChecker;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Manage.GeneralChatHandler;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.ServerStats;
import org.gsdistance.grimmsServer.Stats.WorldStats;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerJoinEvent {
    public PlayerJoinEvent() {
    }

    public static void Event(org.bukkit.event.player.PlayerJoinEvent event) {
        PlayerStats playerStats = PlayerStats.getPlayerStats(event.getPlayer());
        playerStats.changeStat("join_count", 1);
        boolean autologin = playerStats.getStat("autologin", Boolean.class);
        GAuthBaseCommand.login(event.getPlayer(), autologin);
        WorldStats.getWorldStats(event.getPlayer().getWorld()).changeStat("join_count", 1);
        ServerStats.getServerStats().changeStat("join_count", 1);
        PlayerTitleChecker.joinedGame(event.getPlayer());
        PlayerMetadata metadata = PlayerMetadata.getPlayerMetadata(event.getPlayer());
        if (!event.getPlayer().getName().equals(metadata.lastKnownNames.getLast())) {
            metadata.lastKnownNames.add(event.getPlayer().getName());
        }

        playerStats.setStat("money", playerStats.getStat("money", Double.class) + metadata.offlineMoney);
        GeneralChatHandler.joinMessage(event.getPlayer());
        if (autologin) {
            event.getPlayer().sendMessage(ChatColor.GREEN + "Logged in.");
        } else {
            GeneralChatHandler.authMessage(event.getPlayer());
        }

        metadata.firstJoin = false;
        metadata.offlineMoney = 0.0F;
        metadata.saveToPDS();
        PerSessionDataStorage.dataStore.put("requestData-" + event.getPlayer().getName(), Data.of(new ArrayList(), ArrayList.class));

        if (!GAuthBaseCommand.isLoggedIn(event.getPlayer())) {
            event.getPlayer().setInvulnerable(true);
        }

        // Initialize paycheck timer on join to prevent immediate paycheck
        PlayerTickEvent.initializePaycheckTimer(event.getPlayer());

        GrimmsServer.historicalStatsManager.recordJoinSnapshot(event.getPlayer());
    }
}
