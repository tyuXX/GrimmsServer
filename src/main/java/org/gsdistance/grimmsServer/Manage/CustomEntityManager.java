package org.gsdistance.grimmsServer.Manage;

import org.bukkit.entity.Entity;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class CustomEntityManager {
    public static final List<UUID> currentRegistry = new CopyOnWriteArrayList<>();

    public CustomEntityManager() {
    }

    public static void registerEntity(Entity entity) {
        currentRegistry.add(entity.getUniqueId());
    }

    public static void unregisterEntity(Entity entity) {
        currentRegistry.remove(entity.getUniqueId());
    }

    public static void saveToFile() {
        GrimmsServer.pds.saveData(currentRegistry, List.class, "entityRegistry.json", "customEntities");
    }

    public static void loadFromFile() {
        List<UUID> loaded = GrimmsServer.pds.retrieveData("entityRegistry.json", "customEntities", List.class);
        if (loaded != null) {
            currentRegistry.clear();
            currentRegistry.addAll(loaded);
        }
    }
}
