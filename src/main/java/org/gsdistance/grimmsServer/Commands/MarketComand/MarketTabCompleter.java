package org.gsdistance.grimmsServer.Commands.MarketComand;

// ...existing imports...

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.Data.Market.EnchantBaseValues;
import org.gsdistance.grimmsServer.Data.Market.MarketBaseValues;
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
            subs.add("get");
            subs.add("stock");
            subs.add("ripoff");
            subs.add("enchant");
            subs.add("tp");
            subs.add("buy");
            subs.add("sell");
            subs.add("sellall");
            subs.add("enchcosts");
            subs.add("info");
            return subs.stream().filter(s -> s.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }
        if (args.length == 2) {
            return switch (args[0].toLowerCase()) {
                case "stock", "buy" -> {
                    Market marketStock = Market.getMarket();
                    yield marketStock.items.keySet().stream().filter(name -> name.toLowerCase().contains(args[1].toLowerCase())).toList();
                }
                case "ripoff" -> MarketBaseValues.marketBaseValues.keySet().stream()
                        .map(Material::name)
                        .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
                case "enchant" -> EnchantBaseValues.enchantBaseValues.keySet().stream()
                        .map(Enchantment::getName)
                        .map(Object::toString)
                        .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
                case "tp" -> GrimmsServer.instance.getServer().getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
                default -> Collections.emptyList();
            };
        }
        if (args.length == 3 && (args[0].equalsIgnoreCase("buy") || args[0].equalsIgnoreCase("ripoff"))) {
            return Collections.singletonList("<amount>");
        }
        return Collections.emptyList();
    }
}
