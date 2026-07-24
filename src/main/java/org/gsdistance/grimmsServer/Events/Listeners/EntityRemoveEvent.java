package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.entity.EntityType;
import org.gsdistance.grimmsServer.Constructable.Entity.EntityMetadata;
import org.gsdistance.grimmsServer.Manage.CustomEntityManager;

public class EntityRemoveEvent {
    public EntityRemoveEvent() {
    }

    public static void Event(org.bukkit.event.entity.EntityRemoveEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER) {
            CustomEntityManager.unregisterEntity(event.getEntity());
            CustomEntityManager.saveToFile();
            // Delete entity metadata file to prevent stale data on despawn
            EntityMetadata metadata = EntityMetadata.getEntityMetadata(event.getEntity());
            if (metadata != null) {
                metadata.deleteFromFile();
            }
        }
    }
}
