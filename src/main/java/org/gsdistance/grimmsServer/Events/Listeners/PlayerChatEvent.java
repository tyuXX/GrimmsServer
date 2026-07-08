package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.gsdistance.grimmsServer.Commands.GAuthCommand.GAuthBaseCommand;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Manage.GeneralChatHandler;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import java.util.UUID;

public class PlayerChatEvent {
    public PlayerChatEvent() {
    }

    public static void Event(AsyncPlayerChatEvent event) {
        if (!GAuthBaseCommand.isLoggedIn(event.getPlayer())) {
            event.getPlayer().sendMessage(ChatColor.RED + "You must login with /gAuth login <password> to perform this action.");
            event.setCancelled(true);
            return;
        }

        PlayerStats.getPlayerStats(event.getPlayer()).changeStat("sent_messages", 1);

        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(event.getPlayer());
        if (playerMetadata.factionChatEnabled) {
            Faction faction = Faction.getFaction(playerMetadata.factionUUID);
            if (faction != null) {
                event.setCancelled(true);
                String factionMessage = ChatColor.GREEN + "[Faction] " + ChatColor.YELLOW + event.getPlayer().getName() + ChatColor.WHITE + ": " + event.getMessage();
                for (org.gsdistance.grimmsServer.Constructable.Data<UUID, org.gsdistance.grimmsServer.Data.FactionRank> member : faction.members) {
                    Player memberPlayer = Bukkit.getPlayer(member.key());
                    if (memberPlayer != null && memberPlayer.isOnline()) {
                        memberPlayer.sendMessage(factionMessage);
                    }
                }
                return;
            }
        }

        event.setFormat(GeneralChatHandler.handleMessage(event.getMessage(), event.getPlayer()));
    }
}
