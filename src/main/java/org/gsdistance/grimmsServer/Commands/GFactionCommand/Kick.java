package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.PlayerMetadata;

public class Kick {
    public static boolean subCommand(Player player, String[] args) {
        if (args.length < 2) {
            return false;
        }
        Faction faction = Faction.getFaction(PlayerMetadata.getPlayerMetadata(player).factionUUID);
        if (faction == null) {
            player.sendMessage("§cYou are not in a faction.");
            return false;
        }
        Player targetPlayer = Bukkit.getPlayer(args[1]);
        if (targetPlayer == null) {
            player.sendMessage("§cPlayer not found.");
            return false;
        }
        if (targetPlayer.equals(player)) {
            player.sendMessage("§cYou cannot kick yourself.");
            return false;
        }
        if (!faction.isMember(targetPlayer.getUniqueId())) {
            player.sendMessage("§cThis player is not a member of your faction.");
            return false;
        }
        if (faction.getMemberRank(player.getUniqueId()).weight <= faction.getMemberRank(targetPlayer.getUniqueId()).weight) {
            player.sendMessage("§cYou cannot kick a player with the same or higher rank.");
            return false;
        }
        faction.removeMember(targetPlayer.getUniqueId());
        PlayerMetadata targetMetadata = PlayerMetadata.getPlayerMetadata(targetPlayer);
        targetMetadata.factionUUID = null;
        targetMetadata.saveToPDS();
        faction.saveToFile();
        player.sendMessage("§aYou have kicked " + targetPlayer.getName() + " from your faction.");
        targetPlayer.sendMessage("§cYou have been kicked from the faction " + faction.name + ".");
        return true;
    }
}
