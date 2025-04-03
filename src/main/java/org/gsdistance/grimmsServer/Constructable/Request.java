package org.gsdistance.grimmsServer.Constructable;

import org.bukkit.entity.Player;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Function;

public class Request {
    private UUID uuid;
    private String forPlayer;
    public String forPurpose;
    private Function<Object, ?> onAccept;
    private LocalDateTime timestamp;
    private Object requestData;

    public Request(Function<Object, ?> onAccept, Player forPlayer, String forPurpose, Object requestData) {
        uuid = UUID.randomUUID();
        this.onAccept = onAccept;
        this.forPlayer = forPlayer.getName();
        this.forPurpose = forPurpose;
        this.requestData = requestData;
        this.timestamp = LocalDateTime.now();
    }

    public boolean canAccept(Player player){
        if(player.getName().equalsIgnoreCase(forPlayer)){
            return true;
        }
        return false;
    }

    public boolean acceptRequest(Player player){
        if(canAccept(player)){
            onAccept.apply(requestData);
            onAccept = (Object object) -> object;
            return true;
        }
        return false;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    };
}
