package org.gsdistance.grimmsServer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Item.ItemLevelHandler;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class MakeItemLevelable implements CommandExecutor {
    public MakeItemLevelable() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            PlayerStats playerStats = PlayerStats.getPlayerStats((Player) sender);
            if (((Player) sender).getInventory().getItemInMainHand().getType().getMaxDurability() > 1) {
                if (playerStats.getStat("level", Integer.class) <= 10) {
                    sender.sendMessage("You need to be over level 10 to make an item levelable.");
                    return false;
                }

                if (ItemLevelHandler.isItemLevelable(((Player) sender).getInventory().getItemInMainHand())) {
                    sender.sendMessage("This item is already levelable.");
                    return false;
                }

                if (!(playerStats.getStat("money", Double.class) > (double) 3500.0F)) {
                    sender.sendMessage("You need over 3500 money to make an item levelable.");
                    return false;
                }

                playerStats.changeStat("money", -3500);
                ItemLevelHandler.getLevelHandler((Player) sender);
            }

            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}
