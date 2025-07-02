package org.gsdistance.grimmsServer.Commands.JobCommand;

import com.google.gson.Gson;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Commands.GLogCommand.*;
import org.gsdistance.grimmsServer.Constructable.ChunkMetadata;
import org.jetbrains.annotations.NotNull;

public class JobBaseCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        if (args.length == 0) {
            return false;
        }
        String sub = args[0].toLowerCase();
        return switch (sub) {
            case "log" -> Log.subCommand(player, args);
            case "take" -> Take.subCommand(player, args);
            default -> false;
        };
    }
}
