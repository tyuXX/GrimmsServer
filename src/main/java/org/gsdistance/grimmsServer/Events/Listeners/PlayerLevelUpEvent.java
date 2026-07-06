package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerLevelHandler;
import org.gsdistance.grimmsServer.Events.Registers.PlayerLevelUpRegister;

public class PlayerLevelUpEvent {
   public PlayerLevelUpEvent() {
   }

   public static void Event(PlayerLevelUpRegister event) {
      PlayerLevelHandler playerLevelHandler = PlayerLevelHandler.getLevelHandler(event.getPlayer());
      Shared.Broadcast("[" + event.getPlayer().getDisplayName() + "] Has leveled up to " + playerLevelHandler.getLevel() + "!", (String)null);
      Player var10000 = event.getPlayer();
      int var10001 = event.getLvlUps();
      var10000.sendMessage("By leveling up " + var10001 + " times, you have gained " + Shared.formatNumber(Math.floor(event.getTMoney())) + " money!");
   }
}
