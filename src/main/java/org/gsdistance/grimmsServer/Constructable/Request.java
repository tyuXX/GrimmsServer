package org.gsdistance.grimmsServer.Constructable;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class Request {
    private final String forPlayer;
    public final String forPurpose;
    private final Function<Object, ?> onAccept;
    private final LocalDateTime timestamp;
    private final Object requestData;
    private int requestId;

    public Request(Function<Object, ?> onAccept, Player forPlayer, String forPurpose, Object requestData) {
        this.onAccept = onAccept;
        this.forPlayer = forPlayer.getName();
        this.forPurpose = forPurpose;
        this.requestData = requestData;
        this.timestamp = LocalDateTime.now();
        do {
            requestId = new Random().nextInt(1000000, 9999999);
        } while (PerSessionDataStorage.dataStore.containsKey("request-" + requestId));
    }

    public static void newRequest(Function<Object, ?> onAccept, Player forPlayer, String forPurpose, Object requestData) {
        Request request = new Request(onAccept, forPlayer, forPurpose, requestData);

        forPlayer.sendMessage("You have a new request: " + forPurpose);
        forPlayer.sendMessage("Accept with /acceptRequest " + request.requestId);
        PerSessionDataStorage.dataStore.put("request-" + request.requestId, Data.of(request, Request.class));

        ArrayList<Integer> requestDataList = (ArrayList<Integer>) PerSessionDataStorage.dataStore.get("requestData-" + forPlayer.getName()).key();
        requestDataList.add(request.requestId);
        PerSessionDataStorage.dataStore.put("requestData-" + forPlayer.getName(), Data.of(requestDataList, ArrayList.class));
    }

    public boolean canAccept(Player player) {
        return player.getName().equalsIgnoreCase(forPlayer);
    }

    public boolean acceptRequest(Player player) {
        if (canAccept(player)) {
            onAccept.apply(requestData);
            PerSessionDataStorage.dataStore.remove("request-" + requestId);
            ArrayList<Integer> requestDataList = (ArrayList<Integer>) PerSessionDataStorage.dataStore
                    .get("requestData-" + player.getName())
                    .key();
            requestDataList.removeIf(id -> id.equals(requestId));
            PerSessionDataStorage.dataStore.put("requestData-" + player.getName(), Data.of(requestDataList, ArrayList.class));
            return true;
        }
        return false;
    }
}