package org.gsdistance.grimmsServer.Indexers;

import com.google.common.base.Stopwatch;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.gsdistance.grimmsServer.Data.Market.MarketBaseValues;
import org.gsdistance.grimmsServer.GrimmsServer;

import javax.annotation.Nullable;
import java.util.*;

public class ItemIndexer {
    private static final Map<Material, Double> calculatedMarketValues = new HashMap<>();
    private static final List<Material> skippedMarketValues = new ArrayList<>();
    public static boolean loggingEnabled = true;

    public static void indexWholeRegistry(){
        GrimmsServer.logger.info("Starting to index item values...");
        Stopwatch stopwatch = Stopwatch.createStarted();
        // Clear previous values
        calculatedMarketValues.clear();
        skippedMarketValues.clear();
        // Check the cache first
        Map<Material, Double> tmp;
        try {
            tmp  = GrimmsServer.pds.retrieveData("calculatedMarketValues.json", "cache", Map.class);
        } catch (Exception e) {
            GrimmsServer.logger.warning("Failed to load cached market values: " + e.getMessage());
            tmp = null;
        }
        boolean cacheExists = false;
        if (tmp != null && !tmp.isEmpty()) {
            GrimmsServer.logger.info("Using cached market values for items.");
            GrimmsServer.logger.info("Loaded " + tmp.size() + " items from cache.");
            calculatedMarketValues.putAll(tmp);
            cacheExists = true;
        }
        // Iterate through all materials and calculate market values
        for (Material material : Material.values()) {
            if (material.isItem() && material != Material.AIR && !MarketBaseValues.marketBaseValues.containsKey(material) && !calculatedMarketValues.containsKey(material)) {
                double marketValue = calculateMarketValue(material);
                if (marketValue > 0) {
                    calculatedMarketValues.put(material, Math.floor(marketValue));
                    if (loggingEnabled) {
                        GrimmsServer.logger.info("Calculated market value for " + material + ": " + Math.floor(marketValue));
                    }
                }
                else{
                    skippedMarketValues.add(material);
                }
            }
        }
        // Retry calculating market values for skipped items
        if(!cacheExists){
            for (int i = 0; i < 2; i++) {
                if (loggingEnabled) {
                    GrimmsServer.logger.info("Recalculating skipped market values, attempt " + (i + 1));
                }
                for (Material material : skippedMarketValues) {
                    if (material.isItem() && material != Material.AIR && !MarketBaseValues.marketBaseValues.containsKey(material)) {
                        double marketValue = calculateMarketValue(material);
                        if (marketValue > 0) {
                            calculatedMarketValues.put(material, marketValue);
                            skippedMarketValues.remove(material);
                            if (loggingEnabled) {
                                GrimmsServer.logger.info("Calculated market value for " + material + ": " + marketValue);
                            }
                        }
                    }
                }
            }
        }
        GrimmsServer.logger.info("Indexed " + calculatedMarketValues.size() + " items in (" + stopwatch.stop() + ").");
        GrimmsServer.logger.info("Skipped " + skippedMarketValues.size() + " items that could not be calculated.");

        // Save the calculated market values to the cache
        GrimmsServer.pds.saveData(calculatedMarketValues, Map.class, "calculatedMarketValues.json", "cache");
        GrimmsServer.logger.info("Saved calculated market values to cache.");
        // Add the calculated values to the MarketBaseValues
        MarketBaseValues.marketBaseValues.putAll(calculatedMarketValues);
    }

    public static Double calculateMarketValue(@Nullable Material item) {
        // Track materials being processed to prevent infinite recursion
        Set<Material> processingMaterials = new HashSet<>();
        return calculateMarketValueInternal(item, processingMaterials);
    }

    private static Double calculateMarketValueInternal(@Nullable Material item, Set<Material> processingMaterials) {
        if (item == null || item == Material.AIR) {
            return 0.0;
        }

        // Check if the item is already calculated
        if (MarketBaseValues.marketBaseValues.containsKey(item)) {
            return MarketBaseValues.marketBaseValues.get(item);
        }
        if (calculatedMarketValues.containsKey(item)) {
            return calculatedMarketValues.get(item);
        }

        if (processingMaterials.contains(item)) {
            return 0.0; // Prevent circular dependency
        }

        processingMaterials.add(item);
        double marketValue = 0.0;

        for (Recipe recipe : Bukkit.getServer().getRecipesFor(new ItemStack(item))) {
            if (recipe == null || recipe.getResult().getType() == Material.AIR) {
                continue; // Skip invalid recipes
            }

            double recipeValue = 0.0;
            // TODO - IDK something feels off here maybe check the calculation logic sometime
            switch (recipe) {
                case org.bukkit.inventory.ShapedRecipe shapedRecipe -> {
                    for (ItemStack ingredient : shapedRecipe.getIngredientMap().values()) {
                        if (ingredient != null && ingredient.getType() != Material.AIR) {
                            double ingredientValue = calculateMarketValueInternal(ingredient.getType(), processingMaterials);
                            recipeValue += ingredientValue * ingredient.getAmount();
                        }
                    }
                }
                case org.bukkit.inventory.ShapelessRecipe shapelessRecipe -> {
                    for (ItemStack ingredient : shapelessRecipe.getIngredientList()) {
                        if (ingredient != null && ingredient.getType() != Material.AIR) {
                            double ingredientValue = calculateMarketValueInternal(ingredient.getType(), processingMaterials);
                            recipeValue += ingredientValue * ingredient.getAmount();
                        }
                    }
                }
                case org.bukkit.inventory.FurnaceRecipe furnaceRecipe -> {
                    ItemStack result = furnaceRecipe.getResult();
                    if (result.getType() == item) {
                        double ingredientValue = calculateMarketValueInternal(furnaceRecipe.getInput().getType(), processingMaterials);
                        recipeValue = ingredientValue * result.getAmount();
                    }
                }
                case org.bukkit.inventory.StonecuttingRecipe stonecuttingRecipe -> {
                    ItemStack result = stonecuttingRecipe.getResult();
                    if (result.getType() == item) {
                        double ingredientValue = calculateMarketValueInternal(stonecuttingRecipe.getInput().getType(), processingMaterials);
                        recipeValue = ingredientValue * result.getAmount();
                    }
                }
                case org.bukkit.inventory.BlastingRecipe blastingRecipe -> {
                    ItemStack result = blastingRecipe.getResult();
                    if (result.getType() == item) {
                        double ingredientValue = calculateMarketValueInternal(blastingRecipe.getInput().getType(), processingMaterials);
                        recipeValue = ingredientValue * result.getAmount();
                    }
                }
                case org.bukkit.inventory.SmithingRecipe smithingRecipe -> {
                    ItemStack result = smithingRecipe.getResult();
                    if (result.getType() == item) {
                        if (smithingRecipe.getBase() == null || smithingRecipe.getAddition() == null) {
                            continue; // Skip if base or addition is null
                        }
                        ItemStack base = smithingRecipe.getBase().getItemStack();
                        ItemStack addition = smithingRecipe.getAddition().getItemStack();
                        double baseValue = calculateMarketValueInternal(base.getType(), processingMaterials);
                        double additionValue = calculateMarketValueInternal(addition.getType(), processingMaterials);
                        recipeValue = (baseValue + additionValue) * result.getAmount();
                    }
                }
                default -> {
                    return 0.0; // Unsupported recipe type
                }
            }
            recipeValue = recipeValue / recipe.getResult().getAmount(); // Normalize by result amount
            if (recipeValue > marketValue) {
                marketValue = recipeValue;
            }
        }

        processingMaterials.remove(item);
        return marketValue;
    }
}
