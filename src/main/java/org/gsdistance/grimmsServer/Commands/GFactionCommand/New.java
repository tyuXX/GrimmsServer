package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.FactionRank;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class New {
    public static boolean subCommand(Player player, String[] args) {
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        if (playerMetadata.factionUUID != null) {
            player.sendMessage("You are already in a faction.");
            return false;
        }
        if (args.length < 3) {
            return false;
        }
        Faction[] factions = GrimmsServer.pds.retrieveAllData(Faction.class, "factions");
        if (factions != null && Arrays.stream(factions).anyMatch(faction -> faction.id.equalsIgnoreCase(args[1]))) {
            player.sendMessage("§cFaction already exists with this id.");
            return false;
        }
        if (!args[1].matches("[a-zA-Z0-9_]+")) {
            player.sendMessage("§cFaction id can only contain letters, numbers and underscores.");
            return false;
        }
        Faction faction = new Faction(args[1], new ArrayList<>(List.of(new Data<>(player.getUniqueId(), FactionRank.LEADER))));
        faction.name = args[2];
        faction.saveToFile();
        playerMetadata.factionUUID = faction.uuid;
        playerMetadata.saveToPDS();
        // Create a boss bar for the faction
        KeyedBossBar bossBar = GrimmsServer.instance.getServer().createBossBar(Shared.getNamespacedKey("faction_" + faction.id), faction.name, BarColor.WHITE, BarStyle.SOLID);
        bossBar.setVisible(true);
        bossBar.setProgress(0);
        bossBar.setTitle(faction.name);
        player.sendMessage("Faction " + faction.id + " created successfully.");
        return true;
    }
}
