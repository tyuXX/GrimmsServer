package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.Player.PlayerCapability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerTickEvent {
    private static final List<Player> magnetPlayers = new ArrayList();
    private static final Map<Player, Integer> saturationPerkPlayers = new HashMap();

    public PlayerTickEvent() {
    }

    public static void Event(Player player) {
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        if (playerMetadata.capabilities.containsKey(PlayerCapability.MAGNET) && playerMetadata.settings.contains(PlayerCapability.MAGNET.capabilityId)) {
            magnetPlayers.add(player);
        }
        
        // Handle Saturation Perk
        if (playerMetadata.capabilities.containsKey(PlayerCapability.SATURATION_PERK) && 
            playerMetadata.settings.contains(PlayerCapability.SATURATION_PERK.capabilityId)) {
            int currentDuration = saturationPerkPlayers.getOrDefault(player, 0);
            saturationPerkPlayers.put(player, currentDuration + 1);
            
            // Apply saturation effect with lowest level (1) for 10 seconds
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 200, 0, false, false));
        } else {
            saturationPerkPlayers.remove(player);
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
