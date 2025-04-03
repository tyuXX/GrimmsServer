package org.gsdistance.grimmsServer.Data;

import com.google.gson.Gson;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Type;

public class PluginDataStorage {
    private JavaPlugin plugin;
    private Gson jsonParser;

    public PluginDataStorage(JavaPlugin plugin) {
        jsonParser = new Gson();
        this.plugin = plugin;
    }

    public void saveData(Object object, Type objectType, String fileName, String addedPath) {
        File writeFolder = new File(plugin.getDataFolder().getPath() + addedPath);
        File file = new File(writeFolder.getPath() + fileName);
    }
}
