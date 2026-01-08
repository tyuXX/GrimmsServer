package org.gsdistance.grimmsServer.Data;

import org.gsdistance.grimmsServer.Constructable.Data;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PerSessionDataStorage {
    public static final Map<String, Data<Object, Type>> dataStore = new HashMap<>();
    public static Double tpCost = 500.0;

    public static <T> void softSave(T data ,Class<T> ignoredType, String key) {
        dataStore.put(key, Data.of(data, ignoredType));
    }

    public static <T> T getData(String key, Class<T> type) {
        Data<Object, Type> data = dataStore.get(key);
        if (data != null && type.isInstance(data.key)) {
            return type.cast(data.key);
        }
        return null; // or throw an exception if preferred
    }
}
