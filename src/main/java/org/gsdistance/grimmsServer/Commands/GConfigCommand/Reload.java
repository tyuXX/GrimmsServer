package org.gsdistance.grimmsServer.Commands.GConfigCommand;

import java.util.logging.Level;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Config.ConfigLoader;

public class Reload {
   public Reload() {
   }

   public static boolean subCommand() {
      Shared.Broadcast("Reloading config...", (String)null);
      ActiveConfig.configValues.clear();
      ConfigLoader.loadConfigFromFile();
      GrimmsServer.logger.log(Level.INFO, "Config reloaded.");
      Shared.Broadcast("Config reloaded successfully.", (String)null);
      return true;
   }
}
