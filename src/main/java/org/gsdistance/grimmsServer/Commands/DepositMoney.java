package org.gsdistance.grimmsServer.Commands;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class DepositMoney implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            ItemStack itemStack = ((Player)sender).getInventory().getItemInMainHand();
            ItemMeta itemMeta = itemStack.getItemMeta();
            if(!itemMeta.getPersistentDataContainer().has(new NamespacedKey(GrimmsServer.instance,"banknoteValue"),PersistentDataType.DOUBLE)){
                sender.sendMessage("Not a banknote.");
                return false;
            }
            PlayerStats playerStats = PlayerStats.getPlayerStats((Player) sender);
            playerStats.setStat("money",(Double)playerStats.getStat("money") + (itemMeta.getPersistentDataContainer().getOrDefault(new NamespacedKey(GrimmsServer.instance,"banknoteValue"),PersistentDataType.DOUBLE, 0.0)*itemStack.getAmount()));
            ((Player)sender).getInventory().remove(itemStack) ;
            sender.sendMessage("Deposited " + (itemMeta.getPersistentDataContainer().getOrDefault(new NamespacedKey(GrimmsServer.instance,"banknoteValue"),PersistentDataType.DOUBLE, 0.0)*itemStack.getAmount()) + " money.");
            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}
