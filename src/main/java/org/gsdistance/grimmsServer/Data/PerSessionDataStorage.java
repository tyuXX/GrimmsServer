package org.gsdistance.grimmsServer.Data;

import org.gsdistance.grimmsServer.Constructable.Data;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PerSessionDataStorage {
    public static final Map<String, Data<Object, Type>> dataStore = new HashMap<>();
    public static Double tpCost = 500.0;
}
