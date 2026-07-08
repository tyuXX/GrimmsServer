package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Entity.EntityMetadata;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerLevelHandler;
import org.gsdistance.grimmsServer.Events.Registers.CustomEntityDeathRegister;
import org.gsdistance.grimmsServer.Manage.CustomEntityManager;

public class CustomEntityDeathEvent {
    public CustomEntityDeathEvent() {
    }

    public static void Event(CustomEntityDeathRegister event) {
        if (event.getEntity().getType() == EntityType.PLAYER) {
            return;
        }

        CustomEntityManager.currentRegistry.remove(event.getEntity().getUniqueId());

        if (event.getKiller() instanceof Player) {
            EntityMetadata metadata = EntityMetadata.getEntityMetadata(event.getEntity());
            if (metadata != null) {
                double extraXp = Math.sqrt(metadata.level);
                PlayerLevelHandler.getLevelHandler((Player) event.getKiller()).addExp(extraXp);
            }
        }
    }
}
