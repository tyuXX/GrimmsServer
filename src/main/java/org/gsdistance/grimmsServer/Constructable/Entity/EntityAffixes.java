package org.gsdistance.grimmsServer.Constructable.Entity;

import java.util.Map;

public class EntityAffixes {
    public static final Map<EntityAffix, Integer> affixes = Map.ofEntries(
            Map.entry(new EntityAffix("test", 0, (entity) -> null), 99)
    );
}
