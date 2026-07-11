package org.gsdistance.grimmsServer.Constructable;

import com.google.gson.Gson;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Config.ConfigKey;
import org.gsdistance.grimmsServer.Data.Market.EnchantBaseValues;
import org.gsdistance.grimmsServer.Data.Market.MarketBaseValues;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.ServerStats;

import java.util.HashMap;
import java.util.Map;

public class Market {
    public double NegMarketSaturation = 0.0F;
    public final Map<String, Long> items = new HashMap();

    public Market() {
    }

    public static Market getMarket() {
        return (new Gson()).fromJson((String) ServerStats.getServerStats().getStat("market"), Market.class);
    }

    public void saveMarket() {
        ServerStats.getServerStats().setStat("market", (new Gson()).toJson(this));
    }

    public double sell(ItemStack itemStack, Player player) {
        if (!player.getInventory().contains(itemStack)) {
            return 0.0F;
        } else {
            PlayerStats playerStats = PlayerStats.getPlayerStats(player);
            this.items.putIfAbsent(itemStack.getType().getKey().getKey(), 0L);
            long initialAmount = this.items.get(itemStack.getType().getKey().getKey());
            double sold = 0.0F;

            for (int i = 0; i < itemStack.getAmount(); ++i) {
                sold += this.getISPriceWithAmount(itemStack.getType(), itemStack.getEnchantments(), initialAmount + i);
            }

            this.items.put(itemStack.getType().getKey().getKey(), initialAmount + (long) itemStack.getAmount());
            playerStats.setStat("money", playerStats.getStat("money", Double.class) + sold);
            player.getInventory().removeItem(itemStack);
            return sold;
        }
    }

    public void unsafeSell(ItemStack itemStack, Player player) {
        PlayerStats playerStats = PlayerStats.getPlayerStats(player);
        this.items.putIfAbsent(itemStack.getType().getKey().getKey(), 0L);
        double sold = 0.0F;

        for (int i = 0; i < itemStack.getAmount(); ++i) {
            sold += this.getISPrice(itemStack.getType(), itemStack.getEnchantments());
        }

        this.items.put(itemStack.getType().getKey().getKey(), this.items.get(itemStack.getType().getKey().getKey()) + (long) itemStack.getAmount());
        playerStats.setStat("money", playerStats.getStat("money", Double.class) + sold);
    }

    public Data<Double, Integer> sellAll(Material item, Player player) {
        if (!player.getInventory().contains(item)) {
            return Data.of((double) 0.0F, 0);
        } else {
            PlayerStats playerStats = PlayerStats.getPlayerStats(player);
            this.items.putIfAbsent(item.getKey().getKey(), 0L);
            long initialAmount = this.items.get(item.getKey().getKey());
            Double sold = (double) 0.0F;
            int amount = player.getInventory().all(item).values().stream().mapToInt(ItemStack::getAmount).sum();

            for (int i = 0; i < amount; ++i) {
                sold = sold + this.getPriceWithAmount(item, initialAmount + i);
            }

            this.items.put(item.getKey().getKey(), initialAmount + (long) amount);
            playerStats.setStat("money", playerStats.getStat("money", Double.class) + sold);
            player.getInventory().remove(item);
            return Data.of(sold, amount);
        }
    }

    public double buy(Material item, int amount, Player player) {
        if (this.items.get(item.getKey().getKey()) == null) {
            return 0.0F;
        } else if ((long) amount > this.items.get(item.getKey().getKey())) {
            return 0.0F;
        } else {
            long initialAmount = this.items.get(item.getKey().getKey());
            
            // Calculate total buy cost (prices at current market state, then decreasing amounts)
            double totalBuyCost = 0.0;
            for (int i = 0; i < amount; ++i) {
                totalBuyCost += this.getPriceWithAmount(item, initialAmount - i);
            }
            
            // Add a 50% tax to ensure buy cost is always significantly higher than any potential sell revenue
            double tax = totalBuyCost * 0.5;
            double totalCostWithTax = totalBuyCost + tax;
            
            Double money = PlayerStats.getPlayerStats(player).getStat("money", Double.class);
            if (!(money >= totalCostWithTax)) {
                return 0.0F;
            }

            PlayerStats.getPlayerStats(player).setStat("money", money - totalCostWithTax);
            this.items.put(item.getKey().getKey(), initialAmount - amount);
            player.getInventory().addItem(new ItemStack(item, amount));
            
            return totalCostWithTax;
        }
    }

    public double reCalcNegMarketSaturation() {
        double totalValue = 0.0F;

        for (String string : this.items.keySet()) {
            Material material = Material.matchMaterial(string);
            if (material != null) {
                long amount = this.items.get(string);
                Double price = MarketBaseValues.marketBaseValues.get(material);
                if (price != null) {
                    totalValue += (double) amount * price;
                } else {
                    totalValue += (double) amount * (double) 0.25F;
                }
            }
        }

        this.NegMarketSaturation = Math.floor(Math.sqrt(Math.sqrt(totalValue)));
        return this.NegMarketSaturation;
    }

    public double getPrice(Material item) {
        if (this.items.get(item.getKey().getKey()) == null) {
            return 0.0F;
        } else {
            return this.getPriceWithAmount(item, this.items.get(item.getKey().getKey()));
        }
    }

    public double getPriceWithAmount(Material item, long amount) {
        double minPrice = ActiveConfig.getConfigValue(ConfigKey.MARKET_MIN_PRICE, Double.class);
        double rt;
        if (MarketBaseValues.marketBaseValues.containsKey(item)) {
            rt = Math.max(Math.max(minPrice, Math.floor(MarketBaseValues.marketBaseValues.get(item) / (double) 100.0F)), MarketBaseValues.marketBaseValues.get(item) + this.NegMarketSaturation - Math.sqrt((double) amount));
        } else {
            rt = Math.max(minPrice, this.NegMarketSaturation / (double) 2.0F - Math.sqrt((double) amount));
        }

        return rt;
    }

    public double getISPrice(Material item, Map<Enchantment, Integer> enchantments) {
        double rt = this.getPrice(item);

        for (Enchantment e : enchantments.keySet()) {
            Double enchantValue = EnchantBaseValues.enchantBaseValues.get(e);
            if (enchantValue != null) {
                rt += enchantValue * Math.sqrt(enchantments.get(e));
            }
        }

        return rt;
    }

    public double getISPriceWithAmount(Material item, Map<Enchantment, Integer> enchantments, long amount) {
        double rt = this.getPriceWithAmount(item, amount);

        for (Enchantment e : enchantments.keySet()) {
            Double enchantValue = EnchantBaseValues.enchantBaseValues.get(e);
            if (enchantValue != null) {
                rt += enchantValue * Math.sqrt(enchantments.get(e));
            }
        }

        return rt;
    }
}
