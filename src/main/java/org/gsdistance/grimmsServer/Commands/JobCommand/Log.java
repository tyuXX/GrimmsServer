package org.gsdistance.grimmsServer.Commands.JobCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.JobTitle;
import org.gsdistance.grimmsServer.Data.JobTitlesBaseValues;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class Log {
    public static boolean subCommand(Player player, String[] args){
        PlayerStats playerStats = PlayerStats.getPlayerStats(player);
        if (args.length > 0) {
            JobTitle jobTitle = JobTitlesBaseValues.jobTitleBaseValues.getOrDefault(args[0], null);
            if (jobTitle == null) {
                player.sendMessage("Job doesn't exist.");
                return false;
            }
            player.sendMessage("__Job:");
            player.sendMessage("|" + jobTitle.jobName() + " with paycheck " + jobTitle.paycheckSize() + " and intelligence requirement " + jobTitle.intelligenceRequirement());
            player.sendMessage("|" + jobTitle.jobName() + "[" + args[0] + "]: " + jobTitle.jobDescription());
            player.sendMessage("|Available for you: " + (((int) playerStats.getStat("intelligence") > jobTitle.intelligenceRequirement()) && jobTitle.additionalRequirement().apply(player)));
            return true;
        }
        player.sendMessage("__Jobs:");
        for (String job : JobTitlesBaseValues.jobTitleBaseValues.keySet()) {
            JobTitle jobTitle = JobTitlesBaseValues.jobTitleBaseValues.get(job);
            player.sendMessage("|" + jobTitle.jobName() + " with paycheck " + jobTitle.paycheckSize() + " and intelligence requirement " + jobTitle.intelligenceRequirement());
            player.sendMessage("|" + jobTitle.jobName() + "[" + job + "]: " + jobTitle.jobDescription());
            player.sendMessage("|Available for you: " + (((int) playerStats.getStat("intelligence") > jobTitle.intelligenceRequirement()) && jobTitle.additionalRequirement().apply(player)));
        }
        return true;
    }
}
