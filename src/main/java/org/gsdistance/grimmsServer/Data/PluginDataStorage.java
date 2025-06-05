package org.gsdistance.grimmsServer.Data;

import com.google.gson.Gson;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.logging.Level;

public class PluginDataStorage {
    private final JavaPlugin plugin;
    private final Gson jsonParser;

    public PluginDataStorage(JavaPlugin plugin) {
        jsonParser = new Gson();
        this.plugin = plugin;
    }

    public boolean exists(String fileName, String addedPath) {
        File readFolder = new File(plugin.getDataFolder().getPath() + addedPath);
        File readFile = new File(readFolder.getPath() + File.separatorChar + fileName);
        return readFile.exists();
    }

    public void saveData(Object object, Type objectType, String fileName, String addedPath) {
        File writeFolder = new File(plugin.getDataFolder().getPath() + File.separatorChar + addedPath);
        File writeFile = new File(writeFolder.getPath() + File.separatorChar + fileName);
        if (!writeFolder.exists()) {
            writeFolder.mkdir();
        }
        if (!writeFile.exists()) {
            try {
                writeFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        String data = jsonParser.toJson(object, objectType);
        try (FileWriter writer = new FileWriter(writeFile)) {
            writer.write(data);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save data to " + writeFile.getPath());
            GrimmsServer.logger.log(Level.WARNING, "Failed to save data to " + writeFile.getPath(), e);
        }
    }

    public Object retrieveData(String fileName, Type objectType, String addedPath) {
        File readFolder = new File(plugin.getDataFolder().getPath() + addedPath);
        File readFile = new File(readFolder.getPath() + File.separatorChar + fileName);
        if (!readFile.exists()) {
            return null;
        }
        try (FileReader reader = new FileReader(readFile)) {
            return jsonParser.fromJson(reader, objectType);
        } catch (IOException e) {
            GrimmsServer.logger.log(Level.WARNING, "Failed to retrieve data from " + readFile.getPath(), e);
            return null;
        }
    }
}
