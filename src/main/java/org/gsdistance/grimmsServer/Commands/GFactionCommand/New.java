package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.FactionRank;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.Arrays;
import java.util.List;

public class New {
    public static boolean subCommand(Player player, String[] args) {
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        if(playerMetadata.factionUUID != null) {
            player.sendMessage("You are already in a faction.");
            return false;
        }
        if (args.length < 3) {
            return false;
        }
        Faction[] factions = GrimmsServer.pds.retrieveAllData(Faction.class, "factions");
        if(Arrays.stream(factions).anyMatch(faction -> faction.id.equalsIgnoreCase(args[1]))) {
            player.sendMessage("§cFaction already exists with this id.");
            return false;
        }
        Faction faction = new Faction(args[1], List.of(new Data<>(player.getUniqueId(), FactionRank.LEADER)));
        faction.name = args[2];
        faction.saveToFile();
        playerMetadata.factionUUID = faction.uuid;
        playerMetadata.saveToPDS();
        player.sendMessage("Faction " + faction.id + " created successfully.");
        return true;
    }
}
