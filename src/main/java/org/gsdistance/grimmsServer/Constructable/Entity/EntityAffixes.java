package org.gsdistance.grimmsServer.Constructable.Entity;

import org.bukkit.attribute.Attribute;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class EntityAffixes {
    // Key - Affix
    // Value - Tier (higher tier = stronger effect)
    public static final Map<EntityAffix, Integer> affixes = Map.ofEntries(
            // Health bonus - increases max health by tier * 50%
            Map.entry(new EntityAffix("healthy", (entity) -> {
                if (entity.getAttribute(Attribute.MAX_HEALTH) != null) {
                    double base = entity.getAttribute(Attribute.MAX_HEALTH).getBaseValue();
                    entity.getAttribute(Attribute.MAX_HEALTH).setBaseValue(base * 1.5);
                    entity.setHealth(entity.getAttribute(Attribute.MAX_HEALTH).getValue());
                }
                return null;
            }), 1),

            // Armor bonus - increases armor by tier * 5
            Map.entry(new EntityAffix("armored", (entity) -> {
                if (entity.getAttribute(Attribute.ARMOR) != null) {
                    double base = entity.getAttribute(Attribute.ARMOR).getBaseValue();
                    entity.getAttribute(Attribute.ARMOR).setBaseValue(base + 5);
                }
                return null;
            }), 1),

            // Damage bonus - increases attack damage by tier * 20%
            Map.entry(new EntityAffix("strong", (entity) -> {
                if (entity.getAttribute(Attribute.ATTACK_DAMAGE) != null) {
                    double base = entity.getAttribute(Attribute.ATTACK_DAMAGE).getBaseValue();
                    entity.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(base * 1.2);
                }
                return null;
            }), 1),

            // Speed bonus - gives speed effect
            Map.entry(new EntityAffix("swift", (entity) -> {
                entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                return null;
            }), 1),

            // Fire resistance
            Map.entry(new EntityAffix("fiery", (entity) -> {
                entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
                return null;
            }), 1),

            // Knockback resistance
            Map.entry(new EntityAffix("heavy", (entity) -> {
                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, Integer.MAX_VALUE, 0));
                if (entity.getAttribute(Attribute.KNOCKBACK_RESISTANCE) != null) {
                    entity.getAttribute(Attribute.KNOCKBACK_RESISTANCE).setBaseValue(0.5);
                }
                return null;
            }), 1)
    );
}
