package org.gsdistance.grimmsServer.Commands.HomeCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.jetbrains.annotations.NotNull;

public class DelHome {
    @SuppressWarnings("SameReturnValue")
    public static boolean SubCommand(Player player, @NotNull String[] args) {
        String homeName = "home";
        if (args.length > 1) {
            homeName = args[1].toLowerCase();
        }
        PlayerMetadata meta = PlayerMetadata.getPlayerMetadata(player);
        if (!meta.homes.containsKey(homeName)) {
            player.sendMessage("Home '" + homeName + "' does not exist.");
            return true;
        }
        meta.homes.remove(homeName);
        meta.saveToPDS();
        player.sendMessage("Home '" + homeName + "' deleted.");
        return true;
    }
}
