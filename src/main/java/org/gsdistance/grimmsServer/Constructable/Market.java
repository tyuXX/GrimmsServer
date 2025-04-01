package org.gsdistance.grimmsServer.Constructable;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import java.util.Dictionary;

public class Market {
    public Dictionary<String, Long> items;
    public Market() {
        items = new java.util.Hashtable<>();
    }
    public boolean sell(ItemStack itemStack, Player player) {
        if(player.getInventory().contains(itemStack)) {
            items.put(itemStack.getType().getKey().toString(), 0L);
            for (int i = 0; i < itemStack.getAmount(); i++) {
                PlayerStats.getPlayerStats(player).setStat("money", (double)PlayerStats.getPlayerStats(player).getStat("money") + getPrice(itemStack.getType()));
                items.put(itemStack.getType().getKey().toString(), items.get(itemStack.getType().getKey().toString()) + 1);
            }
            player.getInventory().removeItem(itemStack);
            return true;
        }
        return false;
    }
    public boolean buy(Material item, int amount, Player player) {
        if (items.get(item.getKey().toString()) == null) {
            return false;
        } else {
            int bought = 0;
            for (int i = 0; i < amount; i++) {
                if((double)PlayerStats.getPlayerStats(player).getStat("money") >= getPrice(item)) {
                    PlayerStats.getPlayerStats(player).setStat("money", (double)PlayerStats.getPlayerStats(player).getStat("money") - getPrice(item));
                    items.put(item.getKey().toString(), items.get(item.getKey().toString()) - 1);
                    bought++;
                }
                player.getInventory().addItem(new ItemStack(item, bought));
            }
            return true;
        }
    }
    public double getPrice(Material item) {
        if(items.get(item.getKey().toString()) == null) {
            return 0;
        } else {
            long amount = items.get(item.getKey().toString());
            return switch (ItemRarity.valueOf(item.getKey().toString())) {
                case COMMON -> Math.max(1, 25 - Math.sqrt(amount));
                case UNCOMMON -> Math.max(1, 200 - Math.sqrt(amount));
                case RARE -> Math.max(1, 600 - Math.sqrt(amount));
                case EPIC -> Math.max(1, 1200 - Math.sqrt(amount));
            };
        }
    }
}
