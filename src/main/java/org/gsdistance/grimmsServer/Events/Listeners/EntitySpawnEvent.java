package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.entity.EntityType;
import org.gsdistance.grimmsServer.Constructable.Entity.EntityMetadata;
import org.gsdistance.grimmsServer.Manage.CustomEntityManager;

public class EntitySpawnEvent {
    public EntitySpawnEvent() {
    }

    public static void Event(org.bukkit.event.entity.EntitySpawnEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER) {
            CustomEntityManager.registerEntity(event.getEntity());
            EntityMetadata.getEntityMetadata(event.getEntity());
        }
    }
}
