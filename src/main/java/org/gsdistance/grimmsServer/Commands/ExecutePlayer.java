package org.gsdistance.grimmsServer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerTitles;
import org.jetbrains.annotations.NotNull;

public class ExecutePlayer implements CommandExecutor {
    public ExecutePlayer() {
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return false;
        } else {
            Player player = GrimmsServer.instance.getServer().getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage("Player not found.");
                return false;
            } else {
                if (sender instanceof Player && !PlayerTitles.getPlayerTitles((Player) sender).hasTitle("Executioner")) {
                    sender.sendMessage("You need the Executioner title to use this command.");
                    return false;
                }
                player.sendMessage("You have been executed by " + (sender instanceof Player ? ((Player) sender).getDisplayName() : "CONSOLE"));
                sender.sendMessage("You have executed " + player.getDisplayName());
                player.damage(player.getHealth() * (double) 10.0F);
                return true;
            }
        }
    }
}
