package org.gsdistance.grimmsServer.Constructable;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

public class Request {
    private final String forPlayer;
    public String forPurpose;
    private final Function<Object, ?> onAccept;
    private final LocalDateTime timestamp;
    private final Object requestData;

    public Request(Function<Object, ?> onAccept, Player forPlayer, String forPurpose, Object requestData) {
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

        ArrayList<Integer> requestDataList = (ArrayList<Integer>) PerSessionDataStorage.dataStore.get("requestData-" + forPlayer.getName()).keySet().toArray()[0];
        requestDataList.add(requestId);
        PerSessionDataStorage.dataStore.put("requestData-" + forPlayer.getName(), Map.of(requestDataList, ArrayList.class));
    }

    public boolean canAccept(Player player) {
        return player.getName().equalsIgnoreCase(forPlayer);
    }

    public boolean acceptRequest(Player player) {
        if (canAccept(player)) {
            onAccept.apply(requestData);
            PerSessionDataStorage.dataStore.remove("request-" + player.getName());
            ArrayList<Integer> requestDataList = (ArrayList<Integer>) PerSessionDataStorage.dataStore.get("requestData-" + player.getName()).keySet().toArray()[0];
            requestDataList.removeIf(id -> id.equals(Integer.parseInt(forPurpose)));
            PerSessionDataStorage.dataStore.put("requestData-" + player.getName(), Map.of(requestDataList, ArrayList.class));
            return true;
        }
        return false;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}