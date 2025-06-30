package org.gsdistance.grimmsServer.Config;

import org.gsdistance.grimmsServer.GrimmsServer;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ActiveConfig {
    public static final Map<ConfigKey, Object> configValues = new HashMap<>();

    public static void setConfigValue(ConfigKey key, Object value) {
        configValues.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getConfigValue(ConfigKey key, Class<T> ignoredType) {
        try {
            return (T) configValues.getOrDefault(key, key.getDefaultValue());
        } catch (ClassCastException e) {
            GrimmsServer.logger.warning("Config value for " + key.getKey() + " is not of type " + ignoredType.getSimpleName() + ". Message: " + e.getMessage());
            try {
                return ignoredType.getDeclaredConstructor().newInstance();
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                     InstantiationException ex) {
                GrimmsServer.logger.warning("Failed to instantiate default value for " + key.getKey() + ". Message: " + ex.getMessage());
                return null;
            }
        }
    }
}