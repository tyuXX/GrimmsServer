package org.gsdistance.grimmsServer.Constructable;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;

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
            this.requestId = (new Random()).nextInt(1000000, 9999999);
        } while (PerSessionDataStorage.dataStore.containsKey("request-" + this.requestId));

    }

    public static void newRequest(Function<Object, ?> onAccept, Player forPlayer, String forPurpose, Object requestData) {
        Request request = new Request(onAccept, forPlayer, forPurpose, requestData);
        forPlayer.sendMessage(ChatColor.GOLD + "You have a new request: " + ChatColor.WHITE + forPurpose);
        forPlayer.sendMessage(ChatColor.GRAY + "Accept with " + ChatColor.YELLOW + "/acceptRequest " + request.requestId);
        PerSessionDataStorage.dataStore.put("request-" + request.requestId, Data.of(request, Request.class));
        ArrayList<Integer> requestDataList = (ArrayList) PerSessionDataStorage.dataStore.get("requestData-" + forPlayer.getName()).key();
        requestDataList.add(request.requestId);
        PerSessionDataStorage.dataStore.put("requestData-" + forPlayer.getName(), Data.of(requestDataList, ArrayList.class));
    }

    public boolean canAccept(Player player) {
        return player.getName().equalsIgnoreCase(this.forPlayer);
    }

    public boolean acceptRequest(Player player) {
        if (this.canAccept(player)) {
            this.onAccept.apply(this.requestData);
            PerSessionDataStorage.dataStore.remove("request-" + this.requestId);
            ArrayList<Integer> requestDataList = (ArrayList) PerSessionDataStorage.dataStore.get("requestData-" + player.getName()).key();
            requestDataList.removeIf((id) -> id.equals(this.requestId));
            PerSessionDataStorage.dataStore.put("requestData-" + player.getName(), Data.of(requestDataList, ArrayList.class));
            return true;
        } else {
            return false;
        }
    }
}
