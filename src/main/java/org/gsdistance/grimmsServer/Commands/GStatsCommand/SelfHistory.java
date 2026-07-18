package org.gsdistance.grimmsServer.Commands.GStatsCommand;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Manage.GeneralChatHandler;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Stats.HistoricalStatsSnapshot;
import org.gsdistance.grimmsServer.Stats.PlayerHistoricalStats;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SelfHistory {
    public SelfHistory() {
    }

    public static boolean subCommand(Player player, String[] args) {
        if (!player.hasPermission("grimmsserver.stats.self")) {
            return false;
        }

        PlayerHistoricalStats historicalStats = PlayerHistoricalStats.getPlayerHistoricalStats(player);

        if (historicalStats.allTimeSnapshots.isEmpty()) {
            player.sendMessage(ChatColor.RED + "No historical data available yet.");
            player.sendMessage(ChatColor.YELLOW + "Data is recorded when you join, quit, and hourly while online.");
            return true;
        }

        String timeRange = "all";
        String specificStat = null;
        boolean largeGraph = false;

        if (args.length >= 2) {
            timeRange = args[1].toLowerCase();
        }
        if (args.length >= 3) {
            String arg = args[2].toLowerCase();
            if (arg.equals("largegraph")) {
                largeGraph = true;
            } else {
                specificStat = arg;
            }
        }
        if (args.length >= 4) {
            String arg = args[3].toLowerCase();
            if (arg.equals("largegraph")) {
                largeGraph = true;
            } else if (specificStat == null) {
                specificStat = arg;
            }
        }

        List<HistoricalStatsSnapshot> snapshots = filterSnapshotsByTime(historicalStats.allTimeSnapshots, timeRange);

        if (snapshots.isEmpty()) {
            player.sendMessage(ChatColor.RED + "No data available for the specified time range.");
            return true;
        }

        if (specificStat != null && PlayerStats.Stats.get(specificStat) != null) {
            displaySingleStatGraph(player, snapshots, specificStat, timeRange, largeGraph);
        } else {
            displayAllStatsGraph(player, snapshots, timeRange, largeGraph);
        }

        return true;
    }

    public static List<HistoricalStatsSnapshot> filterSnapshotsByTime(List<HistoricalStatsSnapshot> allSnapshots, String timeRange) {
        LocalDateTime now = LocalDateTime.now();
        List<HistoricalStatsSnapshot> filtered = new ArrayList<>();

        for (HistoricalStatsSnapshot snapshot : allSnapshots) {
            try {
                LocalDateTime snapshotTime = LocalDateTime.parse(snapshot.getTimestamp());
                long hoursDiff = java.time.Duration.between(snapshotTime, now).toHours();

                switch (timeRange) {
                    case "hour":
                        if (hoursDiff <= 1) filtered.add(snapshot);
                        break;
                    case "day":
                        if (hoursDiff <= 24) filtered.add(snapshot);
                        break;
                    case "week":
                        if (hoursDiff <= 168) filtered.add(snapshot);
                        break;
                    case "month":
                        if (hoursDiff <= 720) filtered.add(snapshot);
                        break;
                    case "all":
                    default:
                        filtered.add(snapshot);
                        break;
                }
            } catch (Exception e) {
                filtered.add(snapshot);
            }
        }

        return filtered;
    }

    public static void displaySingleStatGraph(Player player, List<HistoricalStatsSnapshot> snapshots, String statName, String timeRange, boolean largeGraph) {
        ArrayList<String> messages = new ArrayList<>();
        String displayName = PlayerStats.StatNames.get(statName);

        messages.add(ChatColor.GOLD + "=== " + displayName + " History (" + timeRange + ") ===");
        messages.add(ChatColor.GRAY + "Showing " + snapshots.size() + " data points");

        if (snapshots.size() < 2) {
            messages.add(ChatColor.YELLOW + "Not enough data points to display graph.");
            for (HistoricalStatsSnapshot snapshot : snapshots) {
                Object value = snapshot.getStat(statName);
                String formattedValue = formatStatValue(statName, value);
                messages.add(ChatColor.WHITE + formatTimestamp(snapshot.getTimestamp()) + ": " + ChatColor.GREEN + formattedValue);
            }
        } else {
            double[] values = new double[snapshots.size()];
            double minValue = Double.MAX_VALUE;
            double maxValue = Double.MIN_VALUE;

            for (int i = 0; i < snapshots.size(); i++) {
                Object value = snapshots.get(i).getStat(statName);
                double numValue = convertToDouble(value);
                values[i] = numValue;
                if (numValue < minValue) minValue = numValue;
                if (numValue > maxValue) maxValue = numValue;
            }

            if (maxValue == minValue) {
                maxValue += 1;
            }

            int graphHeight = largeGraph ? 15 : 6;
            int graphWidth = largeGraph ? Math.min(snapshots.size(), 60) : Math.min(snapshots.size(), 25);

            messages.add(ChatColor.GRAY + "Min: " + ChatColor.AQUA + formatStatValue(statName, minValue) +
                    ChatColor.GRAY + " | Max: " + ChatColor.AQUA + formatStatValue(statName, maxValue));

            for (int row = graphHeight; row >= 0; row--) {
                StringBuilder line = new StringBuilder();
                double threshold = minValue + (maxValue - minValue) * ((double) row / graphHeight);

                line.append(ChatColor.GRAY).append(String.format("%8s", formatStatValue(statName, threshold))).append(" ");

                for (int col = 0; col < graphWidth; col++) {
                    int snapshotIndex = (int) ((double) col / graphWidth * (snapshots.size() - 1));
                    double value = values[snapshotIndex];

                    if (value >= threshold) {
                        line.append(ChatColor.GREEN).append("█");
                    } else {
                        line.append(ChatColor.DARK_GRAY).append("░");
                    }
                }

                messages.add(line.toString());
            }

            messages.add(ChatColor.GRAY + "Time: " + formatTimestamp(snapshots.get(0).getTimestamp()) +
                    " → " + formatTimestamp(snapshots.get(snapshots.size() - 1).getTimestamp()));
        }

        messages.add(ChatColor.GOLD + "=====================================");
        GeneralChatHandler.sendArray(player, messages.toArray(new String[0]));
    }

    public static void displayAllStatsGraph(Player player, List<HistoricalStatsSnapshot> snapshots, String timeRange, boolean largeGraph) {
        ArrayList<String> messages = new ArrayList<>();

        messages.add(ChatColor.GOLD + "=== All Stats History (" + timeRange + ") ===");
        messages.add(ChatColor.GRAY + "Showing " + snapshots.size() + " data points");
        messages.add(ChatColor.YELLOW + "Use /gStats self_history " + timeRange + " <stat> for detailed graph");
        if (largeGraph) {
            messages.add(ChatColor.YELLOW + "Add 'largegraph' for larger graph display");
        }

        HistoricalStatsSnapshot first = snapshots.get(0);
        HistoricalStatsSnapshot last = snapshots.get(snapshots.size() - 1);

        messages.add("");
        messages.add(ChatColor.AQUA + "Comparison: First vs Last Snapshot");
        messages.add(ChatColor.GRAY + "First: " + formatTimestamp(first.getTimestamp()));
        messages.add(ChatColor.GRAY + "Last:  " + formatTimestamp(last.getTimestamp()));
        messages.add("");

        for (String stat : PlayerStats.StatOrder) {
            Object firstValue = first.getStat(stat);
            Object lastValue = last.getStat(stat);

            if (firstValue != null && lastValue != null) {
                String displayName = PlayerStats.StatNames.get(stat);
                double firstNum = convertToDouble(firstValue);
                double lastNum = convertToDouble(lastValue);
                double change = lastNum - firstNum;
                double percentChange = firstNum != 0 ? (change / firstNum) * 100 : 0;

                ChatColor changeColor = change > 0 ? ChatColor.GREEN : (change < 0 ? ChatColor.RED : ChatColor.GRAY);
                String changeSymbol = change > 0 ? "+" : "";

                messages.add(ChatColor.WHITE + displayName + ": " +
                        ChatColor.AQUA + formatStatValue(stat, firstValue) +
                        ChatColor.GRAY + " → " +
                        ChatColor.AQUA + formatStatValue(stat, lastValue) +
                        changeColor + " (" + changeSymbol + formatStatValue(stat, change) +
                        ChatColor.GRAY + ", " + changeSymbol + String.format("%.1f", percentChange) + "%)");
            }
        }

        messages.add(ChatColor.GOLD + "=====================================");
        GeneralChatHandler.sendArray(player, messages.toArray(new String[0]));
    }

    private static String formatTimestamp(String timestamp) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(timestamp);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
            return dateTime.format(formatter);
        } catch (Exception e) {
            return timestamp.substring(0, Math.min(16, timestamp.length()));
        }
    }

    private static String formatStatValue(String statName, Object value) {
        if (value == null) return "N/A";

        if (value instanceof Double) {
            return Shared.formatNumber((Double) value);
        } else if (value instanceof Long) {
            return Shared.formatNumber((double) ((Long) value));
        } else if (value instanceof Integer) {
            return String.valueOf(value);
        }

        return value.toString();
    }

    private static double convertToDouble(Object value) {
        if (value instanceof Double) return (Double) value;
        if (value instanceof Long) return ((Long) value).doubleValue();
        if (value instanceof Integer) return ((Integer) value).doubleValue();
        if (value instanceof Boolean) return ((Boolean) value) ? 1.0 : 0.0;
        return 0.0;
    }
}
