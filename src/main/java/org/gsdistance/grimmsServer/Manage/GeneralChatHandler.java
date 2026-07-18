package org.gsdistance.grimmsServer.Manage;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Config.ConfigKey;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.PlayerTitles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class GeneralChatHandler {
    public GeneralChatHandler() {
    }

    public static void sendArray(CommandSender sender, String[] array) {
        String[] formattedArray = GeneralTextFormatter.formatArray(array);
        sender.sendMessage(formattedArray);
    }

    public static boolean handleCommand(String message, Player player) {
        String command = message.split(" ")[0].substring(1);
        if (!CommandRegistry.CanExecute(command, player)) {
            GrimmsServer.logger.log(Level.INFO, "Command interrupted: '" + message + "' by player: " + player.getName() + ". Command is disabled in the config.");
            player.sendMessage("§cThis command is disabled in the server configuration.");
            return true;
        } else {
            return false;
        }
    }

    public static String handleMessage(String message, Player player) {
        PlayerMetadata metadata = PlayerMetadata.getPlayerMetadata(player);
        PlayerStats playerStats = PlayerStats.getPlayerStats(player);
        String nickname = metadata != null ? metadata.nickname : player.getName();

        // Optimized: Don't fetch metadata twice
        if (metadata != null && PlayerTitles.titles.get(metadata.decoTitle) != null) {
            nickname = "(" + metadata.decoTitle + ") " + nickname;
        }

        int prestige = playerStats.getStat("prestige", Integer.class);

        if (metadata != null && metadata.showPrestigeDeco && prestige > 0) {
            nickname = ChatColor.DARK_PURPLE + "[" + prestige + "] " + ChatColor.RESET + nickname;
        }

        if (metadata != null) {
            Faction faction = Faction.getFaction(metadata.factionUUID);
            if (faction != null) {
                nickname = "[" + faction.name + "] " + nickname;
            }
        }

        // 1. RUN THE FILTER FIRST before doing heavy string formatting
        @SuppressWarnings("unchecked")
        List<String> bannedWords = ActiveConfig.getConfigValue(ConfigKey.BANNED_WORDS, List.class);
        if (bannedWords != null) {
            String regex = "[^\\p{L}\\p{N}]+"; // Safely keeps all letters and numbers across languages
            String normalizedMessage = message.replaceAll(regex, "").toLowerCase();

            for (String word : bannedWords) {
                String normalizedBannedWord = word.replaceAll(regex, "").toLowerCase();
                if (normalizedMessage.contains(normalizedBannedWord)) {
                    GrimmsServer.logger.info("Message interrupted: '" + message + "' by player: " + player.getName() + ". Contains banned word: " + word);
                    return String.format("%s: %s", nickname, "§c[REDACTED]");
                }
            }
        }

        // 2. NOW formatting happens only if the message is safe
        LocalDateTime now = LocalDateTime.now(); // Instantiated once instead of 3 separate times

        return String.format("%s: %s", nickname, message)
                .replace("&timeF", now.toString())
                .replace("&world", player.getWorld().getName())
                .replace("&pos", player.getLocation().toString()) // Consider formatting this cleanly later
                .replace("&player", player.getName())
                .replace("&uuid", player.getUniqueId().toString())
                .replace("&date", now.toLocalDate().toString())
                .replace("&time", now.toLocalTime().toString())
                .replace("&nickname", nickname)
                .replace("&day", String.valueOf(player.getWorld().getTime() / 24000L));
    }

    public static void joinMessage(Player player) {
        if (!Boolean.FALSE.equals(ActiveConfig.getConfigValue(ConfigKey.JOIN_MESSAGE, Boolean.class))) {
            PlayerMetadata metadata = PlayerMetadata.getPlayerMetadata(player);
            PlayerStats playerStats = PlayerStats.getPlayerStats(player);
            ArrayList<String> message = new ArrayList<>();
            if (metadata.firstJoin) {
                message.add("Welcome to the server, " + player.getName() + ".");
                message.add("This server is running on GMSv" + GrimmsServer.instance.getDescription().getVersion() + ".");
                message.add("Use /gLog commands to see a list of available commands.");
            } else {
                message.add("Welcome back, " + metadata.nickname + ".");
                message.add("You have gained " + Shared.formatNumber(metadata.offlineMoney) + " money while you were offline.");
                message.add("Your current balance is " + Shared.formatNumber(playerStats.getStat("money", Double.class)) + ".");
                message.add("You have no mail.");
            }

            String var10001 = player.getWorld().getName();
            message.add("You have joined the server in world " + var10001 + " at " + player.getLocation().getBlockX() + "/" + player.getLocation().getBlockZ() + ".");
            sendArray(player, message.toArray(new String[0]));
            player.sendMessage("");
        }
    }

    public static void authMessage(Player player) {
        ArrayList<String> message = new ArrayList<>();
        if (!GrimmsServer.instance.getServer().isEnforcingSecureProfiles()) {
            PlayerStats playerStats = PlayerStats.getPlayerStats(player);
            if (playerStats.getStat("pass", String.class).isEmpty()) {
                message.add("To secure your name please:");
                message.add("Register using /gAuth register <password>");
            } else {
                message.add("Login using /gAuth login <password> to use commands");
                message.add("You will be kicked if you don't login");
            }
        }

        sendArray(player, message.toArray(new String[0]));
    }
}
