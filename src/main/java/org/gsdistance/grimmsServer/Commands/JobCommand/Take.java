package org.gsdistance.grimmsServer.Commands.JobCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.JobTitle;
import org.gsdistance.grimmsServer.Data.JobTitlesBaseValues;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class Take {
    public static boolean subCommand(Player player, String[] args) {
        if (args.length < 2) {
            return false;
        }
        JobTitle jobTitle = JobTitlesBaseValues.jobTitleBaseValues.get(args[1]);
        if (jobTitle == null || jobTitle == JobTitlesBaseValues.jobTitleBaseValues.get("")) {
            player.sendMessage("Job doesn't exist.");
            return false;
        }
        PlayerStats playerStats = PlayerStats.getPlayerStats(player);
        if (playerStats.getStat("intelligence", Integer.class) < jobTitle.intelligenceRequirement()) {
            player.sendMessage("Not smart enough!");
            return false;
        }
        if (!jobTitle.additionalRequirement().apply(player)) {
            player.sendMessage("Secondary requirement not met!");
            return false;
        }
        playerStats.setStat("jobTitle", args[1]);
        player.sendMessage("Job taken: " + jobTitle.jobName());
        player.sendMessage("Paycheck: " + jobTitle.paycheckSize());
        player.sendMessage("Description: " + jobTitle.jobDescription());
        return true;
    }
}
