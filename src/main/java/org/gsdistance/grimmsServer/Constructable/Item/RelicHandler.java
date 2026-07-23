package org.gsdistance.grimmsServer.Constructable.Item;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.gsdistance.grimmsServer.Shared;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class RelicHandler {
    ItemStack item;
    ItemDataHandler dataHandler;
    private static final Random RANDOM = new Random();

    public RelicHandler(ItemStack item) {
        this.item = item;
        this.dataHandler = ItemDataHandler.getItemDataHandler(item);
    }

    public static boolean isRelic(ItemStack item) {
        Boolean isRelic = getRelicHandler(item).dataHandler.getItemNBTData("isRelic", PersistentDataType.BOOLEAN);
        return isRelic != null && isRelic;
    }

    public static RelicHandler getRelicHandler(ItemStack item) {
        return new RelicHandler(item);
    }

    public static void makeRelic(ItemStack item) {
        // Initialize as a custom item first
        CustomItemHandler customHandler = CustomItemHandler.createHandler(item);
        
        RelicHandler relicHandler = new RelicHandler(item);
        relicHandler.dataHandler.setItemNBTData("isRelic", true, PersistentDataType.BOOLEAN);
        relicHandler.dataHandler.setItemNBTData("relicUUID", UUID.randomUUID().toString(), PersistentDataType.STRING);
        relicHandler.dataHandler.setItemNBTData("relicTimestamp", LocalDateTime.now().toString(), PersistentDataType.STRING);
        String name = item.getType().name().toLowerCase();
        String relicType = "unknown";
        if (name.contains("sword")) {
            relicType = "sword";
        } else if (name.contains("pickaxe")) {
            relicType = "pickaxe";
        } else if (name.contains("axe")) {
            relicType = "axe";
        } else if (name.contains("shovel")) {
            relicType = "shovel";
        } else if (name.contains("hoe")) {
            relicType = "hoe";
        } else if (name.contains("helmet") || name.contains("chestplate") || name.contains("leggings") || name.contains("boots")) {
            relicType = "armor";
        }

        relicHandler.dataHandler.setItemNBTData("relicType", relicType, PersistentDataType.STRING);
        
        // Set custom item ID to relic type for identification
        customHandler.setCustomItemId("relic_" + relicType);
        
        relicHandler.initRandomRelicStats();
    }

    public void initRandomRelicStats() {
        int tier = RANDOM.nextInt(1, 4);
        int grade = RANDOM.nextInt(1, 101);
        int durabilityResistance = RANDOM.nextInt(1, 81);
        this.setRelicStats(tier, grade, durabilityResistance);
        this.recalculateBaseAttributes();
        ItemStats.getItemStats(this.item).UpdateItemStats();
    }

    public void recalculateBaseAttributes() {
        int tier = this.getRelicTier();
        int grade = this.getRelicGrade();
        Material material = this.item.getType();
        double baseDamage = switch (material) {
            case DIAMOND_SWORD, STONE_AXE -> 7.0F;
            case IRON_SWORD, WOODEN_AXE, GOLDEN_AXE -> 6.0F;
            case STONE_SWORD -> 5.0F;
            case WOODEN_SWORD, GOLDEN_SWORD -> 4.0F;
            case NETHERITE_SWORD, IRON_AXE -> 8.0F;
            case DIAMOND_AXE -> 9.0F;
            case NETHERITE_AXE -> 10.0F;
            default -> 1.0F;
        };
        double baseArmor = switch (material) {
            case DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS -> 3.0F;
            case IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS -> 2.0F;
            case CHAINMAIL_HELMET, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_BOOTS -> 1.5F;
            case GOLDEN_HELMET, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS, GOLDEN_BOOTS -> 1.0F;
            case NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS -> 4.0F;
            default -> 0.0F;
        };
        double baseToughness = switch (material) {
            case DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS -> 2.0F;
            case IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS -> 1.0F;
            case CHAINMAIL_HELMET, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_BOOTS -> 0.5F;
            case NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS -> 3.0F;
            default -> 0.0F;
        };
        switch (this.getRelicType()) {
            case "sword":
                this.dataHandler.setAttribute(Attribute.ATTACK_DAMAGE, new AttributeModifier(Shared.getNamespacedKey("relic_attack_damage"), baseDamage + Math.pow(Math.max(2.0F, (double) grade / (double) 25.0F), tier), Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
                if (tier == 1) {
                    this.dataHandler.setAttribute(Attribute.ATTACK_SPEED, new AttributeModifier(Shared.getNamespacedKey("relic_attack_speed"), -2.4, Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
                } else {
                    this.dataHandler.setAttribute(Attribute.ATTACK_SPEED, new AttributeModifier(Shared.getNamespacedKey("relic_attack_speed"), -(2.4 - Math.max(Math.max((Math.sqrt(grade) * (double) tier / (double) 10.0F), 2), 1)), Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
                }
                break;
            case "armor":
                this.dataHandler.setAttribute(Attribute.ARMOR, new AttributeModifier(Shared.getNamespacedKey("relic_armor"), baseArmor + Math.pow(Math.max(2.0F, (double) grade / (double) 25.0F), tier), Operation.ADD_NUMBER, EquipmentSlotGroup.ARMOR));
                if (tier > 1) {
                    this.dataHandler.setAttribute(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(Shared.getNamespacedKey("relic_armor_toughness"), baseToughness + Math.max(1.0F, Math.sqrt(grade) * (double) tier / (double) 10.0F), Operation.ADD_NUMBER, EquipmentSlotGroup.ARMOR));
                }
        }

    }

    public void setRelicStats(Integer tier, Integer grade, Integer durabilityResistance) {
        this.dataHandler.setItemNBTData("relicTier", tier, PersistentDataType.INTEGER);
        this.dataHandler.setItemNBTData("relicGrade", grade, PersistentDataType.INTEGER);
        this.dataHandler.setItemNBTData("relicDurabilityResistance", durabilityResistance, PersistentDataType.INTEGER);
    }

    public Integer getRelicTier() {
        return Objects.requireNonNullElse(this.dataHandler.getItemNBTData("relicTier", PersistentDataType.INTEGER), 1);
    }

    public Integer getRelicGrade() {
        return Objects.requireNonNullElse(this.dataHandler.getItemNBTData("relicGrade", PersistentDataType.INTEGER), 1);
    }

    public Integer getRelicDurabilityResistance() {
        return Objects.requireNonNullElse(this.dataHandler.getItemNBTData("relicDurabilityResistance", PersistentDataType.INTEGER), 0);
    }

    public String getRelicType() {
        return Objects.requireNonNullElse(this.dataHandler.getItemNBTData("relicType", PersistentDataType.STRING), "unknown");
    }
}
