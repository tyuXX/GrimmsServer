package org.gsdistance.grimmsServer.Constructable.Item;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.gsdistance.grimmsServer.Shared;

public class RelicHandler {
   ItemStack item;
   ItemDataHandler dataHandler;

   public RelicHandler(ItemStack item) {
      this.item = item;
      this.dataHandler = ItemDataHandler.getItemDataHandler(item);
   }

   public static boolean isRelic(ItemStack item) {
      Boolean isRelic = (Boolean)getRelicHandler(item).dataHandler.getItemNBTData("isRelic", PersistentDataType.BOOLEAN);
      return isRelic != null && isRelic;
   }

   public static RelicHandler getRelicHandler(ItemStack item) {
      return new RelicHandler(item);
   }

   public static void makeRelic(ItemStack item) {
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
      relicHandler.initRandomRelicStats();
   }

   public void initRandomRelicStats() {
      int tier = (new Random()).nextInt(1, 4);
      int grade = (new Random()).nextInt(1, 101);
      int durabilityResistance = (new Random()).nextInt(1, 81);
      this.setRelicStats(tier, grade, durabilityResistance);
      this.recalculateBaseAttributes();
      ItemStats.getItemStats(this.item).UpdateItemStats();
   }

   public void recalculateBaseAttributes() {
      int tier = this.getRelicTier();
      int grade = this.getRelicGrade();
      Material material = this.item.getType();
      double baseDamage;
      switch (material) {
         case DIAMOND_SWORD:
            baseDamage = (double)7.0F;
            break;
         case IRON_SWORD:
            baseDamage = (double)6.0F;
            break;
         case STONE_SWORD:
            baseDamage = (double)5.0F;
            break;
         case WOODEN_SWORD:
         case GOLDEN_SWORD:
            baseDamage = (double)4.0F;
            break;
         case NETHERITE_SWORD:
            baseDamage = (double)8.0F;
            break;
         case DIAMOND_AXE:
            baseDamage = (double)9.0F;
            break;
         case IRON_AXE:
            baseDamage = (double)8.0F;
            break;
         case STONE_AXE:
            baseDamage = (double)7.0F;
            break;
         case WOODEN_AXE:
         case GOLDEN_AXE:
            baseDamage = (double)6.0F;
            break;
         case NETHERITE_AXE:
            baseDamage = (double)10.0F;
            break;
         default:
            baseDamage = (double)1.0F;
      }
      double baseArmor;
      switch (material) {
         case DIAMOND_HELMET:
         case DIAMOND_CHESTPLATE:
         case DIAMOND_LEGGINGS:
         case DIAMOND_BOOTS:
            baseArmor = (double)3.0F;
            break;
         case IRON_HELMET:
         case IRON_CHESTPLATE:
         case IRON_LEGGINGS:
         case IRON_BOOTS:
            baseArmor = (double)2.0F;
            break;
         case CHAINMAIL_HELMET:
         case CHAINMAIL_CHESTPLATE:
         case CHAINMAIL_LEGGINGS:
         case CHAINMAIL_BOOTS:
            baseArmor = (double)1.5F;
            break;
         case GOLDEN_HELMET:
         case GOLDEN_CHESTPLATE:
         case GOLDEN_LEGGINGS:
         case GOLDEN_BOOTS:
            baseArmor = (double)1.0F;
            break;
         case NETHERITE_HELMET:
         case NETHERITE_CHESTPLATE:
         case NETHERITE_LEGGINGS:
         case NETHERITE_BOOTS:
            baseArmor = (double)4.0F;
            break;
         default:
            baseArmor = (double)0.0F;
      }
      double baseToughness;
      switch (material) {
         case DIAMOND_HELMET:
         case DIAMOND_CHESTPLATE:
         case DIAMOND_LEGGINGS:
         case DIAMOND_BOOTS:
            baseToughness = (double)2.0F;
            break;
         case IRON_HELMET:
         case IRON_CHESTPLATE:
         case IRON_LEGGINGS:
         case IRON_BOOTS:
            baseToughness = (double)1.0F;
            break;
         case CHAINMAIL_HELMET:
         case CHAINMAIL_CHESTPLATE:
         case CHAINMAIL_LEGGINGS:
         case CHAINMAIL_BOOTS:
            baseToughness = (double)0.5F;
            break;
         case GOLDEN_HELMET:
         case GOLDEN_CHESTPLATE:
         case GOLDEN_LEGGINGS:
         case GOLDEN_BOOTS:
            baseToughness = (double)0.0F;
            break;
         case NETHERITE_HELMET:
         case NETHERITE_CHESTPLATE:
         case NETHERITE_LEGGINGS:
         case NETHERITE_BOOTS:
            baseToughness = (double)3.0F;
            break;
         default:
            baseToughness = (double)0.0F;
      }
      switch (this.getRelicType()) {
         case "sword":
            this.dataHandler.setAttribute(Attribute.ATTACK_DAMAGE, new AttributeModifier(Shared.getNamespacedKey("reliic_attack_damage"), baseDamage + Math.pow(Math.max((double)2.0F, (double)grade / (double)25.0F), (double)tier), Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
            if (tier > 1) {
               this.dataHandler.setAttribute(Attribute.ATTACK_SPEED, new AttributeModifier(Shared.getNamespacedKey("reliic_attack_speed"), Math.max(1.7, Math.sqrt((double)grade) * (double)tier / (double)10.0F), Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
            }
            break;
         case "armor":
            this.dataHandler.setAttribute(Attribute.ARMOR, new AttributeModifier(Shared.getNamespacedKey("relic_armor"), baseArmor + Math.pow(Math.max((double)2.0F, (double)grade / (double)25.0F), (double)tier), Operation.ADD_NUMBER, EquipmentSlotGroup.ARMOR));
            if (tier > 1) {
               this.dataHandler.setAttribute(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(Shared.getNamespacedKey("relic_armor_toughness"), baseToughness + Math.max((double)1.0F, Math.sqrt((double)grade) * (double)tier / (double)10.0F), Operation.ADD_NUMBER, EquipmentSlotGroup.ARMOR));
            }
      }

   }

   public void setRelicStats(Integer tier, Integer grade, Integer durabilityResistance) {
      this.dataHandler.setItemNBTData("relicTier", tier, PersistentDataType.INTEGER);
      this.dataHandler.setItemNBTData("relicGrade", grade, PersistentDataType.INTEGER);
      this.dataHandler.setItemNBTData("relicDurabilityResistance", durabilityResistance, PersistentDataType.INTEGER);
   }

   public Integer getRelicTier() {
      return (Integer)Objects.requireNonNullElse((Integer)this.dataHandler.getItemNBTData("relicTier", PersistentDataType.INTEGER), 1);
   }

   public Integer getRelicGrade() {
      return (Integer)Objects.requireNonNullElse((Integer)this.dataHandler.getItemNBTData("relicGrade", PersistentDataType.INTEGER), 1);
   }

   public Integer getRelicDurabilityResistance() {
      return (Integer)Objects.requireNonNullElse((Integer)this.dataHandler.getItemNBTData("relicDurabilityResistance", PersistentDataType.INTEGER), 0);
   }

   public String getRelicType() {
      return (String)Objects.requireNonNullElse((String)this.dataHandler.getItemNBTData("relicType", PersistentDataType.STRING), "unknown");
   }
}
