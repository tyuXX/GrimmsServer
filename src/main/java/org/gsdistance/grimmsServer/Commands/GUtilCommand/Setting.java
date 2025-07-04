package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;

import java.util.ArrayList;

public class Setting {
    public static boolean subCommand(Player player, String[] args) {
        if (args.length < 2) {
            return false;
        }
        String settingName = args[1].toLowerCase();
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        playerMetadata.settings = playerMetadata.settings == null ? new ArrayList<>() : playerMetadata.settings;
        if (playerMetadata.settings.contains(settingName)) {
            playerMetadata.settings.remove(settingName);
            player.sendMessage("Removed setting: " + settingName);
        } else {
            playerMetadata.settings.add(settingName);
            player.sendMessage("Added setting: " + settingName);
        }
        return true;
    }
}
