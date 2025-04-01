package org.gsdistance.grimmsServer.Constructable;

import Data.MarketBaseValues;
import com.google.gson.Gson;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.ServerStats;

import java.util.HashMap;
import java.util.Map;

public class Market {
    public Map<String, Long> items;

    public Market() {
        items = new HashMap<>();
    }

    public static Market getMarket() {
        return new Gson().fromJson((String) ServerStats.getServerStats().getStat("market"), Market.class);
    }

    public void saveMarket() {
        ServerStats.getServerStats().setStat("market", new Gson().toJson(this));
    }

    public double sell(ItemStack itemStack, Player player) {
        if (player.getInventory().contains(itemStack)) {
            double sold = 0;
            items.putIfAbsent(itemStack.getType().getKey().toString(), 0L);
            for (int i = 0; i < itemStack.getAmount(); i++) {
                Object moneyObj = PlayerStats.getPlayerStats(player).getStat("money");
                double money = moneyObj instanceof Integer ? ((Integer) moneyObj).doubleValue() : (double) moneyObj;
                sold+= getPrice(itemStack.getType());
                PlayerStats.getPlayerStats(player).setStat("money", money + getPrice(itemStack.getType()));
                items.put(itemStack.getType().getKey().toString(), items.get(itemStack.getType().getKey().toString()) + 1);
            }
            player.getInventory().removeItem(itemStack);
            return sold;
        }
        return 0;
    }

    public double buy(Material item, int amount, Player player) {
        if (items.get(item.getKey().toString()) == null) {
            return 0;
        } else if (amount > items.get(item.getKey().toString())) {
            return 0;
        } else {
            double boughtP = 0;
            int bought = 0;
            for (int i = 0; i < amount; i++) {
                Object moneyObj = PlayerStats.getPlayerStats(player).getStat("money");
                double money = moneyObj instanceof Integer ? ((Integer) moneyObj).doubleValue() : (double) moneyObj;
                if (money >= getPrice(item)) {
                    PlayerStats.getPlayerStats(player).setStat("money", money - getPrice(item));
                    boughtP += getPrice(item);
                    items.put(item.getKey().toString(), items.get(item.getKey().toString()) - 1);
                    bought++;
                }
                player.getInventory().addItem(new ItemStack(item, bought));
            }
            return boughtP;
        }
    }

    public double getPrice(Material item) {
        if (items.get(item.getKey().toString()) == null) {
            return 0;
        } else {
            long amount = items.get(item.getKey().toString());
            if(MarketBaseValues.marketBaseValues.containsKey(item)){
                return Math.max(Math.max(0.25D,Math.floor(MarketBaseValues.marketBaseValues.get(item)/100)), MarketBaseValues.marketBaseValues.get(item) - Math.sqrt(amount));
            }
            return Math.max(0.25D, 25 - Math.sqrt(amount));
        }
    }
}