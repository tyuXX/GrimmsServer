package org.gsdistance.grimmsServer.Commands.RequestCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Request;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;

public class AcceptRequest implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                return false;
            }
            if (!PerSessionDataStorage.dataStore.containsKey("request-" + args[0])) {
                sender.sendMessage("No such request found.");
                return false;
            }
            Request request = (Request) PerSessionDataStorage.dataStore.get("request-" + args[0]).key;
            if (!request.canAccept((Player) sender)) {
                sender.sendMessage("You cannot accept this request.");
                return false;
            }
            if (request.acceptRequest((Player) sender)) {
                sender.sendMessage("Successfully accepted request that was for: " + request.forPurpose);
                return true;
            }
        } else {
            sender.sendMessage("This command can only be run by a player.");
        }
        return false;
    }
}
