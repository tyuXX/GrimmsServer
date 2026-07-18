package org.gsdistance.grimmsServer.Commands.GLevelCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Config.ConfigKey;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.Player.PlayerCapability;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrestigeShop {
    public static final String GUI_TITLE = "Prestige Shop";
    private static final int GUI_SIZE = 27;
    private static final Map<Player, PrestigeShop> openGUIs = new HashMap<>();

    private final Player player;
    private final PlayerStats playerStats;
    private final PlayerMetadata playerMetadata;
    private final Inventory inventory;

    public PrestigeShop(Player player) {
        this.player = player;
        this.playerStats = PlayerStats.getPlayerStats(player);
        this.playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        this.inventory = Bukkit.createInventory(null, GUI_SIZE, GUI_TITLE);
        openGUIs.put(player, this);
    }

    public void open() {
        renderGUI();
        player.openInventory(inventory);
    }

    private void renderGUI() {
        inventory.clear();

        // Display player's prestige points
        ItemStack pointsItem = new ItemStack(Material.NETHER_STAR);
        ItemMeta pointsMeta = pointsItem.getItemMeta();
        if (pointsMeta != null) {
            long prestigePoints = playerStats.getStat("prestigePoints", Long.class);
            pointsMeta.setDisplayName(ChatColor.GOLD + "Prestige Points: " + prestigePoints);
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Use these points to purchase capabilities");
            pointsMeta.setLore(lore);
            pointsItem.setItemMeta(pointsMeta);
        }
        inventory.setItem(13, pointsItem);

        // Render all available capabilities for purchase
        int slot = 0;
        for (PlayerCapability capability : PlayerCapability.values()) {
            if (slot >= 13) slot++; // Skip the center slot
            if (slot >= 27) break;

            ItemStack capabilityItem = createCapabilityItem(capability);
            inventory.setItem(slot, capabilityItem);
            slot++;
        }

        // Add close button at the end
        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeItem.getItemMeta();
        if (closeMeta != null) {
            closeMeta.setDisplayName(ChatColor.RED + "Close");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Click to close the prestige shop");
            closeMeta.setLore(lore);
            closeItem.setItemMeta(closeMeta);
        }
        inventory.setItem(26, closeItem);
    }

    private ItemStack createCapabilityItem(PlayerCapability capability) {
        Material material;
        switch (capability) {
            case AUTOSELL:
                material = Material.GOLD_INGOT;
                break;
            case MAGNET:
                material = Material.IRON_NUGGET;
                break;
            case VEINMINER:
                material = Material.DIAMOND_PICKAXE;
                break;
            case SATURATION_PERK:
                material = Material.GOLDEN_CARROT;
                break;
            default:
                material = Material.PAPER;
        }

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            boolean isOwned = playerMetadata.capabilities.containsKey(capability);
            int price = getCapabilityPrice(capability);

            meta.setDisplayName(ChatColor.WHITE + capability.displayName);

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + capability.description);
            lore.add("");
            if (isOwned) {
                lore.add(ChatColor.GREEN + "Status: Owned");
                lore.add(ChatColor.GRAY + "Toggle in /gUtil setting");
            } else {
                lore.add(ChatColor.GOLD + "Price: " + price + " Prestige Points");
                lore.add(ChatColor.YELLOW + "Click to purchase");
            }
            meta.setLore(lore);

            item.setItemMeta(meta);
        }

        return item;
    }

    private int getCapabilityPrice(PlayerCapability capability) {
        return switch (capability) {
            case AUTOSELL -> ActiveConfig.getConfigValue(ConfigKey.PRESTIGE_CAPABILITY_AUTOSELL_PRICE, Integer.class);
            case MAGNET -> ActiveConfig.getConfigValue(ConfigKey.PRESTIGE_CAPABILITY_MAGNET_PRICE, Integer.class);
            case VEINMINER -> ActiveConfig.getConfigValue(ConfigKey.PRESTIGE_CAPABILITY_VEINMINER_PRICE, Integer.class);
            case SATURATION_PERK ->
                    ActiveConfig.getConfigValue(ConfigKey.PRESTIGE_CAPABILITY_SATURATION_PERK_PRICE, Integer.class);
            default -> 10;
        };
    }

    public void handleClick(int slot) {
        // Check if it's a capability slot
        if (slot < PlayerCapability.values().length) {
            PlayerCapability[] capabilities = PlayerCapability.values();
            if (slot < capabilities.length) {
                purchaseCapability(capabilities[slot]);
                renderGUI();
            }
            return;
        }

        // Skip center slot (prestige points display)
        if (slot == 13) {
            return;
        }

        // Adjust for skipped center slot
        int adjustedSlot = slot > 13 ? slot - 1 : slot;
        if (adjustedSlot < PlayerCapability.values().length) {
            PlayerCapability[] capabilities = PlayerCapability.values();
            if (adjustedSlot < capabilities.length) {
                purchaseCapability(capabilities[adjustedSlot]);
                renderGUI();
            }
            return;
        }

        // Close button
        if (slot == 26) {
            player.closeInventory();
        }
    }

    private void purchaseCapability(PlayerCapability capability) {
        if (playerMetadata.capabilities.containsKey(capability)) {
            player.sendMessage(ChatColor.RED + "You already own this capability. Toggle it in /gUtil setting");
            return;
        }

        int price = getCapabilityPrice(capability);
        long prestigePoints = playerStats.getStat("prestigePoints", Long.class);

        if (prestigePoints < price) {
            player.sendMessage(ChatColor.RED + "You don't have enough prestige points. Need " + price + ", have " + prestigePoints);
            return;
        }

        playerStats.setStat("prestigePoints", prestigePoints - price);
        playerMetadata.capabilities.put(capability, 1);
        playerMetadata.saveToPDS();

        player.sendMessage(ChatColor.GREEN + "Purchased: " + capability.displayName);
        player.sendMessage(ChatColor.GOLD + "Remaining prestige points: " + (prestigePoints - price));
    }

    public static PrestigeShop getGUI(Player player) {
        return openGUIs.get(player);
    }

    public static void closeGUI(Player player) {
        openGUIs.remove(player);
    }
}
