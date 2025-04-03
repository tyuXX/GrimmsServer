package org.gsdistance.grimmsServer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.JobTitle;
import org.gsdistance.grimmsServer.Data.JobTitlesBaseValues;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class TakeJob implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if(args.length == 0){
                sender.sendMessage("Usage: /takeJob <jobId>");
                return false;
            }
            JobTitle jobTitle = JobTitlesBaseValues.jobTitleBaseValues.get(args[0]);
            if(jobTitle == null || jobTitle == JobTitlesBaseValues.jobTitleBaseValues.get("")){
                sender.sendMessage("Job doesn't exist.");
                return false;
            }
            PlayerStats playerStats = PlayerStats.getPlayerStats(player);
            if((int)playerStats.getStat("intelligence") < jobTitle.intelligenceRequirement){
                sender.sendMessage("Not smart enough!");
                return false;
            }
            if(!jobTitle.additionalRequirement.apply(player)){
                sender.sendMessage("Secondary requirement not met!");
                return false;
            }
            playerStats.setStat("jobTitle", args[0]);
            sender.sendMessage("Job taken: " + jobTitle.jobName);
            sender.sendMessage("Paycheck: " + jobTitle.paycheckSize + "/Description: " + jobTitle.jobDescription);
            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}
