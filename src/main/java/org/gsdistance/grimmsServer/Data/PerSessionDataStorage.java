package org.gsdistance.grimmsServer.Data;

import java.util.Map;

public class PerSessionDataStorage {
    public static final Map<String, Object> dataStore = Map.ofEntries(
            Map.entry("defaultData", new Object())
    );
}
