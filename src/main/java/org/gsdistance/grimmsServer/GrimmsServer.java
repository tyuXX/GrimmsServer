package org.gsdistance.grimmsServer;

import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.Config.ConfigLoader;
import org.gsdistance.grimmsServer.Manage.EventTrigger;
import org.gsdistance.grimmsServer.Events.ServerStartupEvent;

import java.io.*;
import java.util.Arrays;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class GrimmsServer extends JavaPlugin {
    public static JavaPlugin instance;
    public static Logger logger;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        logger = this.getLogger();
        ConfigLoader.initialize(this);
        ConfigLoader.loadConfigFromFile();
        copyResourceFiles();
        getServer().getPluginManager().registerEvents(new EventTrigger(), this);
        ServerStartupEvent.Event();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void copyResourceFiles(){
        // Check if version.embed exists
        File versionEmbedFile = new File("" + getDataFolder() + File.separatorChar + "embed" + File.separatorChar + "version.embed");
        if (versionEmbedFile.exists()){
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
            if(Do){
                File embedFolder = new File(getDataFolder(), "embed");
                deleteDirectoryRecursively(embedFolder);
                saveResourceIfNotExists("embed", true); // Copy the embed folder from the jar
                logger.log(Level.INFO, "Updated embed folder from jar.");
            }
        }
        else {
            // If it does not exist, copy the embed folder from the jar
            saveResourceIfNotExists("embed", true);
        }
    }

    private void deleteDirectoryRecursively(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectoryRecursively(file);
                }
            }
        }
        directory.delete();
    }

    private void saveResourceIfNotExists(String resourcePath, boolean isDirectory) {
        File targetFile = new File(getDataFolder(), resourcePath);
        if (!targetFile.exists()) {
            if (isDirectory) {
                targetFile.mkdirs();
                try {
                    String jarPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
                    try (JarFile jarFile = new JarFile(jarPath)) {
                        jarFile.stream()
                            .filter(entry -> entry.getName().startsWith(resourcePath) && !entry.isDirectory())
                            .forEach(entry -> {
                                String relativePath = entry.getName().substring(resourcePath.length() + 1);
                                saveResourceIfNotExists(resourcePath + "/" + relativePath, false);
                            });
                    }
                } catch (IOException e) {
                    logger.warning("Failed to copy directory: " + resourcePath);
                    e.printStackTrace();
                }
            } else {
                try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourcePath);
                     OutputStream out = new FileOutputStream(targetFile)) {
                    if (in == null) {
                        logger.warning("Resource not found: " + resourcePath);
                        return;
                    }
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Failed to copy file: " + resourcePath, e);
                }
            }
        }
    }
}
