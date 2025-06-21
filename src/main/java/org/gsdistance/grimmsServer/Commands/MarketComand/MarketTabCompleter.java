package org.gsdistance.grimmsServer.Commands.MarketComand;

// ...existing imports...

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.Data.EnchantBaseValues;
import org.gsdistance.grimmsServer.Data.MarketBaseValues;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MarketTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> subs = new ArrayList<>();
            if (sender.hasPermission("grimmsserver.market.get")) subs.add("get");
            if (sender.hasPermission("grimmsserver.market.stock")) subs.add("stock");
            if (sender.hasPermission("grimmsserver.market.ripoff")) subs.add("ripoff");
            if (sender.hasPermission("grimmsserver.market.enchant")) subs.add("enchant");
            if (sender.hasPermission("grimmsserver.market.tp")) subs.add("tp");
            if (sender.hasPermission("grimmsserver.market.buy")) subs.add("buy");
            if (sender.hasPermission("grimmsserver.market.sell")) subs.add("sell");
            return subs.stream().filter(s -> s.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }
        if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "stock":
                    Market market = Market.getMarket();
                    List<String> items = new ArrayList<>(market.items.keySet());
                    return items.stream().filter(i -> i.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
                case "ripoff":
                    return MarketBaseValues.marketBaseValues.keySet().stream()
                            .map(Material::name)
                            .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                            .collect(Collectors.toList());
                case "buy":
                    Market marketBuy = Market.getMarket();
                    return marketBuy.items.keySet().stream().toList();
                case "enchant":
                    return EnchantBaseValues.enchantBaseValues.keySet().stream()
                            .map(Enchantment::getName)
                            .map(Object::toString)
                            .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                            .collect(Collectors.toList());
                case "tp":
                    return GrimmsServer.instance.getServer().getOnlinePlayers().stream()
                            .map(Player::getName)
                            .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                            .collect(Collectors.toList());
                default:
                    return Collections.emptyList();
            }
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("buy")) {
            return Collections.singletonList("<amount>");
        }
        return Collections.emptyList();
    }
}
