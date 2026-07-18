package org.gsdistance.grimmsServer.Data;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Config.ConfigKey;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PlayerLoginLogManager {
    private static final String LOG_FOLDER = "logs";
    private static final String LOG_FILE_PREFIX = "login-";
    private static final String LOG_FILE_SUFFIX = ".log";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat FILE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    private static String currentLogFile;
    private static boolean enabled = false;

    public static void initialize() {
        enabled = Boolean.TRUE.equals(ActiveConfig.getConfigValue(ConfigKey.LOGIN_LOGGER_ENABLED, Boolean.class));
        if (enabled) {
            // Create logs folder if it doesn't exist
            File logFolder = new File(GrimmsServer.instance.getDataFolder(), LOG_FOLDER);
            if (!logFolder.exists()) {
                logFolder.mkdirs();
            }

            String timestamp = FILE_DATE_FORMAT.format(new Date());
            currentLogFile = LOG_FOLDER + File.separator + LOG_FILE_PREFIX + timestamp + LOG_FILE_SUFFIX;
            GrimmsServer.logger.info("Login log initialized: " + currentLogFile);
        } else {
            GrimmsServer.logger.info("Login logger is disabled in config.");
        }
    }

    public static void logPlayerLogin(Player player, String loginType) {
        if (!enabled) {
            return;
        }

        if (currentLogFile == null) {
            initialize();
        }

        String ipAddress = player.getAddress() != null ? player.getAddress().getAddress().getHostAddress() : "unknown";
        long timestamp = System.currentTimeMillis();
        String formattedTime = DATE_FORMAT.format(new Date(timestamp));

        String logEntry = String.format("[%s] %s (%s) - IP: %s - Type: %s - UUID: %s%n",
                formattedTime,
                player.getName(),
                player.getUniqueId(),
                ipAddress,
                loginType,
                player.getUniqueId()
        );

        File logFile = new File(GrimmsServer.instance.getDataFolder(), currentLogFile);
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }

            try (FileWriter writer = new FileWriter(logFile, true)) {
                writer.write(logEntry);
            }

            GrimmsServer.logger.info("Logged login: " + player.getName() + " (" + ipAddress + ") - Type: " + loginType);
        } catch (IOException e) {
            GrimmsServer.logger.warning("Failed to write to login log file: " + e.getMessage());
        }
    }

    public static List<String> getAllLoginLogs() {
        return getLogsFromFile(currentLogFile);
    }

    public static List<String> getAllLoginLogsFromAllFiles() {
        File logFolder = new File(GrimmsServer.instance.getDataFolder(), LOG_FOLDER);
        File[] logFiles = logFolder.listFiles((dir, name) ->
                name.startsWith(LOG_FILE_PREFIX) && name.endsWith(LOG_FILE_SUFFIX));

        List<String> allLogs = new ArrayList<>();
        if (logFiles != null) {
            for (File logFile : logFiles) {
                allLogs.addAll(getLogsFromFile(LOG_FOLDER + File.separator + logFile.getName()));
            }
        }
        return allLogs;
    }

    private static List<String> getLogsFromFile(String fileName) {
        File logFile = new File(GrimmsServer.instance.getDataFolder(), fileName);
        List<String> logs = new ArrayList<>();

        if (!logFile.exists()) {
            return logs;
        }

        try (Scanner scanner = new Scanner(logFile)) {
            while (scanner.hasNextLine()) {
                logs.add(scanner.nextLine());
            }
        } catch (IOException e) {
            GrimmsServer.logger.warning("Failed to read login log file: " + e.getMessage());
        }

        return logs;
    }

    public static List<String> getPlayerLoginLogs(UUID playerUuid) {
        List<String> allLogs = getAllLoginLogsFromAllFiles();
        List<String> playerLogs = new ArrayList<>();

        for (String log : allLogs) {
            if (log.contains(playerUuid.toString())) {
                playerLogs.add(log);
            }
        }

        return playerLogs;
    }

    public static List<String> getLoginLogsByIp(String ipAddress) {
        List<String> allLogs = getAllLoginLogsFromAllFiles();
        List<String> ipLogs = new ArrayList<>();

        for (String log : allLogs) {
            if (log.contains("IP: " + ipAddress)) {
                ipLogs.add(log);
            }
        }

        return ipLogs;
    }

    public static List<String> getLoginLogsByType(String loginType) {
        List<String> allLogs = getAllLoginLogsFromAllFiles();
        List<String> typeLogs = new ArrayList<>();

        for (String log : allLogs) {
            if (log.contains("Type: " + loginType)) {
                typeLogs.add(log);
            }
        }

        return typeLogs;
    }

    public static List<String> getLoginLogsByName(String playerName) {
        List<String> allLogs = getAllLoginLogsFromAllFiles();
        List<String> nameLogs = new ArrayList<>();

        for (String log : allLogs) {
            if (log.contains(" " + playerName + " ")) {
                nameLogs.add(log);
            }
        }

        return nameLogs;
    }
}
