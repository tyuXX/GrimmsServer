package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.FactionRank;

import java.util.UUID;

public class Info {
    public Info() {
    }

    public static boolean subCommand(Player player) {
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        Faction faction = Faction.getFaction(playerMetadata.factionUUID);
        if (faction == null) {
            player.sendMessage(ChatColor.RED + "You are not part of any faction.");
            return false;
        } else {
            for (Data<UUID, FactionRank> member : faction.members) {
                PlayerMetadata memberMetadata = PlayerMetadata.getOfflinePlayerMetadata(member.key());
                if (memberMetadata != null) {
                    String var10001 = memberMetadata.nickname;
                    player.sendMessage(ChatColor.GREEN + "Member: " + ChatColor.YELLOW + var10001 + ChatColor.GREEN + " - Rank: " + ChatColor.YELLOW + member.value().toString());
                } else {
                    player.sendMessage(ChatColor.RED + "Member with UUID " + member.key() + " not found.");
                }
            }

            player.sendMessage(ChatColor.GREEN + "Faction Name: " + ChatColor.YELLOW + faction.name);
            player.sendMessage(ChatColor.GREEN + "Faction UUID: " + ChatColor.YELLOW + faction.uuid);
            player.sendMessage(ChatColor.GREEN + "Faction Members Count: " + ChatColor.YELLOW + faction.members.size());
            int var6 = faction.claims.size();
            player.sendMessage(ChatColor.GREEN + "Faction Claimed Chunks: " + ChatColor.YELLOW + var6 + ChatColor.GREEN + "/" + ChatColor.YELLOW + faction.getClaimLimit());
            return true;
        }
    }
}
