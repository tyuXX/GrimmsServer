package org.gsdistance.grimmsServer.Config;

import com.google.common.base.Stopwatch;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.GrimmsServer;

public class ConfigLoader {
   private static FileConfiguration config;

   public ConfigLoader() {
   }

   public static void initialize(JavaPlugin plugin) {
      File configFile = new File(plugin.getDataFolder(), "config.yml");
      if (!configFile.exists()) {
         plugin.saveDefaultConfig();
      }

      config = YamlConfiguration.loadConfiguration(configFile);
   }

   public static void loadConfigFromFile() {
      Stopwatch sw = Stopwatch.createStarted();
      GrimmsServer.logger.info("Loading GrimmsServer config...");
      if (config == null) {
         GrimmsServer.logger.warning("Config somehow not initialized. (" + String.valueOf(sw.stop()) + ")");
      } else {
         for(ConfigKey key : ConfigKey.values()) {
            Object value = config.get(key.getKey(), key.getDefaultValue());
            ActiveConfig.setConfigValue(key, value);
         }

         GrimmsServer.logger.info("GrimmsServer config loaded successfully. (" + String.valueOf(sw.stop()) + ")");
      }
   }
}
