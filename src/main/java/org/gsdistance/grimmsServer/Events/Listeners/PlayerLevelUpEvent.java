package org.gsdistance.grimmsServer.Events.Listeners;

import org.gsdistance.grimmsServer.Constructable.Player.PlayerLevelHandler;
import org.gsdistance.grimmsServer.Shared;

public class PlayerLevelUpEvent {
    public static void Event(org.gsdistance.grimmsServer.Events.Registers.PlayerLevelUpEvent event) {
        PlayerLevelHandler playerLevelHandler = PlayerLevelHandler.getLevelHandler(event.getPlayer());
        Shared.Broadcast("[" + event.getPlayer().getDisplayName() + "] " + "Has leveled up to " + playerLevelHandler.getLevel() + "!", null);
        event.getPlayer().sendMessage("By leveling up " + event.getLvlUps() + " times, you have gained " + Shared.formatNumber(Math.floor(event.getTMoney())) + " money!");
    }
}
