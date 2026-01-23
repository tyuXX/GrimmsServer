package org.gsdistance.grimmsServer.Data.Market;

import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class PotionEffectBaseValues {
    public static final Map<PotionEffectType, Double> potionEffectBaseValues = Map.ofEntries(
            Map.entry(PotionEffectType.SPEED, 5.0),
            Map.entry(PotionEffectType.SLOWNESS, 3.0),
            Map.entry(PotionEffectType.HASTE, 8.0),
            Map.entry(PotionEffectType.MINING_FATIGUE, 2.0),
            Map.entry(PotionEffectType.STRENGTH, 10.0),
            Map.entry(PotionEffectType.INSTANT_HEALTH, 50.0),
            Map.entry(PotionEffectType.INSTANT_DAMAGE, 45.0),
            Map.entry(PotionEffectType.JUMP_BOOST, 4.0),
            Map.entry(PotionEffectType.NAUSEA, 1.0),
            Map.entry(PotionEffectType.REGENERATION, 12.0),
            Map.entry(PotionEffectType.RESISTANCE, 15.0),
            Map.entry(PotionEffectType.FIRE_RESISTANCE, 6.0),
            Map.entry(PotionEffectType.WATER_BREATHING, 4.0),
            Map.entry(PotionEffectType.INVISIBILITY, 20.0),
            Map.entry(PotionEffectType.BLINDNESS, 2.0),
            Map.entry(PotionEffectType.NIGHT_VISION, 3.0),
            Map.entry(PotionEffectType.HUNGER, 1.0),
            Map.entry(PotionEffectType.WEAKNESS, 2.0),
            Map.entry(PotionEffectType.POISON, 8.0),
            Map.entry(PotionEffectType.WITHER, 18.0),
            Map.entry(PotionEffectType.HEALTH_BOOST, 150.0),
            Map.entry(PotionEffectType.ABSORPTION, 20.0),
            Map.entry(PotionEffectType.GLOWING, 1.0),
            Map.entry(PotionEffectType.LEVITATION, 12.0),
            Map.entry(PotionEffectType.LUCK, 30.0),
            Map.entry(PotionEffectType.UNLUCK, 2.0),
            Map.entry(PotionEffectType.SLOW_FALLING, 5.0),
            Map.entry(PotionEffectType.CONDUIT_POWER, 14.0),
            Map.entry(PotionEffectType.DOLPHINS_GRACE, 3.0),
            Map.entry(PotionEffectType.BAD_OMEN, 8.0),
            Map.entry(PotionEffectType.HERO_OF_THE_VILLAGE, 25.0),
            Map.entry(PotionEffectType.DARKNESS, 2.0),
            Map.entry(PotionEffectType.TRIAL_OMEN, 10.0),
            Map.entry(PotionEffectType.WIND_CHARGED, 7.0),
            Map.entry(PotionEffectType.WEAVING, 6.0),
            Map.entry(PotionEffectType.OOZING, 5.0),
            Map.entry(PotionEffectType.INFESTED, 4.0)
    );

    public static final Map<PotionEffectType, Integer> potionEffectAmplitudeLimits = Map.ofEntries(
            Map.entry(PotionEffectType.REGENERATION, 2),
            Map.entry(PotionEffectType.RESISTANCE, 1),
            Map.entry(PotionEffectType.HASTE, 3)
    );

    public static double getPotionEffectBaseValue(PotionEffectType potionEffectType, int time, int amplifier) {
        Integer limit = potionEffectAmplitudeLimits.get(potionEffectType);
        if(limit != null && amplifier > limit) {
            return Double.MAX_VALUE;
        }
        return potionEffectBaseValues.get(potionEffectType) * time * Math.pow(amplifier,2);
    }
}
