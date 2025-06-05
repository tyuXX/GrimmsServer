package org.gsdistance.grimmsServer.Commands.HomeCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Data.PlayerMetadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HomeTabCompleter implements org.bukkit.command.TabCompleter {
    private static final List<String> SUBCOMMANDS = Arrays.asList("sethome", "tp", "homes", "delhome");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> result = new ArrayList<>();
            for (String sub : SUBCOMMANDS) {
                if (sub.startsWith(args[0].toLowerCase())) {
                    result.add(sub);
                }
            }
            return result;
        }
        if (args.length == 2 && sender instanceof Player) {
            String sub = args[0].toLowerCase();
            if (sub.equals("tp") || sub.equals("delhome")) {
                PlayerMetadata meta = PlayerMetadata.getPlayerMetadata((Player) sender);
                List<String> homes = new ArrayList<>(meta.homes.keySet());
                homes.removeIf(h -> !h.startsWith(args[1].toLowerCase()));
                return homes;
            }
        }
        return Collections.emptyList();
    }
}