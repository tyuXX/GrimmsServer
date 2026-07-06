package org.gsdistance.grimmsServer.Commands.JobCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.JobTitle;
import org.gsdistance.grimmsServer.Data.JobTitlesBaseValues;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class Log {

   public Log() {
   }

   @SuppressWarnings("unchecked")
   public static boolean subCommand(Player player, String[] args) {
      PlayerStats playerStats = PlayerStats.getPlayerStats(player);

      // Case 1: No sub-argument provided -> List all jobs and exit early
      if (args.length <= 1) {
         player.sendMessage("__Jobs:");

         for (String jobKey : JobTitlesBaseValues.jobTitleBaseValues.keySet()) {
            JobTitle jobTitle = (JobTitle) JobTitlesBaseValues.jobTitleBaseValues.get(jobKey);
            if (jobTitle != null) {
               sendJobDetails(player, playerStats, jobKey, jobTitle);
            }
         }
         return true;
      }

      // Case 2: Specific job argument provided
      String targetJobId = args[1];
      JobTitle jobTitle = (JobTitle) JobTitlesBaseValues.jobTitleBaseValues.get(targetJobId);

      if (jobTitle == null) {
         player.sendMessage("Job doesn't exist.");
         return false;
      }

      player.sendMessage("__Job:");
      sendJobDetails(player, playerStats, targetJobId, jobTitle);
      return true;
   }

   /**
    * DRY Principle: Extracted helper method to handle job requirement evaluations
    * and uniform formatting across lists and single queries.
    */
   private static void sendJobDetails(Player player, PlayerStats playerStats, String jobId, JobTitle jobTitle) {
      // Evaluate player prerequisites cleanly
      int currentIntelligence = playerStats.getStat("intelligence", Integer.class);
      boolean meetsAdditionalRequirements = jobTitle.additionalRequirement().apply(player);
      boolean isAvailable = currentIntelligence > jobTitle.intelligenceRequirement() && meetsAdditionalRequirements;

      // Send formatted output to the user
      player.sendMessage("|" + jobTitle.jobName() + " with paycheck " + jobTitle.paycheckSize() + " and intelligence requirement " + jobTitle.intelligenceRequirement());
      player.sendMessage("|" + jobTitle.jobName() + "[" + jobId + "]: " + jobTitle.jobDescription());
      player.sendMessage("|Available for you: " + isAvailable);
   }
}