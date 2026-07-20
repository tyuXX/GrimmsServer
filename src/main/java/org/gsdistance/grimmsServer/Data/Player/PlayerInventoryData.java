package org.gsdistance.grimmsServer.Data.Player;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Config.ConfigKey;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class PlayerInventoryData {
    public final UUID uuid;
    public final String timestamp;
    public Map<Integer, String> inventoryContents; // Store as Base64 strings
    public String[] armorContents; // Store as Base64 strings
    public String[] extraContents; // Store as Base64 strings
    public List<InventorySnapshot> previousInventories;

    public PlayerInventoryData(UUID uuid) {
        this.uuid = uuid;
        this.timestamp = LocalDateTime.now().toString();
        this.inventoryContents = new HashMap<>();
        this.armorContents = new String[4];
        this.extraContents = new String[36];
        this.previousInventories = new ArrayList<>();
    }

    public void addPreviousInventory(InventorySnapshot snapshot) {
        int maxInventories = ActiveConfig.getConfigValue(ConfigKey.MAX_HISTORICAL_INVENTORIES, Integer.class);
        this.previousInventories.add(snapshot);
        if (this.previousInventories.size() > maxInventories) {
            this.previousInventories.remove(0);
        }
    }

    // Helper methods to convert ItemStack to/from Base64
    public static String itemStackToBase64(ItemStack itemStack) throws IOException {
        if (itemStack == null) return null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream)) {
            bukkitObjectOutputStream.writeObject(itemStack);
            bukkitObjectOutputStream.flush();
            byte[] data = byteArrayOutputStream.toByteArray();
            return Base64.getEncoder().encodeToString(data);
        }
    }

    public static ItemStack itemStackFromBase64(String base64) throws IOException, ClassNotFoundException {
        if (base64 == null) return null;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64));
             BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream)) {
            return (ItemStack) bukkitObjectInputStream.readObject();
        }
    }

    // Method to set inventory from actual ItemStacks
    public void setInventoryFromItemStacks(Map<Integer, ItemStack> itemStacks) {
        this.inventoryContents = new HashMap<>();
        for (Map.Entry<Integer, ItemStack> entry : itemStacks.entrySet()) {
            try {
                String base64 = itemStackToBase64(entry.getValue());
                if (base64 != null) {
                    this.inventoryContents.put(entry.getKey(), base64);
                }
            } catch (IOException e) {
                GrimmsServer.logger.warning("Failed to serialize ItemStack at slot " + entry.getKey());
            }
        }
    }

    // Method to get inventory as ItemStacks
    public Map<Integer, ItemStack> getInventoryAsItemStacks() {
        Map<Integer, ItemStack> result = new HashMap<>();
        for (Map.Entry<Integer, String> entry : this.inventoryContents.entrySet()) {
            try {
                ItemStack itemStack = itemStackFromBase64(entry.getValue());
                if (itemStack != null) {
                    result.put(entry.getKey(), itemStack);
                }
            } catch (IOException | ClassNotFoundException e) {
                GrimmsServer.logger.warning("Failed to deserialize ItemStack at slot " + entry.getKey());
            }
        }
        return result;
    }

    // Method to set armor from ItemStack array
    public void setArmorFromItemStacks(ItemStack[] armor) {
        this.armorContents = new String[4];
        for (int i = 0; i < armor.length && i < 4; i++) {
            try {
                this.armorContents[i] = itemStackToBase64(armor[i]);
            } catch (IOException e) {
                GrimmsServer.logger.warning("Failed to serialize armor slot " + i);
            }
        }
    }

    // Method to get armor as ItemStack array
    public ItemStack[] getArmorAsItemStacks() {
        ItemStack[] result = new ItemStack[4];
        for (int i = 0; i < this.armorContents.length && i < 4; i++) {
            try {
                result[i] = itemStackFromBase64(this.armorContents[i]);
            } catch (IOException | ClassNotFoundException e) {
                GrimmsServer.logger.warning("Failed to deserialize armor slot " + i);
            }
        }
        return result;
    }

    // Method to set extra contents from ItemStack array
    public void setExtraFromItemStacks(ItemStack[] extra) {
        this.extraContents = new String[Math.min(extra.length, 36)];
        for (int i = 0; i < this.extraContents.length; i++) {
            try {
                this.extraContents[i] = itemStackToBase64(extra[i]);
            } catch (IOException e) {
                GrimmsServer.logger.warning("Failed to serialize extra slot " + i);
            }
        }
    }

    // Method to get extra contents as ItemStack array
    public ItemStack[] getExtraAsItemStacks() {
        ItemStack[] result = new ItemStack[this.extraContents.length];
        for (int i = 0; i < this.extraContents.length; i++) {
            try {
                result[i] = itemStackFromBase64(this.extraContents[i]);
            } catch (IOException | ClassNotFoundException e) {
                GrimmsServer.logger.warning("Failed to deserialize extra slot " + i);
            }
        }
        return result;
    }

    public static class InventorySnapshot {
        public final String timestamp;
        public Map<Integer, String> inventoryContents; // Store as Base64 strings
        public String[] armorContents; // Store as Base64 strings
        public String[] extraContents; // Store as Base64 strings

        public InventorySnapshot(Map<Integer, String> inventoryContents, String[] armorContents, String[] extraContents) {
            this.timestamp = LocalDateTime.now().toString();
            this.inventoryContents = inventoryContents;
            this.armorContents = armorContents;
            this.extraContents = extraContents;
        }

        // Method to get inventory as ItemStacks
        public Map<Integer, ItemStack> getInventoryAsItemStacks() {
            Map<Integer, ItemStack> result = new HashMap<>();
            for (Map.Entry<Integer, String> entry : this.inventoryContents.entrySet()) {
                try {
                    ItemStack itemStack = PlayerInventoryData.itemStackFromBase64(entry.getValue());
                    if (itemStack != null) {
                        result.put(entry.getKey(), itemStack);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    GrimmsServer.logger.warning("Failed to deserialize ItemStack at slot " + entry.getKey());
                }
            }
            return result;
        }

        // Method to get armor as ItemStack array
        public ItemStack[] getArmorAsItemStacks() {
            ItemStack[] result = new ItemStack[4];
            for (int i = 0; i < this.armorContents.length && i < 4; i++) {
                try {
                    result[i] = PlayerInventoryData.itemStackFromBase64(this.armorContents[i]);
                } catch (IOException | ClassNotFoundException e) {
                    GrimmsServer.logger.warning("Failed to deserialize armor slot " + i);
                }
            }
            return result;
        }

        // Method to get extra contents as ItemStack array
        public ItemStack[] getExtraAsItemStacks() {
            ItemStack[] result = new ItemStack[this.extraContents.length];
            for (int i = 0; i < this.extraContents.length; i++) {
                try {
                    result[i] = PlayerInventoryData.itemStackFromBase64(this.extraContents[i]);
                } catch (IOException | ClassNotFoundException e) {
                    GrimmsServer.logger.warning("Failed to deserialize extra slot " + i);
                }
            }
            return result;
        }
    }

    public void saveToPDS() {
        GrimmsServer.pds.saveData(this, PlayerInventoryData.class, this.uuid.toString() + ".json", "playerInventories");
    }

    public static PlayerInventoryData getPlayerInventoryData(UUID uuid) {
        PlayerInventoryData data = GrimmsServer.pds.retrieveData(uuid.toString() + ".json", "playerInventories", PlayerInventoryData.class);
        if (data == null) {
            data = new PlayerInventoryData(uuid);
        }
        return data;
    }
}
