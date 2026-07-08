package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Constructable.Request;
import org.gsdistance.grimmsServer.Data.FactionRank;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.UUID;

public class Join {
    public Join() {
    }

    public static boolean subCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /gfaction join <factionName>");
            return false;
        } else {
            PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
            if (playerMetadata.factionUUID != null) {
                player.sendMessage(ChatColor.RED + "You are already a member of a faction. Leave your current faction first.");
                return false;
            } else {
                Faction[] factions = GrimmsServer.pds.retrieveAllData(Faction.class, "factions");
                Faction targetFaction = null;

                for (Faction faction : factions) {
                    if (faction.id.equalsIgnoreCase(args[1])) {
                        targetFaction = faction;
                        break;
                    }
                }

                if (targetFaction == null) {
                    player.sendMessage(ChatColor.RED + "The faction you are trying to join does not exist.");
                    return false;
                } else {
                    UUID leaderUUID = targetFaction.getMemberWithRank(FactionRank.LEADER);
                    if (leaderUUID == null) {
                        player.sendMessage(ChatColor.RED + "The faction does not have a leader.");
                        return false;
                    } else {
                        Player targetPlayer = Bukkit.getPlayer(leaderUUID);
                        if (targetPlayer != null && targetPlayer.isOnline()) {
                                Request.newRequest((object) -> {
                                    Data<Player, UUID> data = (Data) object;
                                    Player target = data.key();
                                    PlayerMetadata targetMetadata = PlayerMetadata.getPlayerMetadata(target);
                                    Faction faction = Faction.getFaction(data.value());
                                    if (faction == null) {
                                        target.sendMessage(ChatColor.RED + "The faction no longer exists.");
                                        return null;
                                    }
                                    target.sendMessage(ChatColor.GREEN + "You have successfully joined the faction " + ChatColor.YELLOW + faction.name + ChatColor.GREEN + ".");
                                    targetMetadata.factionUUID = faction.uuid;
                                    faction.addMember(player.getUniqueId(), FactionRank.RECRUIT);
                                    faction.saveToFile();
                                    targetMetadata.saveToPDS();
                                    return null;
                                }, targetPlayer, player.getName() + " wants to join your faction.", Data.of(player, targetFaction.uuid));
                                return true;
                            } else {
                                player.sendMessage(ChatColor.RED + "No leader is online to accept your request.");
                                return true;
                            }
                    }
                }
            }
        }
    }
}
