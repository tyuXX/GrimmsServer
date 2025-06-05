package org.gsdistance.grimmsServer.Events;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Data.PlayerMetadata;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import java.time.LocalDateTime;

public class PlayerChatEvent {
    public static void Event(AsyncPlayerChatEvent event) {
        PlayerStats.getPlayerStats(event.getPlayer()).changeStat("sent_messages", 1);
        if(ActiveConfig.module_Chat){
            // If active, intercept and reformat the chat message
            if (event.getMessage().contains(":")) {
                // Split the message to get the nickname and the actual message
                String message = event.getMessage().split(":")[1];
                PlayerMetadata metadata = PlayerMetadata.getPlayerMetadata(event.getPlayer());
                if (message.charAt(0) != '/') {
                    event.setMessage(
                            String.format(
                                            "<%s>: %s",
                                            metadata.nickname,
                                            message
                                    ).replace("&timeF", LocalDateTime.now().toString())
                                    .replace("&world", event.getPlayer().getWorld().getName())
                                    .replace("&pos", event.getPlayer().getLocation().toString())
                                    .replace("&player", event.getPlayer().getName())
                                    .replace("&uuid", event.getPlayer().getUniqueId().toString())
                                    .replace("&date", LocalDateTime.now().toLocalDate().toString())
                                    .replace("&time", LocalDateTime.now().toLocalTime().toString())
                    );
                }
            }
        }
    }
}
