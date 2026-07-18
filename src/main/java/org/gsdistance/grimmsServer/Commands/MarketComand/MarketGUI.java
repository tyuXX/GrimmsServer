package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.Shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketGUI {
    public static final String GUI_TITLE = "Market";
    private static final int GUI_SIZE = 54;
    private static final Map<Player, MarketGUI> openGUIs = new HashMap<>();

    private final Player player;
    private final Market market;
    private MarketCategory currentCategory;
    private int currentPage;
    private final Inventory inventory;

    public MarketGUI(Player player) {
        this.player = player;
        this.market = Market.getMarket();
        this.currentCategory = MarketCategory.ALL;
        this.currentPage = 0;
        this.inventory = Bukkit.createInventory(null, GUI_SIZE, GUI_TITLE);
        openGUIs.put(player, this);
    }

    public void open() {
        renderGUI();
        player.openInventory(inventory);
    }

    private void renderGUI() {
        inventory.clear();

        // Render category selector (top row)
        renderCategorySelector();

        // Render items
        renderItems();

        // Render navigation buttons
        renderNavigation();

        // Render player info
        renderPlayerInfo();
    }

    private void renderCategorySelector() {
        int slot = 0;
        for (MarketCategory category : MarketCategory.values()) {
            ItemStack categoryItem = category.getDisplayItem();
            ItemMeta meta = categoryItem.getItemMeta();

            if (meta != null) {
                meta.setDisplayName(ChatColor.WHITE + category.getName());
                if (currentCategory == category) {
                    meta.addEnchant(Enchantment.UNBREAKING, 1, true);
                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GREEN + "Selected");
                    meta.setLore(lore);
                }
                categoryItem.setItemMeta(meta);
            }

            inventory.setItem(slot, categoryItem);
            slot++;
        }
    }

    private void renderItems() {
        List<Material> items = currentCategory.getItems();
        int itemsPerPage = 28; // 4 rows of 7 items
        int startIndex = currentPage * itemsPerPage;

        int slot = 9; // Start after category row
        for (int i = startIndex; i < Math.min(startIndex + itemsPerPage, items.size()); i++) {
            Material material = items.get(i);
            Long stock = market.items.get(material.getKey().getKey());
            double price = market.getPrice(material);

            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                meta.setDisplayName(ChatColor.YELLOW + material.name());
                List<String> lore = new ArrayList<>();
                double minPrice = org.gsdistance.grimmsServer.Config.ActiveConfig.getConfigValue(org.gsdistance.grimmsServer.Config.ConfigKey.MARKET_MIN_PRICE, Double.class);
                lore.add(ChatColor.GRAY + "Stock: " + ChatColor.GOLD + (stock != null ? stock : 0));
                lore.add(ChatColor.GRAY + "Price: " + ChatColor.GOLD + Shared.formatNumber(Math.max(minPrice, Math.round(price))));
                lore.add("");
                lore.add(ChatColor.GREEN + "Left-click to Buy");
                lore.add(ChatColor.RED + "Right-click to Sell");
                lore.add(ChatColor.AQUA.toString() + ChatColor.ITALIC + "Shift-click for bulk");
                meta.setLore(lore);
                item.setItemMeta(meta);
            }

            inventory.setItem(slot, item);
            slot++;

            // Skip every 8th slot for column separation
            if ((slot - 9) % 8 == 7) {
                slot++;
            }
        }
    }

    private void renderNavigation() {
        // Previous page button
        ItemStack prevItem = new ItemStack(Material.ARROW);
        ItemMeta prevMeta = prevItem.getItemMeta();
        if (prevMeta != null) {
            prevMeta.setDisplayName(ChatColor.YELLOW + "Previous Page");
            prevItem.setItemMeta(prevMeta);
        }
        inventory.setItem(45, prevItem);

        // Next page button
        ItemStack nextItem = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = nextItem.getItemMeta();
        if (nextMeta != null) {
            nextMeta.setDisplayName(ChatColor.YELLOW + "Next Page");
            nextItem.setItemMeta(nextMeta);
        }
        inventory.setItem(53, nextItem);

        // Refresh button
        ItemStack refreshItem = new ItemStack(Material.COMPASS);
        ItemMeta refreshMeta = refreshItem.getItemMeta();
        if (refreshMeta != null) {
            refreshMeta.setDisplayName(ChatColor.YELLOW + "Refresh Prices");
            refreshItem.setItemMeta(refreshMeta);
        }
        inventory.setItem(49, refreshItem);
    }

    private void renderPlayerInfo() {
        // Player balance
        ItemStack balanceItem = new ItemStack(Material.GOLD_INGOT);
        ItemMeta balanceMeta = balanceItem.getItemMeta();
        if (balanceMeta != null) {
            double balance = org.gsdistance.grimmsServer.Stats.PlayerStats.getPlayerStats(player).getStat("money", Double.class);
            balanceMeta.setDisplayName(ChatColor.GOLD + "Your Balance: " + ChatColor.GREEN + Shared.formatNumber(balance));
            balanceItem.setItemMeta(balanceMeta);
        }
        inventory.setItem(48, balanceItem);
    }

    public void handleClick(int slot, boolean isLeftClick, boolean isShiftClick) {
        // Category selection (top row)
        if (slot < 9) {
            MarketCategory[] categories = MarketCategory.values();
            if (slot < categories.length) {
                currentCategory = categories[slot];
                currentPage = 0;
                renderGUI();
            }
            return;
        }

        // Navigation buttons
        if (slot == 45) { // Previous page
            if (currentPage > 0) {
                currentPage--;
                renderGUI();
            }
            return;
        }

        if (slot == 53) { // Next page
            int itemsPerPage = 28;
            List<Material> items = currentCategory.getItems();
            if ((currentPage + 1) * itemsPerPage < items.size()) {
                currentPage++;
                renderGUI();
            }
            return;
        }

        if (slot == 49) { // Refresh
            market.reCalcNegMarketSaturation();
            renderGUI();
            player.sendMessage(ChatColor.GREEN + "Market prices refreshed!");
            return;
        }

        // Item clicks (middle section)
        if (slot >= 9 && slot < 45 && slot % 9 != 8) {
            int adjustedSlot = slot - 9;
            int row = adjustedSlot / 9;
            int col = adjustedSlot % 9;
            int itemIndex = row * 7 + col;

            int itemsPerPage = 28;
            int startIndex = currentPage * itemsPerPage;
            int actualIndex = startIndex + itemIndex;

            List<Material> items = currentCategory.getItems();
            if (actualIndex < items.size()) {
                Material material = items.get(actualIndex);

                if (isLeftClick) {
                    handleBuy(material, isShiftClick);
                } else {
                    handleSell(material, isShiftClick);
                }

                renderGUI();
            }
        }
    }

    private void handleBuy(Material material, boolean isShiftClick) {
        int amount = isShiftClick ? 64 : 1;
        Long stock = market.items.get(material.getKey().getKey());

        if (stock == null || stock == 0) {
            player.sendMessage(ChatColor.RED + "Item not available in market.");
            return;
        }

        if (stock < amount) {
            amount = stock.intValue();
        }

        double bought = market.buy(material, amount, player);
        double minPrice = org.gsdistance.grimmsServer.Config.ActiveConfig.getConfigValue(org.gsdistance.grimmsServer.Config.ConfigKey.MARKET_MIN_PRICE, Double.class);
        if (bought > 0) {
            market.saveMarket();
            player.sendMessage(ChatColor.GREEN + "Bought " + amount + " " + material.name() + " for " + Shared.formatNumber(Math.max(minPrice, Math.round(bought))));
        } else {
            player.sendMessage(ChatColor.RED + "Not enough money to buy " + material.name());
        }
    }

    private void handleSell(Material material, boolean isShiftClick) {
        int amount = isShiftClick ? 64 : 1;

        // Count items in inventory
        int totalInInventory = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material) {
                totalInInventory += item.getAmount();
            }
        }

        if (totalInInventory == 0) {
            player.sendMessage(ChatColor.RED + "You don't have any " + material.name() + " to sell.");
            return;
        }

        amount = Math.min(amount, totalInInventory);

        ItemStack sellItem = new ItemStack(material, amount);
        double sold = market.sell(sellItem, player);
        double minPrice = org.gsdistance.grimmsServer.Config.ActiveConfig.getConfigValue(org.gsdistance.grimmsServer.Config.ConfigKey.MARKET_MIN_PRICE, Double.class);
        if (sold > 0) {
            market.saveMarket();
            player.sendMessage(ChatColor.GREEN + "Sold " + amount + " " + material.name() + " for " + Shared.formatNumber(Math.max(minPrice, Math.round(sold))));
        }
    }

    public static MarketGUI getGUI(Player player) {
        return openGUIs.get(player);
    }

    public static void closeGUI(Player player) {
        openGUIs.remove(player);
    }
}
