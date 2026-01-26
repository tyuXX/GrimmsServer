package org.gsdistance.grimmsServer.Manage;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Config.ConfigKey;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static org.gsdistance.grimmsServer.Config.ActiveConfig.getConfigValue;

public class GeneralChatHandler {
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
        }
        return false;
    }

    public static String handleMessage(String message, Player player) {
        // Retrieve player metadata
        PlayerMetadata metadata = PlayerMetadata.getPlayerMetadata(player);
        String nickname = metadata != null ? metadata.nickname : player.getName();

        // Format the chat message
        String formattedMessage = String.format(
                        "<%s>: %s",
                        nickname,
                        message
                ).replace("&timeF", LocalDateTime.now().toString())
                .replace("&world", player.getWorld().getName())
                .replace("&pos", player.getLocation().toString())
                .replace("&player", player.getName())
                .replace("&uuid", player.getUniqueId().toString())
                .replace("&date", LocalDateTime.now().toLocalDate().toString())
                .replace("&time", LocalDateTime.now().toLocalTime().toString())
                .replace("&nickname", nickname)
                .replace("&day", String.valueOf(player.getWorld().getTime() / 24000));
        @SuppressWarnings("unchecked")
        List<String> bannedWords = getConfigValue(ConfigKey.BANNED_WORDS, List.class);
        if (bannedWords != null) {
            String regex = "[ .-_,:;!?()\\[\\]{}\"'`~@#$%^&*+=|<>/\\\\]+";
            String normalizedMessage = message.replaceAll(regex, "").toLowerCase();
            for (String word : bannedWords) {
                if (normalizedMessage.contains(word.replaceAll(regex, "").toLowerCase())) {
                    GrimmsServer.logger.info("Message interrupted: '" + message + "' by player: " + player.getName() + ". Contains banned word: " + word);
                    return String.format("<%s>: %s", nickname, "§c[REDACTED]");
                }
            }
        }
        return formattedMessage;
    }

    public static void joinMessage(Player player) {
        if (Boolean.FALSE.equals(getConfigValue(ConfigKey.JOIN_MESSAGE, Boolean.class))) {
            return;
        }
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
        message.add("You have joined the server in world " + player.getWorld().getName() + " at " + player.getLocation().getBlockX() + "/" + player.getLocation().getBlockZ() + ".");
        sendArray(player, message.toArray(new String[0]));
        player.sendMessage("");
    }

    public static void authMessage(Player player){
        ArrayList<String> message = new ArrayList<>();
        // Check if server is in offline mode
        if(!GrimmsServer.instance.getServer().isEnforcingSecureProfiles()){
            // Check if they have registered
            PlayerStats playerStats = PlayerStats.getPlayerStats(player);
            if(playerStats.getStat("pass", String.class).isEmpty()){
                message.add("To secure your name please:");
                message.add("Register using /gAuth register <password>");
            }
            else{
                message.add("Login using /gAuth login <password> to use commands");
                message.add("You will be kicked if you don't login");
            }
        }
        sendArray(player, message.toArray(new String[0]));
    }
}
