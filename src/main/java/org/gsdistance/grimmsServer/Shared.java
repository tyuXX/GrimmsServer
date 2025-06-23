package org.gsdistance.grimmsServer;

import org.bukkit.entity.Player;

import java.io.*;
import java.util.jar.JarFile;
import java.util.logging.Level;

import static org.gsdistance.grimmsServer.GrimmsServer.logger;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Shared {
    public static String formatNumber(Double number) {
        if (number < 1000) {
            return String.format("%.2f", number);
        } else if (number < 1_000_000) {
            return String.format("%.2fK", number / 1000);
        } else if (number < 1_000_000_000) {
            return String.format("%.2fM", number / 1_000_000);
        } else if (number < 1_000_000_000_000L) {
            return String.format("%.2fB", number / 1_000_000_000);
        }
        return number.toString();
    }

    public static void updateResource(String resourcePath, boolean isDirectory) {
        logger.info("Updating resource: " + resourcePath);
        File embedFolder = new File(GrimmsServer.instance.getDataFolder(), resourcePath);
        deleteDirectoryRecursively(embedFolder);
        saveResourceIfNotExists(resourcePath, isDirectory);
    }

    public static void deleteDirectoryRecursively(File directory) {
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

    public static void saveResourceIfNotExists(String resourcePath, boolean isDirectory) {
        File targetFile = new File(GrimmsServer.instance.getDataFolder(), resourcePath);
        if (!targetFile.exists()) {
            if (isDirectory) {
                targetFile.mkdirs();
                try {
                    String jarPath = GrimmsServer.instance.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
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
                    logger.warning("Error: " + e.getMessage());
                }
            } else {
                try (InputStream in = GrimmsServer.instance.getClass().getClassLoader().getResourceAsStream(resourcePath);
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

    public static Player getClosestPlayer(org.bukkit.Location location, Player[] players) {
        Player closestPlayer = null;
        double closestDistance = Double.MAX_VALUE;

        for (Player player : players) {
            double distance = player.getLocation().distance(location);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestPlayer = player;
            }
        }
        return closestPlayer;
    }
}
