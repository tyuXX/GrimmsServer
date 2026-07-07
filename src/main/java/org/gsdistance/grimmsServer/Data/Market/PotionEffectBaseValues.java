package org.gsdistance.grimmsServer.Data.Market;

import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class PotionEffectBaseValues {
    public static final Map<PotionEffectType, Double> potionEffectBaseValues;
    public static final Map<PotionEffectType, Integer> potionEffectAmplitudeLimits;

    public PotionEffectBaseValues() {
    }

    public static double getPotionEffectBaseValue(PotionEffectType potionEffectType, int time, int amplifier) {
        Integer limit = potionEffectAmplitudeLimits.get(potionEffectType);
        return limit != null && amplifier > limit ? Double.MAX_VALUE : potionEffectBaseValues.get(potionEffectType) * (double) time * Math.pow(amplifier, 2.0F);
    }

    static {
        potionEffectBaseValues = Map.ofEntries(Map.entry(PotionEffectType.SPEED, (double) 5.0F), Map.entry(PotionEffectType.SLOWNESS, (double) 3.0F), Map.entry(PotionEffectType.HASTE, (double) 8.0F), Map.entry(PotionEffectType.MINING_FATIGUE, (double) 2.0F), Map.entry(PotionEffectType.STRENGTH, (double) 10.0F), Map.entry(PotionEffectType.INSTANT_HEALTH, (double) 50.0F), Map.entry(PotionEffectType.INSTANT_DAMAGE, (double) 45.0F), Map.entry(PotionEffectType.JUMP_BOOST, (double) 4.0F), Map.entry(PotionEffectType.NAUSEA, (double) 1.0F), Map.entry(PotionEffectType.REGENERATION, (double) 12.0F), Map.entry(PotionEffectType.RESISTANCE, (double) 15.0F), Map.entry(PotionEffectType.FIRE_RESISTANCE, (double) 6.0F), Map.entry(PotionEffectType.WATER_BREATHING, (double) 4.0F), Map.entry(PotionEffectType.INVISIBILITY, (double) 20.0F), Map.entry(PotionEffectType.BLINDNESS, (double) 2.0F), Map.entry(PotionEffectType.NIGHT_VISION, (double) 3.0F), Map.entry(PotionEffectType.HUNGER, (double) 1.0F), Map.entry(PotionEffectType.WEAKNESS, (double) 2.0F), Map.entry(PotionEffectType.POISON, (double) 8.0F), Map.entry(PotionEffectType.WITHER, (double) 18.0F), Map.entry(PotionEffectType.HEALTH_BOOST, (double) 150.0F), Map.entry(PotionEffectType.ABSORPTION, (double) 20.0F), Map.entry(PotionEffectType.GLOWING, (double) 1.0F), Map.entry(PotionEffectType.LEVITATION, (double) 12.0F), Map.entry(PotionEffectType.LUCK, (double) 30.0F), Map.entry(PotionEffectType.UNLUCK, (double) 2.0F), Map.entry(PotionEffectType.SLOW_FALLING, (double) 5.0F), Map.entry(PotionEffectType.CONDUIT_POWER, (double) 14.0F), Map.entry(PotionEffectType.DOLPHINS_GRACE, (double) 3.0F), Map.entry(PotionEffectType.BAD_OMEN, (double) 8.0F), Map.entry(PotionEffectType.HERO_OF_THE_VILLAGE, (double) 25.0F), Map.entry(PotionEffectType.DARKNESS, (double) 2.0F), Map.entry(PotionEffectType.TRIAL_OMEN, (double) 10.0F), Map.entry(PotionEffectType.WIND_CHARGED, (double) 7.0F), Map.entry(PotionEffectType.WEAVING, (double) 6.0F), Map.entry(PotionEffectType.OOZING, (double) 5.0F), Map.entry(PotionEffectType.INFESTED, (double) 4.0F));
        potionEffectAmplitudeLimits = Map.ofEntries(Map.entry(PotionEffectType.REGENERATION, 2), Map.entry(PotionEffectType.RESISTANCE, 1), Map.entry(PotionEffectType.HASTE, 3));
    }
}
