package org.gsdistance.grimmsServer.Constructable;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;

public class Request {
    private final UUID uuid;
    private final String forPlayer;
    public String forPurpose;
    private Function<Object, ?> onAccept;
    private final LocalDateTime timestamp;
    private final Object requestData;

    public Request(Function<Object, ?> onAccept, Player forPlayer, String forPurpose, Object requestData) {
        uuid = UUID.randomUUID();
        this.onAccept = onAccept;
        this.forPlayer = forPlayer.getName();
        this.forPurpose = forPurpose;
        this.requestData = requestData;
        this.timestamp = LocalDateTime.now();
    }

    public static void newRequest(Function<Object, ?> onAccept, Player forPlayer, String forPurpose, Object requestData) {
        Request request = new Request(onAccept, forPlayer, forPurpose, requestData);
        int requestId = new Random().nextInt(1000000, 9999999);
        while (PerSessionDataStorage.dataStore.containsKey("request-" + requestId)) {
            requestId = new Random().nextInt(1000000, 9999999);
        }
        forPlayer.sendMessage("You have a new request: " + forPurpose);
        forPlayer.sendMessage("Accept with /acceptRequest " + requestId);
        PerSessionDataStorage.dataStore.put("request-" + requestId, Map.of(request, Request.class));
    }

    public boolean canAccept(Player player) {
        return player.getName().equalsIgnoreCase(forPlayer);
    }

    public boolean acceptRequest(Player player) {
        if (canAccept(player)) {
            onAccept.apply(requestData);
            onAccept = (Object object) -> object;
            return true;
        }
        return false;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
