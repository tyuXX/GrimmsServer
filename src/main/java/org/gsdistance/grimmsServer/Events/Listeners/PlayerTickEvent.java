package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.Player.PlayerCapability;

import java.util.ArrayList;
import java.util.List;

public class PlayerTickEvent {
    private static final List<Player> magnetPlayers = new ArrayList();

    public PlayerTickEvent() {
    }

    public static void Event(Player player) {
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        if (playerMetadata.capabilities.containsKey(PlayerCapability.MAGNET) && playerMetadata.settings.contains(PlayerCapability.MAGNET.capabilityId)) {
            magnetPlayers.add(player);
        }

    }

    public static void processMagnets() {
        if (!magnetPlayers.isEmpty()) {
            for (Player magnetPlayer : magnetPlayers) {
                for (Item item : magnetPlayer.getWorld().getEntitiesByClass(Item.class)) {
                    if (item.getLocation().distance(magnetPlayer.getLocation()) <= (double) 10.0F) {
                        item.teleport(magnetPlayer.getLocation());
                        break;
                    }
                }
            }

            magnetPlayers.clear();
        }
    }
}
