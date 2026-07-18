package org.gsdistance.grimmsServer.Commands;

import org.bukkit.ChatColor;
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
import org.jetbrains.annotations.NotNull;

public class DepositMoney implements CommandExecutor {
    public DepositMoney() {
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
            return false;
        }

        ItemStack itemStack = ((Player) sender).getInventory().getItemInMainHand();

        if (itemStack.getType() == Material.AIR) {
            sender.sendMessage(ChatColor.RED + "You are not holding anything to deposit.");
            return false;
        }

        ItemDataHandler itemDataHandler = new ItemDataHandler(itemStack, GrimmsServer.instance);
        Double banknoteValue = itemDataHandler.getItemNBTData("banknoteValue", PersistentDataType.DOUBLE);

        if (banknoteValue == null) {
            sender.sendMessage(ChatColor.RED + "This item is not a valid banknote.");
            return false;
        }

        PlayerStats playerStats = PlayerStats.getPlayerStats((Player) sender);
        double totalValue = banknoteValue * itemStack.getAmount();
        double currentBalance = playerStats.getStat("money", Double.class);

        playerStats.setStat("money", currentBalance + totalValue);
        ((Player) sender).getInventory().setItemInMainHand(new ItemStack(Material.AIR));

        sender.sendMessage(ChatColor.GREEN + "Deposited " + ChatColor.GOLD + Shared.formatNumber(totalValue) + ChatColor.GREEN + ". New balance: " + ChatColor.GOLD + Shared.formatNumber(currentBalance + totalValue));
        return true;
    }
}
