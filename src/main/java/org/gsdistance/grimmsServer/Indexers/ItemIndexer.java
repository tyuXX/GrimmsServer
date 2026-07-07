package org.gsdistance.grimmsServer.Indexers;

import com.google.common.base.Stopwatch;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.*;
import org.gsdistance.grimmsServer.Data.Market.MarketBaseValues;
import org.gsdistance.grimmsServer.GrimmsServer;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class ItemIndexer {
    private static final Map<Material, Double> calculatedMarketValues = new HashMap<>();
    private static final Set<Material> skippedMarketValues = new HashSet<>();
    private static final Map<Material, List<Recipe>> recipeCache = new HashMap<>();
    public static boolean loggingEnabled = true;
    private static final ThreadLocal<Set<Material>> processingMaterials = ThreadLocal.withInitial(HashSet::new);

    public ItemIndexer() {
    }

    @SuppressWarnings("unchecked")
    public static void indexWholeRegistry() {
        GrimmsServer.logger.info("Starting to index item values...");
        Stopwatch stopwatch = Stopwatch.createStarted();
        calculatedMarketValues.clear();
        skippedMarketValues.clear();
        recipeCache.clear();
        cacheAllRecipes();

        Map<Material, Double> tmp;
        try {
            tmp = (Map<Material, Double>) GrimmsServer.pds.retrieveData("calculatedMarketValues.json", "cache", Map.class);
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

        Set<Material> materialsToProcess = Arrays.stream(Material.values())
                .filter(m -> m.isItem() && m != Material.AIR)
                .filter(m -> !MarketBaseValues.marketBaseValues.containsKey(m))
                .filter(m -> !calculatedMarketValues.containsKey(m))
                .collect(Collectors.toSet());

        Set<Material> firstPassSkipped = new HashSet<>();

        for (Material material : materialsToProcess) {
            double marketValue = calculateMarketValue(material);
            if (marketValue > 0.0) {
                calculatedMarketValues.put(material, Math.floor(marketValue));
                if (loggingEnabled) {
                    GrimmsServer.logger.info("Calculated market value for " + material + ": " + Math.floor(marketValue));
                }
            } else {
                firstPassSkipped.add(material);
            }
        }

        if (!cacheExists && !firstPassSkipped.isEmpty()) {
            for (int i = 0; i < 2; ++i) {
                if (loggingEnabled) {
                    GrimmsServer.logger.info("Recalculating skipped market values, attempt " + (i + 1));
                }

                Set<Material> stillSkipped = new HashSet<>();

                for (Material material : firstPassSkipped) {
                    double marketValue = calculateMarketValue(material);
                    if (marketValue > 0.0) {
                        calculatedMarketValues.put(material, marketValue);
                        if (loggingEnabled) {
                            GrimmsServer.logger.info("Calculated market value for " + material + ": " + marketValue);
                        }
                    } else {
                        stillSkipped.add(material);
                    }
                }

                firstPassSkipped = stillSkipped;
                if (stillSkipped.isEmpty()) {
                    break;
                }
            }

            skippedMarketValues.addAll(firstPassSkipped);
        }

        GrimmsServer.logger.info("Indexed " + calculatedMarketValues.size() + " items in (" + stopwatch.stop() + ").");
        GrimmsServer.logger.info("Skipped " + skippedMarketValues.size() + " items that could not be calculated.");
        GrimmsServer.pds.saveData(calculatedMarketValues, Map.class, "calculatedMarketValues.json", "cache");
        GrimmsServer.logger.info("Saved calculated market values to cache.");
        MarketBaseValues.marketBaseValues.putAll(calculatedMarketValues);
    }

    private static void cacheAllRecipes() {
        for (Material material : Material.values()) {
            if (material.isItem() && material != Material.AIR) {
                List<Recipe> recipes = Bukkit.getServer().getRecipesFor(new ItemStack(material));
                if (recipes != null && !recipes.isEmpty()) {
                    recipeCache.put(material, recipes);
                }
            }
        }
    }

    public static Double calculateMarketValue(@Nullable Material item) {
        Set<Material> processingSet = processingMaterials.get();
        processingSet.clear();
        return calculateMarketValueInternal(item, processingSet);
    }

    private static Double calculateMarketValueInternal(@Nullable Material item, Set<Material> processingMaterials) {
        if (item == null || item == Material.AIR) {
            return 0.0;
        }

        if (MarketBaseValues.marketBaseValues.containsKey(item)) {
            return MarketBaseValues.marketBaseValues.get(item);
        } else if (calculatedMarketValues.containsKey(item)) {
            return calculatedMarketValues.get(item);
        } else if (processingMaterials.contains(item)) {
            return 0.0;
        }

        processingMaterials.add(item);
        double marketValue = Double.MAX_VALUE;
        List<Recipe> recipes = recipeCache.get(item);

        if (recipes != null && !recipes.isEmpty()) {
            for (Recipe recipe : recipes) {
                if (recipe != null && recipe.getResult().getType() != Material.AIR) {
                    double recipeValue = 0.0;

                    // Refactored into Java 21 Pattern Matching for switch
                    switch (recipe) {
                        case ShapedRecipe shapedRecipe -> {
                            for (ItemStack ingredient : shapedRecipe.getIngredientMap().values()) {
                                if (ingredient != null && ingredient.getType() != Material.AIR) {
                                    double ingredientValue = calculateMarketValueInternal(ingredient.getType(), processingMaterials);
                                    recipeValue += ingredientValue * ingredient.getAmount();
                                }
                            }
                        }
                        case ShapelessRecipe shapelessRecipe -> {
                            for (ItemStack ingredient : shapelessRecipe.getIngredientList()) {
                                if (ingredient != null && ingredient.getType() != Material.AIR) {
                                    double ingredientValue = calculateMarketValueInternal(ingredient.getType(), processingMaterials);
                                    recipeValue += ingredientValue * ingredient.getAmount();
                                }
                            }
                        }
                        case FurnaceRecipe furnaceRecipe -> {
                            ItemStack result = furnaceRecipe.getResult();
                            if (result.getType() == item) {
                                double ingredientValue = calculateMarketValueInternal(furnaceRecipe.getInput().getType(), processingMaterials);
                                recipeValue = ingredientValue / result.getAmount();
                            }
                        }
                        case StonecuttingRecipe stonecuttingRecipe -> {
                            ItemStack result = stonecuttingRecipe.getResult();
                            if (result.getType() == item) {
                                double ingredientValue = calculateMarketValueInternal(stonecuttingRecipe.getInput().getType(), processingMaterials);
                                recipeValue = ingredientValue / result.getAmount();
                            }
                        }
                        case BlastingRecipe blastingRecipe -> {
                            ItemStack result = blastingRecipe.getResult();
                            if (result.getType() == item) {
                                double ingredientValue = calculateMarketValueInternal(blastingRecipe.getInput().getType(), processingMaterials);
                                recipeValue = ingredientValue / result.getAmount();
                            }
                        }
                        case SmithingRecipe smithingRecipe -> {
                            ItemStack result = smithingRecipe.getResult();
                            if (result.getType() == item) {
                                if (smithingRecipe.getBase() != null && smithingRecipe.getAddition() != null) {
                                    ItemStack base = smithingRecipe.getBase().getItemStack();
                                    ItemStack addition = smithingRecipe.getAddition().getItemStack();
                                    double baseValue = calculateMarketValueInternal(base.getType(), processingMaterials);
                                    double additionValue = calculateMarketValueInternal(addition.getType(), processingMaterials);
                                    recipeValue = (baseValue + additionValue) / result.getAmount();
                                }
                            }
                        }
                        default -> {
                            processingMaterials.remove(item);
                            return 0.0;
                        }
                    }

                    if (recipeValue > 0.0 && recipeValue < marketValue) {
                        marketValue = recipeValue;
                    }
                }
            }

            processingMaterials.remove(item);
            return marketValue == Double.MAX_VALUE ? 0.0 : marketValue;
        }

        return 0.0;
    }
}