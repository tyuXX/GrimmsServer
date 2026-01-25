package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Constructable.Request;
import org.gsdistance.grimmsServer.Data.FactionRank;

import java.util.UUID;

public class Invite {
    public static boolean subCommand(Player player, String[] args) {
        if (args.length < 2) {
            return false;
        }
        Player targetPlayer = Bukkit.getPlayer(args[1]);
        if (targetPlayer == null || !targetPlayer.isOnline()) {
            player.sendMessage("§cThe player you are trying to invite is not online.");
            return false;
        }
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        if (playerMetadata.factionUUID == null) {
            player.sendMessage("§cYou are not a member of any faction.");
            return false;
        }
        if (PlayerMetadata.getPlayerMetadata(targetPlayer).factionUUID != null) {
            player.sendMessage("§cThe player you are trying to invite is already in a faction.");
            return false;
        }
        Faction faction = Faction.getFaction(playerMetadata.factionUUID);
        if (faction.getMemberRank(player.getUniqueId()).weight < FactionRank.MEMBER.weight) {
            player.sendMessage("§cYou do not have permission to invite players to your faction.");
            return false;
        }
        Request.newRequest(
                (Object object) -> {
                    Data<Player, UUID> data = (Data<Player, UUID>) object;
                    Faction targetFaction = Faction.getFaction(data.value());
                    Player target = data.key();
                    PlayerMetadata targetMetadata = PlayerMetadata.getPlayerMetadata(target);
                    targetMetadata.factionUUID = targetFaction.uuid;
                    targetMetadata.saveToPDS();
                    targetFaction.addMember(target.getUniqueId(), FactionRank.RECRUIT);
                    faction.saveToFile();
                    return null;
                },
                targetPlayer,
                "You have been invited to join the faction " + faction.name + ".",
                Data.of(targetPlayer, playerMetadata.factionUUID)
        );
        return true;
    }
}
