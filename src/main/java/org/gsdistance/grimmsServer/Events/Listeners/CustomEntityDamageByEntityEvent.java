package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Entity.EntityAbility;
import org.gsdistance.grimmsServer.Constructable.Entity.EntityMetadata;
import org.gsdistance.grimmsServer.Constructable.Item.ItemLevelHandler;
import org.gsdistance.grimmsServer.Data.StaticLists;
import org.gsdistance.grimmsServer.Events.Registers.CustomEntityDamageByEntityRegister;

public class CustomEntityDamageByEntityEvent {
    public CustomEntityDamageByEntityEvent() {
    }

    public static void Event(CustomEntityDamageByEntityRegister event) {
        // This event is only called for non-player entities being damaged
        org.bukkit.event.entity.EntityDamageByEntityEvent originalEvent = event.getOriginalEvent();

        // Handle champion abilities
        EntityMetadata metadata = EntityMetadata.getEntityMetadata(originalEvent.getEntity());
        if (metadata != null && metadata.championTier > 0) {
            // Check for reflect_damage ability
            for (EntityAbility ability : metadata.abilities) {
                if (ability.id().equals("reflect_damage") && originalEvent.getDamager() instanceof org.bukkit.entity.LivingEntity) {
                    // Reflect 50% of damage back to the damager
                    double reflectedDamage = originalEvent.getDamage() * 0.5;
                    ((org.bukkit.entity.LivingEntity) originalEvent.getDamager()).damage(reflectedDamage, originalEvent.getEntity());
                }
            }
            // Update bossbar health when entity takes damage
            if (originalEvent.getEntity() instanceof org.bukkit.entity.LivingEntity) {
                EntityMetadata.updateBossBarHealth((org.bukkit.entity.LivingEntity) originalEvent.getEntity(), metadata);
            }
        }

        // Item XP logic for damaging non-player entities
        if (originalEvent.getDamager().getType() == EntityType.PLAYER && !StaticLists.xpBlacklist.contains(originalEvent.getEntityType()) && ItemLevelHandler.isItemLevelable(((Player) originalEvent.getDamager()).getInventory().getItemInMainHand())) {
            ItemLevelHandler.getLevelHandler((Player) originalEvent.getDamager()).addXp(originalEvent.getDamage());
        }
    }
}
