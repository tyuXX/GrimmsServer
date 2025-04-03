package org.gsdistance.grimmsServer.Constructable;

import org.bukkit.entity.Player;

import java.util.function.Function;

public class JobTitle {
    public final int intelligenceRequirement;
    public final double paycheckSize;
    public final String jobDescription;
    public final String jobName;
    public final Function<Player,Boolean> additionalRequirement;
    public JobTitle(String jobName, String jobDescription, int intelligenceRequirement, double paycheckSize, Function<Player,Boolean> additionalRequirement){
        this.jobName = jobName;
        this.jobDescription = jobDescription;
        this.intelligenceRequirement = intelligenceRequirement;
        this.paycheckSize = paycheckSize;
        this.additionalRequirement = additionalRequirement;
    }
}
