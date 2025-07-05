package org.gsdistance.grimmsServer.Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.gsdistance.grimmsServer.Constructable.Item.ItemDataHandler;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import java.time.LocalDateTime;
import java.util.List;

public class WithdrawMoney implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                return false;
            }
            if (Double.parseDouble(args[0]) < 25) {
                sender.sendMessage("Smallest banknote is 25 money.");
                return false;
            }
            PlayerStats playerStats = PlayerStats.getPlayerStats(((Player) sender));
            if (playerStats.getStat("money", Double.class) < Double.parseDouble(args[0])) {
                sender.sendMessage("Not enough money.");
                return false;
            }
            if (args.length > 1) {
                double banknoteValue = (Double.parseDouble(args[0]) / Integer.parseInt(args[1]));
                if (banknoteValue < 25) {
                    sender.sendMessage("Smallest banknote is 25 money.");
                    return false;
                }
                ItemStack itemStack = new ItemStack(Material.PAPER, Integer.parseInt(args[1]));
                ItemDataHandler itemDataHandler = new ItemDataHandler(itemStack, GrimmsServer.instance);
                itemDataHandler.setItemNBTData("banknoteValue", banknoteValue, PersistentDataType.DOUBLE);
                itemDataHandler.setItemLoreData(List.of("Grimmnote - " + Shared.formatNumber(banknoteValue) + " Money", "Minted by " + sender.getName(), "Minted at " + LocalDateTime.now()));
                ((Player) sender).getInventory().addItem(itemStack);
                sender.sendMessage("Gave " + args[1] + " banknotes.");
            } else {
                ItemStack itemStack = new ItemStack(Material.PAPER, 1);
                ItemDataHandler itemDataHandler = new ItemDataHandler(itemStack, GrimmsServer.instance);
                itemDataHandler.setItemNBTData("banknoteValue", Double.parseDouble(args[0]), PersistentDataType.DOUBLE);
                itemDataHandler.setItemLoreData(List.of("Grimmnote - " + Shared.formatNumber(Double.parseDouble(args[0])) + " Money", "Minted by " + sender.getName(), "Minted at " + LocalDateTime.now()));
                ((Player) sender).getInventory().addItem(itemStack);
            }
            playerStats.setStat("money", playerStats.getStat("money", Double.class) - Double.parseDouble(args[0]));
            sender.sendMessage("Withdrew " + args[0] + " money.");
            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}
