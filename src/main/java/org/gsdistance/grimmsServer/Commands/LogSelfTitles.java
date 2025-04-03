package org.gsdistance.grimmsServer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Stats.PlayerTitles;

public class LogSelfTitles implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            PlayerTitles playerTitles = PlayerTitles.getPlayerTitles(player);
            sender.sendMessage("__Your Titles:");
            for (String title : playerTitles.getTitles()) {
                sender.sendMessage("|" + title + ": " + PlayerTitles.titles.get(title));
            }
            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}
