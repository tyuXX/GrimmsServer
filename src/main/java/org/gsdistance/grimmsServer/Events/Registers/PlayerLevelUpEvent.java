package org.gsdistance.grimmsServer.Events.Registers;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerLevelUpEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final int lvlups;
    private final double tMoney;

    public PlayerLevelUpEvent(Player player, int lvlups, double tMoney) {
        this.player = player;
        this.lvlups = lvlups;
        this.tMoney = tMoney;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public int getLvlUps() {
        return lvlups;
    }

    public double getTMoney() {
        return tMoney;
    }
}
