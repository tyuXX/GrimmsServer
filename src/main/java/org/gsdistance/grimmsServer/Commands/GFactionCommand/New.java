package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.FactionRank;

public class New {
   public New() {
   }

   public static boolean subCommand(Player player, String[] args) {
      PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
      if (playerMetadata.factionUUID != null) {
         player.sendMessage("You are already in a faction.");
         return false;
      } else if (args.length < 3) {
         return false;
      } else {
         Faction[] factions = (Faction[])GrimmsServer.pds.retrieveAllData(Faction.class, "factions");
         if (factions != null && Arrays.stream(factions).anyMatch((factionx) -> factionx.id.equalsIgnoreCase(args[1]))) {
            player.sendMessage("§cFaction already exists with this id.");
            return false;
         } else if (!args[1].matches("[a-zA-Z0-9_]+")) {
            player.sendMessage("§cFaction id can only contain letters, numbers and underscores.");
            return false;
         } else {
            Faction faction = new Faction(args[1], new ArrayList(List.of(new Data(player.getUniqueId(), FactionRank.LEADER))));
            faction.name = args[2];
            faction.saveToFile();
            playerMetadata.factionUUID = faction.uuid;
            playerMetadata.saveToPDS();
            KeyedBossBar bossBar = GrimmsServer.instance.getServer().createBossBar(Shared.getNamespacedKey("faction_" + faction.id), faction.name, BarColor.WHITE, BarStyle.SOLID, new BarFlag[0]);
            bossBar.setVisible(true);
            bossBar.setProgress((double)0.0F);
            bossBar.setTitle(faction.name);
            player.sendMessage("Faction " + faction.id + " created successfully.");
            return true;
         }
      }
   }
}
