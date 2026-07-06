package org.gsdistance.grimmsServer.Manage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.Entity;

public class CustomEntityManager {
   public static final List<UUID> currentRegistry = new ArrayList();

   public CustomEntityManager() {
   }

   public static void registerEntity(Entity entity) {
      currentRegistry.add(entity.getUniqueId());
   }
}
