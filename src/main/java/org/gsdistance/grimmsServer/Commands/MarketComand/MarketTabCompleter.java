package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.Data.CustomEnchantment;
import org.gsdistance.grimmsServer.Data.Market.EnchantBaseValues;
import org.gsdistance.grimmsServer.Data.Market.MarketBaseValues;
import org.gsdistance.grimmsServer.Data.Market.PotionEffectBaseValues;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MarketTabCompleter implements TabCompleter {
    public MarketTabCompleter() {
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> subs = new ArrayList<>();
            subs.add("get");
            subs.add("stock");
            subs.add("ripoff");
            subs.add("enchant");
            subs.add("customenchant");
            subs.add("potioneffect");
            subs.add("tp");
            subs.add("buy");
            subs.add("sell");
            subs.add("sellall");
            subs.add("enchcosts");
            subs.add("info");
            subs.add("gui");
            return subs.stream().filter((s) -> s.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        } else if (args.length == 2) {
            return switch (args[0].toLowerCase()) {
                case "stock":
                case "buy":
                    Market marketStock = Market.getMarket();
                    yield marketStock.items.keySet().stream().filter((name) -> name.toLowerCase().contains(args[1].toLowerCase())).collect(Collectors.toList());
                case "ripoff":
                    yield MarketBaseValues.marketBaseValues.keySet().stream().map(Material::name).filter((name) -> name.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
                case "enchant":
                    yield EnchantBaseValues.enchantBaseValues.keySet().stream().map((enchantment) -> enchantment.getKey().getKey()).map(Object::toString).filter((name) -> name.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
                case "customenchant":
                    yield EnchantBaseValues.customEnchantBaseValues.keySet().stream().map(CustomEnchantment::name).map(String::toLowerCase).filter((name) -> name.startsWith(args[1].toLowerCase())).collect(Collectors.toList());
                case "potioneffect":
                    yield PotionEffectBaseValues.potionEffectBaseValues.keySet().stream().map(PotionEffectType::getName).filter((name) -> name.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
                case "tp":
                    yield GrimmsServer.instance.getServer().getOnlinePlayers().stream().map(Player::getName).filter((name) -> name.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
                default:
                    yield Collections.emptyList();
            };
        } else if (args.length != 3 || !args[0].equalsIgnoreCase("buy") && !args[0].equalsIgnoreCase("ripoff")) {
            if (args.length == 3 && args[0].equalsIgnoreCase("potioneffect")) {
                return Collections.singletonList("<duration>");
            } else {
                return args.length == 4 && args[0].equalsIgnoreCase("potioneffect") ? Collections.singletonList("<level>") : Collections.emptyList();
            }
        } else {
            return Collections.singletonList("<amount>");
        }
    }
}
