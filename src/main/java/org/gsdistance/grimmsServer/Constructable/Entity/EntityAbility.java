package org.gsdistance.grimmsServer.Constructable.Entity;

import org.bukkit.entity.LivingEntity;

import java.util.function.Function;

public record EntityAbility(String id, int cooldownTicks, Function<LivingEntity, Void> effect) {
}
