package org.gsdistance.grimmsServer.Config;

import org.gsdistance.grimmsServer.GrimmsServer;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ActiveConfig {
    public static final Map<ConfigKey, Object> configValues = new HashMap();

    public ActiveConfig() {
    }

    public static void setConfigValue(ConfigKey key, Object value) {
        configValues.put(key, value);
    }

    public static <T> T getConfigValue(ConfigKey key, Class<T> ignoredType) {
        Object value = configValues.getOrDefault(key, key.getDefaultValue());

        // Handle Integer to Long conversion for config values
        if (ignoredType == Long.class && value instanceof Integer) {
            value = ((Integer) value).longValue();
        }

        try {
            return (T) value;
        } catch (ClassCastException e) {
            GrimmsServer.logger.warning("Config value for " + key.getKey() + " is not of type " + ignoredType.getSimpleName() + ". Message: " + e.getMessage());

            try {
                return ignoredType.getDeclaredConstructor().newInstance();
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                     InvocationTargetException ex) {
                GrimmsServer.logger.warning("Failed to instantiate default value for " + key.getKey() + ". Message: " + ex.getMessage());
                return null;
            }
        }
    }
}
