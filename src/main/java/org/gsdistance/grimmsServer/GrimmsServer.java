package org.gsdistance.grimmsServer;

import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.Config.ConfigLoader;
import org.gsdistance.grimmsServer.Data.PluginDataStorage;
import org.gsdistance.grimmsServer.Events.Listeners.ServerStartupEvent;
import org.gsdistance.grimmsServer.Manage.EventRegistry;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.gsdistance.grimmsServer.Shared.saveResourceIfNotExists;
import static org.gsdistance.grimmsServer.Shared.updateResource;

public final class GrimmsServer extends JavaPlugin {
    public static JavaPlugin instance;
    public static Logger logger;
    public static PluginDataStorage pds;
    public static List<Function<JavaPlugin,String>> hotReloadFuncs;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        logger = this.getLogger();
        pds = new PluginDataStorage(this);
        ConfigLoader.initialize(this);
        ConfigLoader.loadConfigFromFile();
        copyResourceFiles();
        getServer().getPluginManager().registerEvents(new EventRegistry(), this);
        ServerStartupEvent.Event();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public String[] fullHotReload(){
        String[] rt = new String[hotReloadFuncs.size()];
        for (int i = 0; i < hotReloadFuncs.size(); i++) {
            rt[i] = hotReloadFuncs.get(i).apply(instance);
        }
        return rt;
    }

    public void copyResourceFiles() {
        // Check if version.embed exists
        File versionEmbedFile = new File("" + getDataFolder() + File.separatorChar + "embed" + File.separatorChar + "version.embed");
        if (versionEmbedFile.exists()) {
            boolean Do = false;
            // Updated code
            try (InputStream jarStream = getClass().getResourceAsStream("/embed/version.embed");
                 InputStream fileStream = new FileInputStream(versionEmbedFile)) {
                if (jarStream != null) {
                    byte[] jarBytes = jarStream.readAllBytes();
                    byte[] fileBytes = fileStream.readAllBytes();
                    if (!Arrays.equals(jarBytes, fileBytes)) {
                        // Files are different, delete the embed folder
                        Do = true;
                    }
                }
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error comparing version.embed files", e);
            }
            if (Do) {
                updateResource("embed", true);
                updateResource("worldConstructors", true);
                logger.log(Level.INFO, "Updated resources from jar.");
            }
        } else {
            // If it does not exist, copy the embed folder from the jar
            saveResourceIfNotExists("embed", true);
        }
    }
}
