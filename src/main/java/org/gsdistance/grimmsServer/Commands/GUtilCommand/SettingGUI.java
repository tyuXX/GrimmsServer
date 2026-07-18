package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.Player.PlayerCapability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingGUI {
    public static final String GUI_TITLE = "Settings";
    private static final int GUI_SIZE = 27;
    private static final Map<Player, SettingGUI> openGUIs = new HashMap<>();
    private static final Map<Player, Long> lastClickTime = new HashMap<>();
    private static final long COOLDOWN_MS = 1000;

    private final Player player;
    private final PlayerMetadata playerMetadata;
    private final Inventory inventory;

    public SettingGUI(Player player) {
        this.player = player;
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

        // Render all available settings
        int slot = 0;
        for (PlayerCapability capability : PlayerCapability.values()) {
            ItemStack settingItem = createSettingItem(capability);
            inventory.setItem(slot, settingItem);
            slot++;
        }

        // Add close button at the end
        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeItem.getItemMeta();
        if (closeMeta != null) {
            closeMeta.setDisplayName(ChatColor.RED + "Close");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Click to close the settings menu");
            closeMeta.setLore(lore);
            closeItem.setItemMeta(closeMeta);
        }
        inventory.setItem(26, closeItem);
    }

    private ItemStack createSettingItem(PlayerCapability capability) {
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
            boolean isEnabled = playerMetadata.settings.contains(capability.capabilityId);

            meta.setDisplayName(ChatColor.WHITE + capability.displayName);

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + capability.description);
            lore.add("");
            if (isEnabled) {
                lore.add(ChatColor.GREEN + "Status: Enabled");
                meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            } else {
                lore.add(ChatColor.RED + "Status: Disabled");
            }
            lore.add("");
            lore.add(ChatColor.YELLOW + "Click to toggle");
            meta.setLore(lore);

            item.setItemMeta(meta);
        }

        return item;
    }

    public void handleClick(int slot) {
        // Check cooldown
        long currentTime = System.currentTimeMillis();
        long lastClick = lastClickTime.getOrDefault(player, 0L);
        if (currentTime - lastClick < COOLDOWN_MS) {
            return;
        }
        lastClickTime.put(player, currentTime);

        // Check if it's a setting slot
        if (slot < PlayerCapability.values().length) {
            PlayerCapability[] capabilities = PlayerCapability.values();
            if (slot < capabilities.length) {
                toggleSetting(capabilities[slot]);
                renderGUI();
            }
            return;
        }

        // Close button
        if (slot == 26) {
            player.closeInventory();
        }
    }

    private void toggleSetting(PlayerCapability capability) {
        String settingName = capability.capabilityId;

        if (playerMetadata.settings.contains(settingName)) {
            playerMetadata.settings.remove(settingName);
            player.sendMessage(ChatColor.RED + "Disabled: " + capability.displayName);
        } else {
            playerMetadata.settings.add(settingName);
            player.sendMessage(ChatColor.GREEN + "Enabled: " + capability.displayName);
        }

        playerMetadata.saveToPDS();
    }

    public static SettingGUI getGUI(Player player) {
        return openGUIs.get(player);
    }

    public static void closeGUI(Player player) {
        openGUIs.remove(player);
    }
}
