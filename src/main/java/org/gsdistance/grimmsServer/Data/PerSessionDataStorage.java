package org.gsdistance.grimmsServer.Data;

import org.gsdistance.grimmsServer.Constructable.Data;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PerSessionDataStorage {
    public static final Map<String, Data<Object, Type>> dataStore = new ConcurrentHashMap<>();
    public static Double tpCost = (double) 500.0F;

    public PerSessionDataStorage() {
    }

    public static <T> void softSave(T data, Class<T> ignoredType, String key) {
        dataStore.put(key, Data.of(data, ignoredType));
    }

    public static <T> T getData(String key, Class<T> type) {
        Data<Object, Type> data = dataStore.get(key);
        return data != null && type.isInstance(data.key()) ? type.cast(data.key()) : null;
    }
}
