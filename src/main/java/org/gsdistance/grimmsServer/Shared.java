package org.gsdistance.grimmsServer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Config.ConfigKey;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.List;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class Shared {
    public Shared() {
    }

    public static void Broadcast(String message, @Nullable String prefix) {
        if (Boolean.TRUE.equals(ActiveConfig.getConfigValue(ConfigKey.FORCE_DISABLE_BC, Boolean.class))) {
            GrimmsServer.logger.info("Broadcast is disabled by configuration.");
        } else {
            String formattedPrefix = prefix == null ? "" : ChatColor.translateAlternateColorCodes('&', prefix);
            String formattedMessage = ChatColor.translateAlternateColorCodes('&', message);
            GrimmsServer.instance.getServer().broadcastMessage(ChatColor.GOLD + "[" + ChatColor.YELLOW + GrimmsServer.instance.getDescription().getPrefix() + ChatColor.GOLD + "-BC]: " + ChatColor.WHITE + formattedPrefix + formattedMessage);
        }
    }

    public static NamespacedKey getNamespacedKey(String key) {
        return new NamespacedKey(GrimmsServer.instance, key);
    }

    public static String formatNumber(Double number) {
        if (number < (double) 1000.0F) {
            return String.format("%.2f", number);
        } else if (number < (double) 1000000.0F) {
            return String.format("%.2fK", number / (double) 1000.0F);
        } else if (number < (double) 1.0E9F) {
            return String.format("%.2fM", number / (double) 1000000.0F);
        } else {
            return number < 1.0E12 ? String.format("%.2fB", number / (double) 1.0E9F) : number.toString();
        }
    }

    public static void updateResource(String resourcePath, boolean isDirectory) {
        GrimmsServer.logger.info("Updating resource: " + resourcePath);
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
                        jarFile.stream().filter((entry) -> entry.getName().startsWith(resourcePath) && !entry.isDirectory()).forEach((entry) -> {
                            String relativePath = entry.getName().substring(resourcePath.length() + 1);
                            saveResourceIfNotExists(resourcePath + "/" + relativePath, false);
                        });
                    }
                } catch (IOException e) {
                    GrimmsServer.logger.warning("Failed to copy directory: " + resourcePath);
                    GrimmsServer.logger.warning("Error: " + e.getMessage());
                }
            } else {
                try (InputStream in = GrimmsServer.instance.getClass().getClassLoader().getResourceAsStream(resourcePath)) {
                    if (in != null) {
                        targetFile.getParentFile().mkdirs();
                        try (OutputStream out = new FileOutputStream(targetFile)) {
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = in.read(buffer)) > 0) {
                                out.write(buffer, 0, length);
                            }
                        }
                    } else {
                        GrimmsServer.logger.warning("Resource not found: " + resourcePath);
                    }
                } catch (IOException e) {
                    GrimmsServer.logger.log(Level.WARNING, "Failed to copy file: " + resourcePath, e);
                }
            }
        }

    }

    public static Player getClosestPlayer(Location location, List<Player> players) {
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
