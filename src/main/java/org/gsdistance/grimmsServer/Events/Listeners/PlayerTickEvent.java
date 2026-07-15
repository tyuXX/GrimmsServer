package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.gsdistance.grimmsServer.Commands.GAuthCommand.GAuthBaseCommand;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.JobTitlesBaseValues;
import org.gsdistance.grimmsServer.Data.Player.PlayerCapability;
import org.gsdistance.grimmsServer.Data.Player.PlayerTitleChecker;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Stats.PlayerStatLeaderBoard;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerTickEvent {
    private static final List<Player> magnetPlayers = new ArrayList();
    private static final Map<Player, Integer> saturationPerkPlayers = new HashMap();
    private static final Map<UUID, Long> playerLoginTimes = new HashMap();
    private static final Map<UUID, Long> lastPaycheckTimes = new HashMap();
    private static final long LOGIN_KICK_TIMEOUT_MS = 60000L; // 60 seconds
    private static final long TITLE_CHECK_INTERVAL_MS = 50000L; // ~50 seconds (was 1000 ticks)
    private static final long PAYCHECK_INTERVAL_MS = 1200000L; // ~20 minutes (was 24000 ticks)

    public PlayerTickEvent() {
    }

    public static void Event(Player player) {
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        if (playerMetadata.capabilities.containsKey(PlayerCapability.MAGNET) && playerMetadata.settings.contains(PlayerCapability.MAGNET.capabilityId)) {
            magnetPlayers.add(player);
        }
        
        // Handle Saturation Perk
        if (playerMetadata.capabilities.containsKey(PlayerCapability.SATURATION_PERK) && 
            playerMetadata.settings.contains(PlayerCapability.SATURATION_PERK.capabilityId)) {
            int currentDuration = saturationPerkPlayers.getOrDefault(player, 0);
            saturationPerkPlayers.put(player, currentDuration + 1);
            
            // Apply saturation effect with the lowest level (1) for 10 seconds
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 200, 0, false, false));
        } else {
            saturationPerkPlayers.remove(player);
        }

        long currentTime = System.currentTimeMillis();
        UUID playerId = player.getUniqueId();
        
        // Track player login time for real-time kick timer
        if (!playerLoginTimes.containsKey(playerId)) {
            playerLoginTimes.put(playerId, currentTime);
        }

        long loginTime = playerLoginTimes.get(playerId);

        // Real-time login kick check
        if (!GAuthBaseCommand.isLoggedIn(player) && (currentTime - loginTime) >= LOGIN_KICK_TIMEOUT_MS) {
            player.kickPlayer("Not logged in for too long.");
            playerLoginTimes.remove(playerId);
            return;
        }

        // Title checks (was every 1000 ticks, now every ~50 seconds)
        if ((currentTime - loginTime) % TITLE_CHECK_INTERVAL_MS < 50L) {
            PlayerStatLeaderBoard.getPlayerStatLeaderBoard().checkPlayer(player);
            PlayerTitleChecker.checkForMoney(player);
            PlayerTitleChecker.checkTitles(player);
            PlayerTitleChecker.checkForBlockBreaks(player);
            PlayerTitleChecker.checkForTotalKills(player);
        }

        // Paycheck (was every 24000 ticks, now every ~20 minutes)
        long lastPaycheckTime = lastPaycheckTimes.getOrDefault(playerId, 0L);
        if ((currentTime - lastPaycheckTime) >= PAYCHECK_INTERVAL_MS) {
            PlayerStats playerStats = PlayerStats.getPlayerStats(player);
            String jobTitleId = playerStats.getStat("jobTitle", String.class);
            if (jobTitleId != null && !jobTitleId.isEmpty()) {
                double multiplier = (double) 1.0F + Math.pow((double) playerStats.getStat("level", Integer.class), 2.0F) * Math.pow(playerStats.getStat("prestige", Integer.class) + 1,2) / (double) 100.0F;
                double payCheck = Math.ceil(JobTitlesBaseValues.jobTitleBaseValues.getOrDefault(jobTitleId, null).paycheckSize() * multiplier);
                double money = playerStats.getStat("money", Double.class);
                if(playerStats.getStat("maximum_balance", Double.class) < money + payCheck){
                    playerStats.setStat("money", money + payCheck);
                }
                player.sendMessage(ChatColor.GREEN + "You have received your paycheck: " + ChatColor.GOLD + Shared.formatNumber(payCheck));
                lastPaycheckTimes.put(playerId, currentTime);
            }
        }
    }

    public static void processMagnets() {
        if (!magnetPlayers.isEmpty()) {
            for (Player magnetPlayer : magnetPlayers) {
                for (Item item : magnetPlayer.getWorld().getEntitiesByClass(Item.class)) {
                    if (item.getLocation().distance(magnetPlayer.getLocation()) <= (double) 10.0F) {
                        item.teleport(magnetPlayer.getLocation());
                        break;
                    }
                }
            }

            magnetPlayers.clear();
        }
    }

    public static void onPlayerQuit(Player player) {
        playerLoginTimes.remove(player.getUniqueId());
        lastPaycheckTimes.remove(player.getUniqueId());
    }

    public static void initializePaycheckTimer(Player player) {
        UUID playerId = player.getUniqueId();
        lastPaycheckTimes.put(playerId, System.currentTimeMillis());
    }
}
