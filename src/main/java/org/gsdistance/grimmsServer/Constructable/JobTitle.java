package org.gsdistance.grimmsServer.Constructable;

import java.util.function.Function;
import org.bukkit.entity.Player;

public record JobTitle(String jobName, String jobDescription, int intelligenceRequirement, double paycheckSize, Function<Player, Boolean> additionalRequirement) {
}
