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

public class DepositMoney implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            ItemStack itemStack = ((Player) sender).getInventory().getItemInMainHand();
            ItemDataHandler itemDataHandler = new ItemDataHandler(itemStack, GrimmsServer.instance);
            Double banknoteValue = itemDataHandler.getItemNBTData("banknoteValue", PersistentDataType.DOUBLE);
            if (banknoteValue == null) {
                sender.sendMessage("Not a banknote.");
                return false;
            }
            PlayerStats playerStats = PlayerStats.getPlayerStats((Player) sender);
            playerStats.setStat("money", playerStats.getStat("money", Double.class) + (banknoteValue * itemStack.getAmount()));
            ((Player) sender).getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            sender.sendMessage("Deposited " + Shared.formatNumber((banknoteValue * itemStack.getAmount())) + " money.");
            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}
