package org.gsdistance.grimmsServer.Commands;

import Leveling.ItemLevelHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class MakeItemLevelable implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            PlayerStats playerStats = PlayerStats.getPlayerStats((Player) sender);
            if (((Player) sender).getInventory().getItemInMainHand().getType().getMaxDurability() > 1) {
                if ((int) playerStats.getStat("level") > 10) {
                    if (ItemLevelHandler.isItemLevelable(((Player) sender).getInventory().getItemInMainHand())) {
                        sender.sendMessage("This item is already levelable.");
                        return false;
                    } else {
                        if ((Double) playerStats.getStat("money") > 3500) {
                            playerStats.changeStat("money", -3500);
                            ItemLevelHandler.getLevelHandler((Player) sender);
                        } else {
                            sender.sendMessage("You need 3500 money to make an item levelable.");
                            return false;
                        }
                    }
                } else {
                    sender.sendMessage("You need to be level 10 to make an item levelable.");
                    return false;
                }
            }
            return false;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}
