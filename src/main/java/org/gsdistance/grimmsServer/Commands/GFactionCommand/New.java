package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
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
    public New() {
    }

    public static boolean subCommand(Player player, String[] args) {
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        if (playerMetadata.factionUUID != null) {
            player.sendMessage(ChatColor.RED + "You are already in a faction.");
            return false;
        } else if (args.length < 3) {
            return false;
        } else {
            Faction[] factions = GrimmsServer.pds.retrieveAllData(Faction.class, "factions");
            if (factions != null && Arrays.stream(factions).anyMatch((factionx) -> factionx.id.equalsIgnoreCase(args[1]))) {
                player.sendMessage(ChatColor.RED + "Faction already exists with this id.");
                return false;
            } else if (!args[1].matches("[a-zA-Z0-9_]+")) {
                player.sendMessage(ChatColor.RED + "Faction id can only contain letters, numbers and underscores.");
                return false;
            } else {
                Faction faction = new Faction(args[1], new ArrayList(List.of(new Data(player.getUniqueId(), FactionRank.LEADER))));
                faction.name = args[2];
                faction.saveToFile();
                playerMetadata.factionUUID = faction.uuid;
                playerMetadata.saveToPDS();
                KeyedBossBar bossBar = GrimmsServer.instance.getServer().createBossBar(Shared.getNamespacedKey("faction_" + faction.id), faction.name, BarColor.WHITE, BarStyle.SOLID);
                bossBar.setVisible(true);
                bossBar.setProgress(0.0F);
                bossBar.setTitle(faction.name);
                player.sendMessage(ChatColor.GREEN + "Faction " + ChatColor.YELLOW + faction.id + ChatColor.GREEN + " created successfully.");
                return true;
            }
        }
    }
}
