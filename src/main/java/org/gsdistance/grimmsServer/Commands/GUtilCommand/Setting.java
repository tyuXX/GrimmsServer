package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;

import java.util.ArrayList;
import java.util.List;

public class Setting {
    public Setting() {
    }

    public static boolean subCommand(Player player, String[] args) {
        if (args.length < 2) {
            return false;
        } else {
            String settingName = args[1].toLowerCase();
            PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
            playerMetadata.settings = (List<String>) (playerMetadata.settings == null ? new ArrayList() : playerMetadata.settings);
            if (playerMetadata.settings.contains(settingName)) {
                playerMetadata.settings.remove(settingName);
                player.sendMessage("Removed setting: " + settingName);
            } else {
                playerMetadata.settings.add(settingName);
                player.sendMessage("Added setting: " + settingName);
            }

            playerMetadata.saveToPDS();
            return true;
        }
    }
}
