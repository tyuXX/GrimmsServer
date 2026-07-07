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

import java.time.LocalDateTime;
import java.util.List;

public class WithdrawMoney implements CommandExecutor {
    private static final double MIN_BANKNOTE_VALUE = 25.0;

    public WithdrawMoney() {
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
            return false;
        }
        
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /withdrawMoney <amount> [banknoteCount]");
            return false;
        }
        
        double amount;
        try {
            amount = Double.parseDouble(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid amount: '" + args[0] + "'. Must be a number.");
            return false;
        }
        
        if (amount < MIN_BANKNOTE_VALUE) {
            sender.sendMessage(ChatColor.RED + "Smallest banknote is " + ChatColor.GOLD + MIN_BANKNOTE_VALUE + ChatColor.RED + " money.");
            return false;
        }
        
        PlayerStats playerStats = PlayerStats.getPlayerStats((Player) sender);
        double currentBalance = playerStats.getStat("money", Double.class);
        
        if (currentBalance < amount) {
            sender.sendMessage(ChatColor.RED + "Not enough money. Current balance: " + ChatColor.GOLD + Shared.formatNumber(currentBalance));
            return false;
        }
        
        int banknoteCount = 1;
        double banknoteValue = amount;
        
        if (args.length > 1) {
            try {
                banknoteCount = Integer.parseInt(args[1]);
                if (banknoteCount <= 0) {
                    sender.sendMessage(ChatColor.RED + "Banknote count must be greater than 0.");
                    return false;
                }
                banknoteValue = amount / banknoteCount;
                if (banknoteValue < MIN_BANKNOTE_VALUE) {
                    sender.sendMessage(ChatColor.RED + "With these parameters, each banknote would be " + ChatColor.GOLD + Shared.formatNumber(banknoteValue) + ChatColor.RED + ". Minimum is " + ChatColor.GOLD + MIN_BANKNOTE_VALUE);
                    return false;
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid banknote count: '" + args[1] + "'. Must be a whole number.");
                return false;
            }
        }
        
        ItemStack itemStack = new ItemStack(Material.PAPER, banknoteCount);
        ItemDataHandler itemDataHandler = new ItemDataHandler(itemStack, GrimmsServer.instance);
        itemDataHandler.setItemNBTData("banknoteValue", banknoteValue, PersistentDataType.DOUBLE);
        itemDataHandler.setItemLoreData(List.of(
            ChatColor.GOLD + "Grimmnote - " + Shared.formatNumber(banknoteValue) + " Money",
            ChatColor.GRAY + "Minted by " + sender.getName(),
            ChatColor.GRAY + "Minted at " + LocalDateTime.now()
        ));
        
        ((Player) sender).getInventory().addItem(itemStack);
        playerStats.setStat("money", currentBalance - amount);
        
        if (banknoteCount > 1) {
            sender.sendMessage(ChatColor.GREEN + "Withdrew " + ChatColor.GOLD + Shared.formatNumber(amount) + ChatColor.GREEN + " into " + ChatColor.YELLOW + banknoteCount + ChatColor.GREEN + " banknotes of " + ChatColor.GOLD + Shared.formatNumber(banknoteValue) + ChatColor.GREEN + " each.");
        } else {
            sender.sendMessage(ChatColor.GREEN + "Withdrew " + ChatColor.GOLD + Shared.formatNumber(amount) + ChatColor.GREEN + " into a banknote.");
        }
        
        return true;
    }
}
