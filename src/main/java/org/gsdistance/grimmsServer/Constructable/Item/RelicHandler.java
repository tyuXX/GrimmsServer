package org.gsdistance.grimmsServer.Constructable.Item;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
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

    public RelicHandler(ItemStack item) {
        this.item = item;
        this.dataHandler = ItemDataHandler.getItemDataHandler(item);
    }

    public static boolean isRelic(ItemStack item) {
        Boolean isRelic = getRelicHandler(item).dataHandler.getItemNBTData("isRelic", PersistentDataType.BOOLEAN);
        return isRelic != null && isRelic; // Check for null before accessing the value
    }

    public static RelicHandler getRelicHandler(ItemStack item) {
        return new RelicHandler(item);
    }

    public static void makeRelic(ItemStack item) {
        RelicHandler relicHandler = new RelicHandler(item);
        relicHandler.dataHandler.setItemNBTData("isRelic", true, PersistentDataType.BOOLEAN);
        relicHandler.dataHandler.setItemNBTData("relicUUID", UUID.randomUUID().toString(), PersistentDataType.STRING);
        relicHandler.dataHandler.setItemNBTData("relicTimestamp", LocalDateTime.now().toString(), PersistentDataType.STRING);

        String name = item.getType().name().toLowerCase(); // Use Material.name() instead
        String relicType = "unknown"; // Default type

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
        relicHandler.initRandomRelicStats();
    }

    public void initRandomRelicStats() {
        int tier = new Random().nextInt(1,  4); // Random tier between 1 and 3
        int grade = new Random().nextInt(1, 101); // Random grade between 1 and 100
        int durabilityResistance = new Random().nextInt(1, 81); // Random grade between 1 and 100
        setRelicStats(tier, grade, durabilityResistance);
        recalculateBaseAttributes();
        ItemStats.getItemStats(item).UpdateItemStats();
    }

    @SuppressWarnings("UnstableApiUsage")
    public void recalculateBaseAttributes() {
        int tier = getRelicTier();
        int grade = getRelicGrade();

        Material material = item.getType();
        double baseDamage = switch (material) {
            // Sword types
            case DIAMOND_SWORD -> 7.0;
            case IRON_SWORD -> 6.0;
            case STONE_SWORD -> 5.0;
            case WOODEN_SWORD, GOLDEN_SWORD -> 4.0;
            case NETHERITE_SWORD -> 8.0;
            // Axe types
            case DIAMOND_AXE -> 9.0;
            case IRON_AXE -> 8.0;
            case STONE_AXE -> 7.0;
            case WOODEN_AXE, GOLDEN_AXE -> 6.0;
            case NETHERITE_AXE -> 10.0;
            default -> 1.0; // Default base damage for unsupported items
        };
        double baseArmor = switch (material) {
            // Armor types
            case DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS -> 3.0;
            case IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS -> 2.0;
            case CHAINMAIL_HELMET, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_BOOTS -> 1.5;
            case GOLDEN_HELMET, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS, GOLDEN_BOOTS -> 1.0;
            case NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS -> 4.0;
            default -> 0.0; // Default base armor for unsupported items
        };
        double baseToughness = switch (material) {
            // Armor toughness types
            case DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS -> 2.0;
            case IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS -> 1.0;
            case CHAINMAIL_HELMET, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_BOOTS -> 0.5;
            case GOLDEN_HELMET, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS, GOLDEN_BOOTS -> 0.0;
            case NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS -> 3.0;
            default -> 0.0; // Default base toughness for unsupported items
        };

        switch (getRelicType()){
            case "sword" -> {
                dataHandler.setAttribute(Attribute.ATTACK_DAMAGE, new AttributeModifier(Shared.getNamespacedKey("reliic_attack_damage"), baseDamage + Math.pow(Math.max(2,(double) grade / 25) ,tier), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
                if(tier > 1){
                    dataHandler.setAttribute(Attribute.ATTACK_SPEED, new AttributeModifier(Shared.getNamespacedKey("reliic_attack_speed"), Math.max(1.7,Math.sqrt(grade) * tier / 10), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
                }
            }
            case "armor" -> {
                dataHandler.setAttribute(Attribute.ARMOR, new AttributeModifier(Shared.getNamespacedKey("relic_armor"), baseArmor + Math.pow(Math.max(2,(double) grade / 25) ,tier), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ARMOR));
                if(tier > 1){
                    dataHandler.setAttribute(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(Shared.getNamespacedKey("relic_armor_toughness"), baseToughness + Math.max(1,Math.sqrt(grade) * tier / 10), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ARMOR));
                }
            }
            default -> {}
        }
    }

    public void setRelicStats(Integer tier, Integer grade, Integer durabilityResistance) {
        dataHandler.setItemNBTData("relicTier", tier, PersistentDataType.INTEGER);
        dataHandler.setItemNBTData("relicGrade", grade, PersistentDataType.INTEGER);
        dataHandler.setItemNBTData("relicDurabilityResistance", durabilityResistance, PersistentDataType.INTEGER);
    }

    public Integer getRelicTier() {
        return Objects.requireNonNullElse(dataHandler.getItemNBTData("relicTier", PersistentDataType.INTEGER), 1);
    }

    public Integer getRelicGrade() {
        return Objects.requireNonNullElse(dataHandler.getItemNBTData("relicGrade", PersistentDataType.INTEGER), 1);
    }

    public Integer getRelicDurabilityResistance() {
        return Objects.requireNonNullElse(dataHandler.getItemNBTData("relicDurabilityResistance", PersistentDataType.INTEGER), 0);
    }

    public String getRelicType() {
        return Objects.requireNonNullElse(dataHandler.getItemNBTData("relicType", PersistentDataType.STRING), "unknown");
    }
}
