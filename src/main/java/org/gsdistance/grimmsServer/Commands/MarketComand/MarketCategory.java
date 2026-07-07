package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public enum MarketCategory {
    ALL("All Items", Material.CHEST),
    ORES("Ores & Minerals", Material.DIAMOND_ORE),
    WOOD("Wood & Logs", Material.OAK_LOG),
    STONE("Stone & Building", Material.STONE),
    FOOD("Food & Farming", Material.BREAD),
    REDSTONE("Redstone", Material.REDSTONE),
    NETHER("Nether Materials", Material.NETHERRACK),
    END("End Materials", Material.END_STONE),
    MAGIC("Magic & Enchanting", Material.ENCHANTING_TABLE),
    MISC("Miscellaneous", Material.BARRIER);
    
    private final String name;
    private final Material displayMaterial;
    
    MarketCategory(String name, Material displayMaterial) {
        this.name = name;
        this.displayMaterial = displayMaterial;
    }
    
    public String getName() {
        return name;
    }
    
    public ItemStack getDisplayItem() {
        return new ItemStack(displayMaterial);
    }
    
    public List<Material> getItems() {
        List<Material> items = new ArrayList<>();
        
        switch (this) {
            case ALL:
                for (Material material : Material.values()) {
                    if (material.isItem()) {
                        items.add(material);
                    }
                }
                break;
            case ORES:
                addMaterials(items, Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE, Material.IRON_ORE, 
                    Material.DEEPSLATE_IRON_ORE, Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE,
                    Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE, Material.COPPER_ORE, 
                    Material.DEEPSLATE_COPPER_ORE, Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE,
                    Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE, Material.LAPIS_ORE,
                    Material.DEEPSLATE_LAPIS_ORE, Material.NETHER_GOLD_ORE, Material.NETHER_QUARTZ_ORE,
                    Material.ANCIENT_DEBRIS, Material.DIAMOND, Material.IRON_INGOT, Material.GOLD_INGOT,
                    Material.COPPER_INGOT, Material.COAL, Material.REDSTONE, Material.LAPIS_LAZULI,
                    Material.EMERALD, Material.NETHERITE_INGOT, Material.RAW_COPPER, Material.RAW_GOLD,
                    Material.RAW_IRON);
                break;
            case WOOD:
                addMaterials(items, Material.OAK_LOG, Material.SPRUCE_LOG, Material.BIRCH_LOG, 
                    Material.JUNGLE_LOG, Material.ACACIA_LOG, Material.CHERRY_LOG, Material.DARK_OAK_LOG,
                    Material.MANGROVE_LOG, Material.CRIMSON_STEM, Material.WARPED_STEM, Material.BAMBOO_BLOCK,
                    Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.BIRCH_PLANKS, Material.JUNGLE_PLANKS,
                    Material.ACACIA_PLANKS, Material.CHERRY_PLANKS, Material.DARK_OAK_PLANKS, 
                    Material.MANGROVE_PLANKS, Material.BAMBOO_PLANKS, Material.CRIMSON_PLANKS, 
                    Material.WARPED_PLANKS, Material.OAK_SAPLING, Material.SPRUCE_SAPLING, Material.BIRCH_SAPLING,
                    Material.JUNGLE_SAPLING, Material.ACACIA_SAPLING, Material.CHERRY_SAPLING, 
                    Material.DARK_OAK_SAPLING, Material.MANGROVE_PROPAGULE);
                break;
            case STONE:
                addMaterials(items, Material.STONE, Material.COBBLESTONE, Material.GRANITE, 
                    Material.DIORITE, Material.ANDESITE, Material.DEEPSLATE, Material.COBBLED_DEEPSLATE,
                    Material.SANDSTONE, Material.RED_SANDSTONE, Material.BRICKS, Material.PRISMARINE,
                    Material.TERRACOTTA, Material.SAND, Material.RED_SAND, Material.GRAVEL, 
                    Material.DIRT, Material.GRASS_BLOCK, Material.COARSE_DIRT, Material.PODZOL,
                    Material.MUD, Material.CLAY);
                break;
            case FOOD:
                addMaterials(items, Material.WHEAT, Material.BREAD, Material.CARROT, Material.POTATO,
                    Material.BEETROOT, Material.APPLE, Material.GOLDEN_APPLE, Material.COOKED_BEEF,
                    Material.COOKED_PORKCHOP, Material.COOKED_CHICKEN, Material.COOKED_MUTTON,
                    Material.COOKED_RABBIT, Material.COOKED_COD, Material.COOKED_SALMON,
                    Material.BEEF, Material.PORKCHOP, Material.CHICKEN, Material.MUTTON, Material.RABBIT,
                    Material.COD, Material.SALMON, Material.TROPICAL_FISH, Material.PUFFERFISH,
                    Material.EGG, Material.SUGAR, Material.HONEY_BOTTLE, Material.HONEYCOMB,
                    Material.MELON_SLICE, Material.PUMPKIN, Material.SWEET_BERRIES, Material.GLOW_BERRIES,
                    Material.COCOA_BEANS, Material.SUSPICIOUS_STEW, Material.MUSHROOM_STEW,
                    Material.RABBIT_STEW, Material.BEETROOT_SOUP);
                break;
            case REDSTONE:
                addMaterials(items, Material.REDSTONE, Material.REDSTONE_TORCH, Material.REDSTONE_BLOCK,
                    Material.REPEATER, Material.COMPARATOR, Material.PISTON, Material.STICKY_PISTON,
                    Material.OBSERVER, Material.HOPPER, Material.DROPPER, Material.DISPENSER,
                    Material.LEVER, Material.TRIPWIRE_HOOK, Material.DAYLIGHT_DETECTOR,
                    Material.TARGET, Material.LIGHTNING_ROD, Material.COPPER_BLOCK, Material.LIGHTNING_ROD);
                break;
            case NETHER:
                addMaterials(items, Material.NETHERRACK, Material.NETHER_BRICKS, Material.NETHER_QUARTZ_ORE,
                    Material.SOUL_SAND, Material.SOUL_SOIL, Material.BASALT, Material.BLACKSTONE,
                    Material.GLOWSTONE, Material.MAGMA_BLOCK, Material.NETHER_GOLD_ORE,
                    Material.ANCIENT_DEBRIS, Material.CRIMSON_NYLIUM, Material.WARPED_NYLIUM,
                    Material.CRIMSON_STEM, Material.WARPED_STEM, Material.CRIMSON_FUNGUS, Material.WARPED_FUNGUS,
                    Material.CRIMSON_ROOTS, Material.WARPED_ROOTS, Material.SHROOMLIGHT);
                break;
            case END:
                addMaterials(items, Material.END_STONE, Material.OBSIDIAN, Material.CRYING_OBSIDIAN,
                    Material.ENDER_CHEST, Material.ENDER_PEARL, Material.ENDER_EYE, Material.CHORUS_PLANT,
                    Material.CHORUS_FLOWER, Material.PURPUR_BLOCK, Material.PURPUR_PILLAR, Material.ELDER_GUARDIAN_SPAWN_EGG);
                break;
            case MAGIC:
                addMaterials(items, Material.ENCHANTING_TABLE, Material.BOOKSHELF, Material.BOOK,
                    Material.ENCHANTED_BOOK, Material.BREWING_STAND, Material.CAULDRON, Material.GLASS_BOTTLE,
                    Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION,
                    Material.EXPERIENCE_BOTTLE, Material.END_ROD, Material.TOTEM_OF_UNDYING,
                    Material.HEART_OF_THE_SEA, Material.NAUTILUS_SHELL, Material.CONDUIT,
                    Material.BEACON, Material.SPAWNER);
                break;
            case MISC:
                // Add remaining items that don't fit other categories
                for (Material material : Material.values()) {
                    if (material.isItem() && !items.contains(material)) {
                        items.add(material);
                    }
                }
                break;
        }
        
        return items;
    }
    
    private void addMaterials(List<Material> list, Material... materials) {
        for (Material material : materials) {
            if (material != null && material.isItem()) {
                list.add(material);
            }
        }
    }
}
