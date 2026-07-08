package org.gsdistance.grimmsServer.Constructable.Entity;

import com.google.gson.Gson;
import org.bukkit.entity.Entity;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Logger;

public class EntityMetadata {
    public final UUID uuid;
    public final String timestamp;
    public int level = 1;

    public EntityMetadata(Entity entity) {
        this.uuid = entity.getUniqueId();
        this.timestamp = LocalDateTime.now().toString();
    }

    public void logMetadata() {
        Logger var10000 = GrimmsServer.logger;
        String var10001 = String.valueOf(this.uuid);
        var10000.info("Entity Metadata for " + var10001 + ":" + (new Gson()).toJson(this));
    }

    public void softSave() {
        PerSessionDataStorage.dataStore.put("entityMetadata-" + this.uuid, Data.of(this, EntityMetadata.class));
    }

    public void saveToFile() {
        this.softSave();
        GrimmsServer.pds.saveData(this, EntityMetadata.class, this.uuid.toString() + ".json", "entityMetadata");
    }

    public static EntityMetadata getEntityMetadata(Entity entity) {
        if (PerSessionDataStorage.dataStore.containsKey("entityMetadata-" + entity.getUniqueId())) {
            return (EntityMetadata) PerSessionDataStorage.dataStore.get("entityMetadata-" + entity.getUniqueId()).key();
        } else {
            EntityMetadata metadata = GrimmsServer.pds.retrieveData(entity.getUniqueId() + ".json", "entityMetadata", EntityMetadata.class);
            if (metadata == null) {
                metadata = new EntityMetadata(entity);
                GrimmsServer.logger.info("Created new EntityMetadata for " + entity.getUniqueId());
            } else {
                GrimmsServer.logger.info("Retrieved EntityMetadata for " + entity.getUniqueId());
            }

            PerSessionDataStorage.dataStore.put("entityMetadata-" + entity.getUniqueId(), Data.of(metadata, EntityMetadata.class));
            metadata.softSave();
            metadata.logMetadata();
            return metadata;
        }
    }
}
