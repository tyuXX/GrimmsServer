package org.gsdistance.grimmsServer.Constructable;

import org.bukkit.entity.Player;

import java.util.function.Function;

public record JobTitle(String jobName, String jobDescription, int intelligenceRequirement, double paycheckSize,
                       Function<Player, Boolean> additionalRequirement) {
}
