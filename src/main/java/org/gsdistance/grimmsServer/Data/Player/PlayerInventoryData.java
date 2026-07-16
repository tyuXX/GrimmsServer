package org.gsdistance.grimmsServer.Data.Player;

import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Config.ConfigKey;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.time.LocalDateTime;
import java.util.*;

public class PlayerInventoryData {
    public final UUID uuid;
    public final String timestamp;
    public Map<Integer, ItemStack> inventoryContents;
    public ItemStack[] armorContents;
    public ItemStack[] extraContents;
    public List<InventorySnapshot> previousInventories;

    public PlayerInventoryData(UUID uuid) {
        this.uuid = uuid;
        this.timestamp = LocalDateTime.now().toString();
        this.inventoryContents = new HashMap<>();
        this.armorContents = new ItemStack[4];
        this.extraContents = new ItemStack[36];
        this.previousInventories = new ArrayList<>();
    }

    public void addPreviousInventory(InventorySnapshot snapshot) {
        int maxInventories = ActiveConfig.getConfigValue(ConfigKey.MAX_HISTORICAL_INVENTORIES, Integer.class);
        this.previousInventories.add(snapshot);
        if (this.previousInventories.size() > maxInventories) {
            this.previousInventories.remove(0);
        }
    }

    public static class InventorySnapshot {
        public final String timestamp;
        public Map<Integer, ItemStack> inventoryContents;
        public ItemStack[] armorContents;
        public ItemStack[] extraContents;

        public InventorySnapshot(Map<Integer, ItemStack> inventoryContents, ItemStack[] armorContents, ItemStack[] extraContents) {
            this.timestamp = LocalDateTime.now().toString();
            this.inventoryContents = inventoryContents;
            this.armorContents = armorContents;
            this.extraContents = extraContents;
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
