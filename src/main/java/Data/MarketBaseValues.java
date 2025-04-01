package Data;

import org.bukkit.Material;

import java.util.Map;

public class MarketBaseValues {
    public static final Map<Material, Double> marketBaseValues = Map.of(
            Material.DIAMOND, 100.0,
            Material.GOLD_INGOT, 50.0,
            Material.IRON_INGOT, 25.0,
            Material.COAL, 10.0,
            Material.EMERALD, 200.0,
            Material.REDSTONE, 5.0,
            Material.LAPIS_LAZULI, 15.0,
            Material.NETHERITE_INGOT, 500.0
    );
}
