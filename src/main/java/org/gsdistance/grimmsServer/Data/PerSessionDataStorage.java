package org.gsdistance.grimmsServer.Data;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PerSessionDataStorage {
    public static Map<String, Map<Object, Type>> dataStore = new HashMap<>();
    public static Double tpCost = 500.0;
}
