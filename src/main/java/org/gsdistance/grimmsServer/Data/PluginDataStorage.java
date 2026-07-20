package org.gsdistance.grimmsServer.Data;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.logging.Level;

public class PluginDataStorage {
    private final JavaPlugin plugin;
    private final Gson jsonParser;

    public PluginDataStorage(JavaPlugin plugin) {
        this.plugin = plugin;
        this.jsonParser = createGson();
    }

    private Gson createGson() {
        return new GsonBuilder()
            .disableInnerClassSerialization()
            .excludeFieldsWithModifiers(java.lang.reflect.Modifier.STATIC, java.lang.reflect.Modifier.TRANSIENT)
            .setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    // Skip fields that contain Optional or other problematic types
                    String typeName = f.getDeclaredType().getTypeName();
                    return typeName.contains("java.util.Optional") ||
                           typeName.contains("java.lang.reflect") ||
                           typeName.contains("java.security") ||
                           typeName.contains("java.util.concurrent") ||
                           typeName.contains("org.bukkit");
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    // Skip all Bukkit classes from reflection
                    return clazz.getName().startsWith("org.bukkit");
                }
            })
            .create();
    }

    public boolean exists(String fileName, String addedPath) {
        File readFolder = new File(this.plugin.getDataFolder().getPath() + File.separatorChar + addedPath);
        File readFile = new File(readFolder.getPath() + File.separatorChar + fileName);
        return readFile.exists();
    }

    public <T> void saveData(T object, Class<T> objectType, String fileName, String addedPath) {
        saveDataInternal(object, objectType, fileName, addedPath);
    }

    public <T> void saveData(T object, Type objectType, String fileName, String addedPath) {
        saveDataInternal(object, objectType, fileName, addedPath);
    }

    private <T> void saveDataInternal(T object, Type objectType, String fileName, String addedPath) {
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
        return retrieveDataInternal(fileName, addedPath, ignoredType);
    }

    public <T> T retrieveData(String fileName, String addedPath, Type ignoredType) {
        return retrieveDataInternal(fileName, addedPath, ignoredType);
    }

    private <T> T retrieveDataInternal(String fileName, String addedPath, Type ignoredType) {
        File readFolder = new File(this.plugin.getDataFolder().getPath() + File.separatorChar + addedPath);
        File readFile = new File(readFolder.getPath() + File.separatorChar + fileName);
        if (!readFile.exists()) {
            return null;
        } else {
            try (FileReader reader = new FileReader(readFile)) {
                Object result = this.jsonParser.fromJson(reader, ignoredType);
                return (T) result;
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
                T[] dataObjects = (T[]) Array.newInstance(objectType, files.length);

                for (int i = 0; i < files.length; ++i) {
                    try (FileReader reader = new FileReader(files[i])) {
                        dataObjects[i] = this.jsonParser.fromJson(reader, objectType);
                    } catch (IOException e) {
                        GrimmsServer.logger.log(Level.WARNING, "Failed to retrieve data from " + files[i].getPath(), e);
                    }
                }

                return dataObjects;
            } else {
                return (T[]) Array.newInstance(objectType, 0);
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
