package org.gsdistance.grimmsServer.Data.Market;

import java.util.Map;
import org.bukkit.enchantments.Enchantment;

public class EnchantBaseValues {
   public static final Map<Enchantment, Double> enchantBaseValues;

   public EnchantBaseValues() {
   }

   static {
      enchantBaseValues = Map.ofEntries(Map.entry(Enchantment.SHARPNESS, (double)3000.0F), Map.entry(Enchantment.EFFICIENCY, (double)3500.0F), Map.entry(Enchantment.KNOCKBACK, (double)2000.0F), Map.entry(Enchantment.BANE_OF_ARTHROPODS, (double)2500.0F), Map.entry(Enchantment.SMITE, (double)2500.0F), Map.entry(Enchantment.BINDING_CURSE, (double)10000.0F), Map.entry(Enchantment.VANISHING_CURSE, (double)10000.0F), Map.entry(Enchantment.PROTECTION, (double)3500.0F), Map.entry(Enchantment.BLAST_PROTECTION, (double)2500.0F), Map.entry(Enchantment.PROJECTILE_PROTECTION, (double)2500.0F), Map.entry(Enchantment.FIRE_PROTECTION, (double)2500.0F), Map.entry(Enchantment.SWIFT_SNEAK, (double)2000.0F), Map.entry(Enchantment.BREACH, (double)5000.0F), Map.entry(Enchantment.DENSITY, (double)5000.0F), Map.entry(Enchantment.MENDING, (double)12500.0F), Map.entry(Enchantment.FEATHER_FALLING, (double)2500.0F), Map.entry(Enchantment.RESPIRATION, (double)4750.0F), Map.entry(Enchantment.AQUA_AFFINITY, (double)3000.0F), Map.entry(Enchantment.THORNS, (double)5000.0F), Map.entry(Enchantment.DEPTH_STRIDER, (double)4000.0F), Map.entry(Enchantment.FROST_WALKER, (double)3500.0F), Map.entry(Enchantment.FIRE_ASPECT, (double)4000.0F), Map.entry(Enchantment.LOOTING, (double)4000.0F), Map.entry(Enchantment.SWEEPING_EDGE, (double)4500.0F), Map.entry(Enchantment.SILK_TOUCH, (double)5000.0F), Map.entry(Enchantment.UNBREAKING, (double)4000.0F), Map.entry(Enchantment.FORTUNE, (double)5500.0F), Map.entry(Enchantment.POWER, (double)4500.0F), Map.entry(Enchantment.PUNCH, (double)2000.0F), Map.entry(Enchantment.FLAME, (double)4000.0F), Map.entry(Enchantment.INFINITY, (double)9000.0F), Map.entry(Enchantment.LUCK_OF_THE_SEA, (double)3500.0F), Map.entry(Enchantment.LURE, (double)4500.0F), Map.entry(Enchantment.LOYALTY, (double)6000.0F), Map.entry(Enchantment.IMPALING, (double)2500.0F), Map.entry(Enchantment.RIPTIDE, (double)6000.0F), Map.entry(Enchantment.CHANNELING, (double)6000.0F), Map.entry(Enchantment.MULTISHOT, (double)4500.0F), Map.entry(Enchantment.QUICK_CHARGE, (double)4000.0F), Map.entry(Enchantment.PIERCING, (double)5000.0F), Map.entry(Enchantment.WIND_BURST, (double)5000.0F), Map.entry(Enchantment.SOUL_SPEED, (double)3000.0F));
   }
}
