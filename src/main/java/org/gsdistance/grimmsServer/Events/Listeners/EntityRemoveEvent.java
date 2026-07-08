package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.entity.EntityType;
import org.gsdistance.grimmsServer.Manage.CustomEntityManager;

public class EntityRemoveEvent {
    public EntityRemoveEvent() {
    }

    public static void Event(org.bukkit.event.entity.EntityRemoveEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER) {
            CustomEntityManager.unregisterEntity(event.getEntity());
        }
    }
}
