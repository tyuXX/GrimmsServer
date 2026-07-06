package org.gsdistance.grimmsServer.Data;

import java.util.Map;
import org.gsdistance.grimmsServer.Constructable.JobTitle;
import org.gsdistance.grimmsServer.Stats.PlayerTitles;

public class JobTitlesBaseValues {
   public static final Map<String, JobTitle> jobTitleBaseValues = Map.ofEntries(Map.entry("dictator", new JobTitle("Dictator", "Dictator of a dictatorship. Requires title 'Dictator'", 95, (double)50000.0F, (player) -> PlayerTitles.getPlayerTitles(player).hasTitle("Dictator"))), Map.entry("executioner", new JobTitle("Executioner", "Executioner, self explanatory. Requires title 'Executioner'", 65, (double)8500.0F, (player) -> PlayerTitles.getPlayerTitles(player).hasTitle("Executioner"))), Map.entry("", new JobTitle("None", "No Job", -1, (double)0.0F, (player) -> true)), Map.entry("miner", new JobTitle("Miner", "Minin' for a quick buck, requires title 'Miner'", -1, (double)750.0F, (player) -> PlayerTitles.getPlayerTitles(player).hasTitle("Miner"))), Map.entry("farmer", new JobTitle("Farmer", "Farming, but inefficiently.", 60, (double)650.0F, (player) -> true)), Map.entry("hitman", new JobTitle("Hitman", "Killing as a job? Right away! Requires title 'Hitman'", 75, (double)17500.0F, (player) -> PlayerTitles.getPlayerTitles(player).hasTitle("Hitman"))), Map.entry("ceo", new JobTitle("CEO", "Makin money while laying back, the best life! Requires title 'Millionaire'", 80, (double)11500.0F, (player) -> PlayerTitles.getPlayerTitles(player).hasTitle("Millionaire"))));

   public JobTitlesBaseValues() {
   }
}
