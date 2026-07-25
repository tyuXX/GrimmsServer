package org.gsdistance.grimmsServer.Constructable.Entity;

import org.bukkit.entity.LivingEntity;

import java.util.function.Function;

public record EntityAffix(String id,int cooldown, Function<LivingEntity, Void> effect) {
}
