package org.gsdistance.grimmsServer.Events;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Data.PlayerMetadata;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class PlayerChatEvent {
    public static void Event(AsyncPlayerChatEvent event) {
        PlayerStats.getPlayerStats(event.getPlayer()).changeStat("sent_messages", 1);

        if(ActiveConfig.module_Chat){
            // If active, intercept and reformat the chat message
            String message = event.getMessage();
            PlayerMetadata metadata = PlayerMetadata.getPlayerMetadata(event.getPlayer());
            if (message.charAt(0) != '/') {
                event.setMessage(
                    String.format(
                        "<%s>: %s",
                        metadata.nickname,
                        message
                    )
                );
            }
        }
    }
}
