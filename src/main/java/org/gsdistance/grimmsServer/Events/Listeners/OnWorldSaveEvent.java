package org.gsdistance.grimmsServer.Events.Listeners;

import com.google.common.base.Stopwatch;
import java.lang.reflect.Type;
import org.bukkit.Bukkit;
import org.bukkit.event.world.WorldSaveEvent;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;

public class OnWorldSaveEvent {
   public static int i = 0;

   public OnWorldSaveEvent() {
   }

   public static void Event(WorldSaveEvent event) {
      ++i;
      if (i >= Bukkit.getWorlds().size()) {
         saveData();
         i = 0;
      }

   }

   public static void saveData() {
      Stopwatch sw = Stopwatch.createStarted();
      GrimmsServer.logger.info("Saving temporary data...");

      for(Data<Object, Type> data : PerSessionDataStorage.dataStore.values()) {
         Type type = ((Type)data.value()).getClass();
         if (type.equals(PlayerMetadata.class)) {
            ((PlayerMetadata)data.key()).saveToPDS();
         } else if (type.equals(Faction.class)) {
            ((Faction)data.key()).saveToFile();
         }
      }

      GrimmsServer.logger.info("Done (" + String.valueOf(sw.stop()) + ")");
   }
}
