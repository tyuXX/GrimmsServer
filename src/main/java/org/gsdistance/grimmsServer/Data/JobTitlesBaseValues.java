package org.gsdistance.grimmsServer.Data;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.JobTitle;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.PlayerTitles;

import java.util.Map;
import java.util.function.Function;

public class JobTitlesBaseValues {
    public static final Map<String, JobTitle> jobTitleBaseValues = Map.ofEntries(
            Map.entry("dictator", new JobTitle("Dictator", "Dictator of a dictatorship. Requires title 'Dictator'", 95, 50000.0, (Player player) -> PlayerTitles.getPlayerTitles(player).hasTitle("Dictator"))),
            Map.entry("executioner", new JobTitle("Executioner", "Executioner, self explanatory. Requires title 'Executioner'", 65, 8500.0, (Player player) -> PlayerTitles.getPlayerTitles(player).hasTitle("Executioner"))),
            Map.entry("", new JobTitle("None", "No Job", -1, 0.0, (Player player) -> true)),
            Map.entry("miner", new JobTitle("Miner", "Minin' for a quick buck, requires title 'Miner'", -1, 750.0, (Player player) -> PlayerTitles.getPlayerTitles(player).hasTitle("Miner"))),
            Map.entry("hitman", new JobTitle("Hitman", "Killing as a job? Right away! Requires title 'Hitman'", -1, 17500.0, (Player player) -> PlayerTitles.getPlayerTitles(player).hasTitle("Hitman")))
    );
}
