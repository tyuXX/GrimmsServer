package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Constructable.Request;
import org.gsdistance.grimmsServer.Data.FactionRank;

import java.util.UUID;

public class Invite {
    public Invite() {
    }

    public static boolean subCommand(Player player, String[] args) {
        if (args.length < 2) {
            return false;
        } else {
            Player targetPlayer = Bukkit.getPlayer(args[1]);
            if (targetPlayer != null && targetPlayer.isOnline()) {
                PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
                if (playerMetadata.factionUUID == null) {
                    player.sendMessage(ChatColor.RED + "You are not a member of any faction.");
                    return false;
                } else if (PlayerMetadata.getPlayerMetadata(targetPlayer).factionUUID != null) {
                    player.sendMessage(ChatColor.RED + "The player you are trying to invite is already in a faction.");
                    return false;
                } else {
                    Faction faction = Faction.getFaction(playerMetadata.factionUUID);
                    if (faction == null) {
                        player.sendMessage(ChatColor.RED + "Your faction could not be found.");
                        return false;
                    } else if (faction.getMemberRank(player.getUniqueId()).weight < FactionRank.MEMBER.weight) {
                        player.sendMessage(ChatColor.RED + "You do not have permission to invite players to your faction.");
                        return false;
                    } else {
                        Request.newRequest((object) -> {
                            Data<Player, UUID> data = (Data) object;
                            Faction targetFaction = Faction.getFaction(data.value());
                            if (targetFaction == null) {
                                return null;
                            }
                            Player target = data.key();
                            PlayerMetadata targetMetadata = PlayerMetadata.getPlayerMetadata(target);
                            targetMetadata.factionUUID = targetFaction.uuid;
                            targetMetadata.saveToPDS();
                            targetFaction.addMember(target.getUniqueId(), FactionRank.RECRUIT);
                            targetFaction.saveToFile();
                            return null;
                        }, targetPlayer, "You have been invited to join the faction " + faction.name + ".", Data.of(targetPlayer, playerMetadata.factionUUID));
                        return true;
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "The player you are trying to invite is not online.");
                return false;
            }
        }
    }
}
