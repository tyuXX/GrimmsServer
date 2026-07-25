package org.gsdistance.grimmsServer.Constructable.Entity;

import java.util.Map;

public class EntityAbilities {
    // Key - Ability
    // Value - Tier
    public static final Map<EntityAbility, Integer> abilities = Map.ofEntries(
            Map.entry(new EntityAbility("test", Integer.MAX_VALUE, (entity) -> null), 99)
    );
    // Could add abilities such as heal self, reflect damage, summon reinforcements etc.
}
