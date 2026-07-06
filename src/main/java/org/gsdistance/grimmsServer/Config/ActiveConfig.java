package org.gsdistance.grimmsServer.Config;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.gsdistance.grimmsServer.GrimmsServer;

public class ActiveConfig {
   public static final Map<ConfigKey, Object> configValues = new HashMap();

   public ActiveConfig() {
   }

   public static void setConfigValue(ConfigKey key, Object value) {
      configValues.put(key, value);
   }

   public static <T> T getConfigValue(ConfigKey key, Class<T> ignoredType) {
      try {
         return (T)configValues.getOrDefault(key, key.getDefaultValue());
      } catch (ClassCastException e) {
         GrimmsServer.logger.warning("Config value for " + key.getKey() + " is not of type " + ignoredType.getSimpleName() + ". Message: " + e.getMessage());

         try {
            return (T)ignoredType.getDeclaredConstructor().newInstance();
         } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            GrimmsServer.logger.warning("Failed to instantiate default value for " + key.getKey() + ". Message: " + ((ReflectiveOperationException)ex).getMessage());
            return null;
         }
      }
   }
}
