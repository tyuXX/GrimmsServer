package org.gsdistance.grimmsServer.Events;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.gsdistance.grimmsServer.Constructable.PlayerMetadata;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import java.time.LocalDateTime;

public class PlayerChatEvent {
    public static void Event(AsyncPlayerChatEvent event) {
        PlayerStats.getPlayerStats(event.getPlayer()).changeStat("sent_messages", 1);
        // Retrieve player metadata
        PlayerMetadata metadata = PlayerMetadata.getPlayerMetadata(event.getPlayer());
        String nickname = metadata != null ? metadata.nickname : event.getPlayer().getName();

        // Format the chat message
        String formattedMessage = String.format(
                        "<%s>: %s",
                        nickname,
                        event.getMessage()
                ).replace("&timeF", LocalDateTime.now().toString())
                .replace("&world", event.getPlayer().getWorld().getName())
                .replace("&pos", event.getPlayer().getLocation().toString())
                .replace("&player", event.getPlayer().getName())
                .replace("&uuid", event.getPlayer().getUniqueId().toString())
                .replace("&date", LocalDateTime.now().toLocalDate().toString())
                .replace("&time", LocalDateTime.now().toLocalTime().toString());

        // Set the formatted message
        event.setFormat(formattedMessage);
    }
}
