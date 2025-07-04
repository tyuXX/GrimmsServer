package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.Player.PlayerCapability;

import java.util.ArrayList;
import java.util.HashMap;

public class Capability {
    public static boolean subCommand(Player player, String[] args) {
        if(!player.hasPermission("grimmsserver.util.admin")){
            player.sendMessage("You do not have permission to use this command.");
            return false;
        }
        if (args.length < 2) {
            return false;
        }
        String capabilityName = args[1].toLowerCase();
        PlayerCapability capability = PlayerCapability.valueOf(capabilityName.toUpperCase());
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        playerMetadata.capabilities = playerMetadata.capabilities == null ? new HashMap<>() : playerMetadata.capabilities;
        playerMetadata.capabilities.put(capability, playerMetadata.capabilities.getOrDefault(capability, 0) + 1);
        playerMetadata.saveToPDS();
        player.sendMessage("Granted capability: " + capability.displayName);
        return true;
    }
}
