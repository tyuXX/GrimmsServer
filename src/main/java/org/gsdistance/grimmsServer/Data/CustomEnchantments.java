package org.gsdistance.grimmsServer.Data;

public enum CustomEnchantments {
    TRUE_DAMAGE("True Damage", 3),
    SOULBOUND("Soulbound", 1);

    public final String enchantmentName;
    public final int maxLevel;


    CustomEnchantments(String enchantmentName, int maxLevel) {
        this.enchantmentName = enchantmentName;
        this.maxLevel = maxLevel;
    }
}
