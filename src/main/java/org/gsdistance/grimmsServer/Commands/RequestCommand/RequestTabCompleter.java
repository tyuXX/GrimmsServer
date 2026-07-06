package org.gsdistance.grimmsServer.Commands.RequestCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;

public class RequestTabCompleter implements TabCompleter {
   public RequestTabCompleter() {
   }

   public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
      Object requestData = ((Data)PerSessionDataStorage.dataStore.get("requestData-" + sender.getName())).key();
      return requestData == null ? Collections.emptyList() : ((ArrayList)requestData).stream().map(Object::toString).filter((s) -> ((String)s).startsWith(args.length > 0 ? args[0] : "")).toList();
   }
}
