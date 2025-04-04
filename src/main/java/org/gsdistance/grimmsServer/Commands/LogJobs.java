package org.gsdistance.grimmsServer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.JobTitle;
import org.gsdistance.grimmsServer.Data.JobTitlesBaseValues;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class LogJobs implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                JobTitle jobTitle = JobTitlesBaseValues.jobTitleBaseValues.getOrDefault(args[0], null);
                if (jobTitle == null) {
                    sender.sendMessage("Job doesn't exist.");
                    return false;
                }
                sender.sendMessage("__Job:");
                sender.sendMessage("|" + jobTitle.jobName + " with paycheck " + jobTitle.paycheckSize + " and intelligence requirement " + jobTitle.intelligenceRequirement);
                sender.sendMessage("|" + jobTitle.jobName + "[" + args[0] + "]: " + jobTitle.jobDescription);
                sender.sendMessage("|Available for you: " + (((int) PlayerStats.getPlayerStats(((Player) sender)).getStat("intelligence") > jobTitle.intelligenceRequirement) && jobTitle.additionalRequirement.apply((Player) sender)));
                return true;
            }
            sender.sendMessage("__Jobs:");
            for (String job : JobTitlesBaseValues.jobTitleBaseValues.keySet()) {
                JobTitle jobTitle = JobTitlesBaseValues.jobTitleBaseValues.get(job);
                sender.sendMessage("|" + jobTitle.jobName + " with paycheck " + jobTitle.paycheckSize + " and intelligence requirement " + jobTitle.intelligenceRequirement);
                sender.sendMessage("|" + jobTitle.jobName + "[" + job + "]: " + jobTitle.jobDescription);
                sender.sendMessage("|Available for you: " + (((int) PlayerStats.getPlayerStats(((Player) sender)).getStat("intelligence") > jobTitle.intelligenceRequirement) && jobTitle.additionalRequirement.apply((Player) sender)));
            }
            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}
