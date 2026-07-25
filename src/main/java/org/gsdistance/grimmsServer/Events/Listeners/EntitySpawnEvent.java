package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.entity.EntityType;
import org.gsdistance.grimmsServer.Constructable.Entity.EntityMetadata;
import org.gsdistance.grimmsServer.Manage.CustomEntityManager;

public class EntitySpawnEvent {
    public EntitySpawnEvent() {
    }

    public static void Event(org.bukkit.event.entity.EntitySpawnEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER) {
            // Skip if this is a champion spawn (handled by SpawnChampion command)
            if (event.getEntity().hasMetadata("champion_spawn")) {
                event.getEntity().removeMetadata("champion_spawn", org.gsdistance.grimmsServer.GrimmsServer.instance);
                return;
            }

            CustomEntityManager.registerEntity(event.getEntity());
            // getEntityMetadata handles both new entities (creates & levels) and chunk reloads (loads from disk without releveling)
            EntityMetadata.getEntityMetadata(event.getEntity());
        }
    }
}
