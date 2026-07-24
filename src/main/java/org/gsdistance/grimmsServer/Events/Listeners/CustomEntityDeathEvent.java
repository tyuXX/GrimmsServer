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
        CustomEntityManager.saveToFile();

        // Get metadata before deleting for XP calculation
        EntityMetadata metadata = EntityMetadata.getEntityMetadata(event.getEntity());
        if (metadata != null) {
            if (event.getKiller() instanceof Player) {
                double extraXp = Math.sqrt(metadata.level) * Math.cbrt(metadata.prestige);
                PlayerLevelHandler.getLevelHandler((Player) event.getKiller()).addExp(extraXp);
            }
            // Delete entity metadata file to prevent stale data
            metadata.deleteFromFile();
        }
    }
}
