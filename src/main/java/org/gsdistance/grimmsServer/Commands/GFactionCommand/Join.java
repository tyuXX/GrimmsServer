package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.PlayerMetadata;
import org.gsdistance.grimmsServer.Constructable.Request;
import org.gsdistance.grimmsServer.Data.FactionRank;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.Objects;
import java.util.UUID;

public class Join {
    public static boolean subCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cUsage: /gfaction join <factionName>");
            return false;
        }
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        if (playerMetadata.factionUUID != null) {
            player.sendMessage("§cYou are already a member of a faction. Leave your current faction first.");
            return false;
        }
        Faction[] factions = GrimmsServer.pds.retrieveAllData(Faction.class, "factions");
        Faction targetFaction = null;
        for (Faction faction : factions) {
            if (faction.id.equalsIgnoreCase(args[1])) {
                targetFaction = faction;
                break;
            }
        }
        if (targetFaction == null) {
            player.sendMessage("§cThe faction you are trying to join does not exist.");
            return false;
        }
        UUID leaderUUID = targetFaction.getMemberWithRank(FactionRank.LEADER);
        if (leaderUUID == null) {
            player.sendMessage("§cThe faction does not have a leader.");
            return false;
        }
        Player targetPlayer = Bukkit.getPlayer(leaderUUID);
        if (targetPlayer == null || !targetPlayer.isOnline()) {
            player.sendMessage("§cThe faction leader is not online.");
            return false;
        }
        if (Objects.requireNonNull(targetPlayer).isOnline()) {
            Request.newRequest((Object object) -> {
                        Data<Player, UUID> data = (Data<Player, UUID>) object;
                        Player target = data.key;
                        PlayerMetadata targetMetadata = PlayerMetadata.getPlayerMetadata(target);
                        Faction faction = Faction.getFaction(data.value);
                        target.sendMessage("You have successfully joined the faction " + faction.name + ".");
                        targetMetadata.factionUUID = faction.uuid;
                        faction.addMember(player.getUniqueId(), FactionRank.RECRUIT);
                        faction.saveToFile();
                        targetMetadata.saveToPDS();
                        return null;
                    },
                    targetPlayer,
                    player.getName() + " wants to join your faction.",
                    Data.of(player, targetFaction.uuid)
            );
            return true;
        }
        player.sendMessage("§cNo leader or officer is online to accept your request.");
        return true;
    }
}
