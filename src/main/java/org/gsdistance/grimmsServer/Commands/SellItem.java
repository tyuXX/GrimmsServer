package org.gsdistance.grimmsServer.Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Constructable.Market;

public class SellItem implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Market market = Market.getMarket();
            ItemStack iS = ((Player) sender).getInventory().getItemInMainHand();
            if (iS.getType() == Material.AIR) {
                sender.sendMessage("You are not holding anything.");
                return false;
            }
            double sold = market.sell(iS, (Player) sender);
            market.saveMarket();
            sender.sendMessage("You sold " + iS.getAmount() + " many of " + iS.getType().getKey() + " for " + Math.max(0.25D, Math.round((sold))));
            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}
