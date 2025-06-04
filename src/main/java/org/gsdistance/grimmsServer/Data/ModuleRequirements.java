package org.gsdistance.grimmsServer.Data;

import java.util.Map;

public class ModuleRequirements {
    public static final Map<String, String> CommandRequirements;
    static {
        CommandRequirements = Map.ofEntries(
                Map.entry("","job")
        );
    }
}
