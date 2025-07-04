package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Constructable.Item.RelicHandler;

public class Relic {
    public static boolean subCommand(Player player, String[] args){
        if(!player.hasPermission("grimmsserver.util.admin")){
            player.sendMessage("You do not have permission to use this command.");
            return false;
        }
        if (args.length < 2) {
            return false;
        }
        String action = args[1].toLowerCase();
        switch (action.toLowerCase()){
            case "make" -> {
                ItemStack item = player.getInventory().getItemInMainHand();
                if(!RelicHandler.isRelic(item)){
                    RelicHandler.makeRelic(item);
                }
                player.sendMessage("Made " + item.getType() + " a relic item.");
                return true;
            }
            case "set" -> {
                if (args.length < 5) {
                    return false;
                }
                ItemStack item = player.getInventory().getItemInMainHand();
                if (!RelicHandler.isRelic(item)) {
                    player.sendMessage("You must hold a relic item in your hand to set its ID.");
                    return false;
                }
                RelicHandler relicHandler = RelicHandler.getRelicHandler(item);
                relicHandler.setRelicStats(Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
                player.sendMessage("Set relic stats for " + item.getType() + " to tier " + args[2] + " and grade " + args[3] + " with resistance value " + args[4]);
                return true;
            }
            case "recalc" -> {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (!RelicHandler.isRelic(item)) {
                    player.sendMessage("You must hold a relic item in your hand to recalculate its stats.");
                    return false;
                }
                RelicHandler.getRelicHandler(item).recalculateBaseAttributes();
                player.sendMessage("Recalculated relic stats for " + item.getType());
                return true;
            }
            case "reroll" -> {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (!RelicHandler.isRelic(item)) {
                    player.sendMessage("You must hold a relic item in your hand to reroll its stats.");
                    return false;
                }
                RelicHandler.getRelicHandler(item).initRandomRelicStats();
                player.sendMessage("Rerolled relic stats for " + item.getType());
                return true;
            }
            default -> {
                player.sendMessage("Unknown relic command: " + action);
                return false;
            }
        }
    }
}
