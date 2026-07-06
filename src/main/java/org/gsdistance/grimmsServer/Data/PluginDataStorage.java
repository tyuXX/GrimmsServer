package org.gsdistance.grimmsServer.Data;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.GrimmsServer;

public class PluginDataStorage {
   private final JavaPlugin plugin;
   private final Gson jsonParser = new Gson();

   public PluginDataStorage(JavaPlugin plugin) {
      this.plugin = plugin;
   }

   public boolean exists(String fileName, String addedPath) {
      File readFolder = new File(this.plugin.getDataFolder().getPath() + File.separatorChar + addedPath);
      File readFile = new File(readFolder.getPath() + File.separatorChar + fileName);
      return readFile.exists();
   }

   public <T> void saveData(T object, Class<T> objectType, String fileName, String addedPath) {
      File writeFolder = new File(this.plugin.getDataFolder().getPath() + File.separatorChar + addedPath);
      File writeFile = new File(writeFolder.getPath() + File.separatorChar + fileName);
      if (!writeFolder.exists()) {
         writeFolder.mkdirs();
      }

      if (!writeFile.exists()) {
         try {
            writeFile.createNewFile();
         } catch (IOException e) {
            throw new RuntimeException(e);
         }
      }

      String data = this.jsonParser.toJson(object, objectType);

      try (FileWriter writer = new FileWriter(writeFile)) {
         writer.write(data);
      } catch (IOException e) {
         this.plugin.getLogger().warning("Failed to save data to " + writeFile.getPath());
         GrimmsServer.logger.log(Level.WARNING, "Failed to save data to " + writeFile.getPath(), e);
      }

   }

   public <T> T retrieveData(String fileName, String addedPath, Class<T> ignoredType) {
      File readFolder = new File(this.plugin.getDataFolder().getPath() + File.separatorChar + addedPath);
      File readFile = new File(readFolder.getPath() + File.separatorChar + fileName);
      if (!readFile.exists()) {
         return null;
      } else {
         try (FileReader reader = new FileReader(readFile)) {
            Object result = this.jsonParser.fromJson(reader, ignoredType);
            return (T)result;
         } catch (IOException e) {
            GrimmsServer.logger.log(Level.WARNING, "Failed to retrieve data from " + readFile.getPath(), e);
            return null;
         }
      }
   }

   public <T> T[] retrieveAllData(Class<T> objectType, String addedPath) {
      File readFolder = new File(this.plugin.getDataFolder().getPath() + File.separatorChar + addedPath);
      if (!readFolder.exists()) {
         return null;
      } else {
         File[] files = readFolder.listFiles();
         if (files != null && files.length != 0) {
            T[] dataObjects = (T[])((Object[])Array.newInstance(objectType, files.length));

            for(int i = 0; i < files.length; ++i) {
               try (FileReader reader = new FileReader(files[i])) {
                  dataObjects[i] = this.jsonParser.fromJson(reader, objectType);
               } catch (IOException e) {
                  GrimmsServer.logger.log(Level.WARNING, "Failed to retrieve data from " + files[i].getPath(), e);
               }
            }

            return dataObjects;
         } else {
            return (T[])((Object[])Array.newInstance(objectType, 0));
         }
      }
   }

   public void deleteData(String fileName, String addedPath) {
      File deleteFolder = new File(this.plugin.getDataFolder().getPath() + File.separatorChar + addedPath);
      File deleteFile = new File(deleteFolder.getPath() + File.separatorChar + fileName);
      if (deleteFile.exists()) {
         if (!deleteFile.delete()) {
            GrimmsServer.logger.log(Level.WARNING, "Failed to delete data file: " + deleteFile.getPath());
         }
      } else {
         GrimmsServer.logger.log(Level.WARNING, "Data file does not exist: " + deleteFile.getPath());
      }

   }
}
