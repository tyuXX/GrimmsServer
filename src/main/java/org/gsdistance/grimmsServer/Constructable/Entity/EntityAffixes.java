package org.gsdistance.grimmsServer.Constructable.Entity;

import java.util.Map;

public class EntityAffixes {
    // Key - Affix
    // Value - Tier
    public static final Map<EntityAffix, Integer> affixes = Map.ofEntries(
            Map.entry(new EntityAffix("test", (entity) -> null), 99)
    );

    // Could add affixes such as health bonus, armor bonus and damage bonus
}
