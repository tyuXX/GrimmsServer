package org.gsdistance.grimmsServer.Commands.RequestCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RequestTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        Map<Object, Type> sessionData = PerSessionDataStorage.dataStore.get("requestData-" + sender.getName());
        if (sessionData == null) {
            return Collections.emptyList();
        }

        return ((ArrayList<Integer>) (sessionData.keySet().toArray()[0])).stream()
                .map(Object::toString) // Convert Integer to String
                .filter(s -> s.startsWith(args.length > 0 ? args[0] : "")) // Filter by prefix
                .toList();
    }
}
