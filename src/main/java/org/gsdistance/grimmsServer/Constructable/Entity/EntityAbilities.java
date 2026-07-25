package org.gsdistance.grimmsServer.Constructable.Entity;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class EntityAbilities {
    // Key - Ability
    // Value - Tier (higher tier = stronger effect)
    public static final Map<EntityAbility, Integer> abilities = Map.ofEntries(
            // Heal self - heals 20% of max health every 10 seconds
            Map.entry(new EntityAbility("heal_self", 200, (entity) -> {
                double maxHealth = entity.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue();
                double healAmount = maxHealth * 0.2;
                entity.setHealth(Math.min(maxHealth, entity.getHealth() + healAmount));
                return null;
            }), 1),

            // Reflect damage - reflects 50% of damage taken (handled in damage event)
            Map.entry(new EntityAbility("reflect_damage", 0, (entity) -> {
                // This is a passive ability handled in damage events
                return null;
            }), 1),

            // Summon reinforcements - summons 2 mobs of same type every 30 seconds
            Map.entry(new EntityAbility("summon_reinforcements", 600, (entity) -> {
                Location loc = entity.getLocation();
                for (int i = 0; i < 2; i++) {
                    LivingEntity reinforcement = (LivingEntity) loc.getWorld().spawnEntity(loc, entity.getType());
                    reinforcement.setCustomName("Reinforcement");
                }
                return null;
            }), 1),

            // Teleport - teleports to random nearby location every 15 seconds
            Map.entry(new EntityAbility("teleport", 900, (entity) -> {
                Location loc = entity.getLocation();
                Location newLoc = new Location(
                        loc.getWorld(),
                        loc.getX() + (Math.random() - 0.5) * 10,
                        loc.getY(),
                        loc.getZ() + (Math.random() - 0.5) * 10
                );
                entity.teleport(newLoc);
                return null;
            }), 1),

            // Rage - increases attack speed and damage when low health
            Map.entry(new EntityAbility("rage", 100, (entity) -> {
                if (entity.getHealth() < entity.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue() * 0.3) {
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 100, 1));
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2));
                }
                return null;
            }), 1),

            // Fire burst - ignites nearby enemies every 20 seconds
            Map.entry(new EntityAbility("fire_burst", 400, (entity) -> {
                entity.getNearbyEntities(5, 5, 5).forEach(nearby -> {
                    if (nearby instanceof LivingEntity && nearby != entity) {
                        nearby.setFireTicks(60);
                    }
                });
                return null;
            }), 1)
    );
}
