package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.entity.LivingEntity;
import org.gsdistance.grimmsServer.Constructable.Entity.EntityMetadata;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Manage.CustomEntityManager;

public class ChampionTickEvent {
    public ChampionTickEvent() {
    }

    public static void Event() {
        // Only process entities in loaded chunks to avoid unnecessary processing
        for (java.util.UUID entityUuid : CustomEntityManager.currentRegistry) {
            org.bukkit.entity.Entity entity = GrimmsServer.instance.getServer().getEntity(entityUuid);
            if (entity instanceof LivingEntity && entity.isValid()) {
                EntityMetadata metadata = EntityMetadata.getEntityMetadata(entity);
                if (metadata != null && metadata.championTier > 0 && !metadata.abilities.isEmpty()) {
                    metadata.executeAbilities((LivingEntity) entity, metadata, ServerTickEvent.ticks);
                    // Update bossbar health
                    EntityMetadata.updateBossBarHealth((LivingEntity) entity, metadata);
                }
            }
        }
    }
}
