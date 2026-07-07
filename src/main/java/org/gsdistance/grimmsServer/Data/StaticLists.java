package org.gsdistance.grimmsServer.Data;

import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class StaticLists {
    public static final List<EntityType> xpBlacklist = new ArrayList();

    public StaticLists() {
    }

    static {
        xpBlacklist.add(EntityType.ITEM_FRAME);
        xpBlacklist.add(EntityType.PAINTING);
        xpBlacklist.add(EntityType.MINECART);
        xpBlacklist.add(EntityType.END_CRYSTAL);
        xpBlacklist.add(EntityType.ITEM);

        for (EntityType e : EntityType.values()) {
            try {
                if (e.getKey().getKey().toLowerCase().contains("boat")) {
                    xpBlacklist.add(e);
                }
            } catch (IllegalArgumentException var5) {
            }
        }

    }
}
