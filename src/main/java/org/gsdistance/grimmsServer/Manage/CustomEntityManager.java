package org.gsdistance.grimmsServer.Manage;

import com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Entity;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.lang.reflect.Type;
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
        Type listType = new TypeToken<List<UUID>>() {
        }.getType();
        List<UUID> loaded = GrimmsServer.pds.retrieveData("entityRegistry.json", "customEntities", listType);
        if (loaded != null) {
            currentRegistry.clear();
            currentRegistry.addAll(loaded);
        }
    }
}
