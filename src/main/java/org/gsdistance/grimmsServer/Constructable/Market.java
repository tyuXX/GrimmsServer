package org.gsdistance.grimmsServer.Constructable;

import com.google.gson.Gson;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Data.MarketBaseValues;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.ServerStats;

import java.util.HashMap;
import java.util.Map;

public class Market {
    public final Map<String, Long> items;

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
            PlayerStats playerStats = PlayerStats.getPlayerStats(player);
            items.putIfAbsent(itemStack.getType().getKey().toString(), 0L);
            double sold = 0.0;
            for (int i = 0; i < itemStack.getAmount(); i++) {
                sold += getPrice(itemStack.getType());
            }
            items.put(itemStack.getType().getKey().toString(), items.get(itemStack.getType().getKey().toString()) + itemStack.getAmount());
            playerStats.setStat("money", playerStats.getStat("money", Double.class) + sold);
            player.getInventory().removeItem(itemStack);
            return sold;
        }
        return 0;
    }

    public double unsafeSell(ItemStack itemStack, Player player){
        PlayerStats playerStats = PlayerStats.getPlayerStats(player);
        items.putIfAbsent(itemStack.getType().getKey().toString(), 0L);
        double sold = 0.0;
        for (int i = 0; i < itemStack.getAmount(); i++) {
            sold += getPrice(itemStack.getType());
        }
        items.put(itemStack.getType().getKey().toString(), items.get(itemStack.getType().getKey().toString()) + itemStack.getAmount());
        playerStats.setStat("money", playerStats.getStat("money", Double.class) + sold);
        return sold;
    }

    public Data<Double, Integer> sellAll(Material item, Player player) {
        if (player.getInventory().contains(item)) {
            PlayerStats playerStats = PlayerStats.getPlayerStats(player);
            items.putIfAbsent(item.getKey().toString(), 0L);
            Double sold = 0.0;
            int amount = player.getInventory().all(item).values().stream()
                    .mapToInt(ItemStack::getAmount)
                    .sum();
            for (int i = 0; i < amount; i++) {
                sold += getPrice(item);
            }
            items.put(item.getKey().toString(), items.get(item.getKey().toString()) + amount);
            playerStats.setStat("money", playerStats.getStat("money", Double.class) + sold);
            player.getInventory().remove(item);
            return Data.of(sold, amount);
        }
        return Data.of(0.0, 0);
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
                Double money = PlayerStats.getPlayerStats(player).getStat("money", Double.class);
                if (money >= getPrice(item)) {
                    PlayerStats.getPlayerStats(player).setStat("money", money - getPrice(item));
                    boughtP += getPrice(item);
                    items.put(item.getKey().toString(), items.get(item.getKey().toString()) - 1);
                    bought++;
                } else {
                    break;
                }
            }
            player.getInventory().addItem(new ItemStack(item, bought));
            return boughtP;
        }
    }

    public double getPrice(Material item) {
        if (items.get(item.getKey().toString()) == null) {
            return 0;
        } else {
            long amount = items.get(item.getKey().toString());
            double rt;
            if (MarketBaseValues.marketBaseValues.containsKey(item)) {
                rt = Math.max(Math.max(0.25D, Math.floor(MarketBaseValues.marketBaseValues.get(item) / 100)), MarketBaseValues.marketBaseValues.get(item) - Math.sqrt(amount));
            } else {
                rt = Math.max(0.25D, 25 - Math.sqrt(amount));
            }
            return rt;
        }
    }
}