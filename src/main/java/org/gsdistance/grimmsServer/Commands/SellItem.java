package org.gsdistance.grimmsServer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.Stats.ServerStats;

public class SellItem implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (sender instanceof Player) {
            Market market = (Market) ServerStats.getServerStats().getStat("market");
            market.sell(((Player) sender).getInventory().getItemInMainHand(), (Player) sender);
            ServerStats.getServerStats().setStat("market", market);
            return true;
        }
        else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}
