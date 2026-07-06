package org.gsdistance.grimmsServer.Constructable.Player;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Events.Registers.PlayerLevelUpRegister;
import org.gsdistance.grimmsServer.Manage.EventRegistry;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class PlayerLevelHandler {
   private final Player player;
   private final PlayerStats playerStats;

   public PlayerLevelHandler(Player player) {
      this.playerStats = PlayerStats.getPlayerStats(player);
      this.player = player;
   }

   public static PlayerLevelHandler getLevelHandler(Player player) {
      return new PlayerLevelHandler(player);
   }

   public int getLevel() {
      return (Integer)this.playerStats.getStat("level", Integer.class);
   }

   public double getXp() {
      return (Double)this.playerStats.getStat("xp", Double.class);
   }

   public void setXp(double xp) {
      this.playerStats.setStat("xp", xp);
   }

   public double getXpToLevel() {
      return Math.pow((double)this.getLevel(), 1.7) * (double)100.0F;
   }

   public double getMoneyMultiplier() {
      return Math.max((double)1.0F, Math.floor(Math.sqrt((double)this.getLevel())));
   }

   public double getLesserMoneyMultiplier() {
      return Math.max((double)1.0F, Math.floor(Math.sqrt((double)this.getLevel() / (double)2.0F)));
   }

   public void addExp(double xp) {
      double exp = xp + this.getXp();
      int lvlups = 0;

      double tMoney;
      for(tMoney = (double)0.0F; exp > this.getXpToLevel(); ++lvlups) {
         exp -= this.getXpToLevel();
         this.playerStats.changeStat("level", 1);
         tMoney += Math.pow((double)this.getLevel(), 1.8) * (double)100.0F;
      }

      this.setXp(exp);
      this.playerStats.setStat("xp_required", this.getXpToLevel());
      if (lvlups > 0) {
         this.playerStats.setStat("money", (Double)this.playerStats.getStat("money", Double.class) + tMoney);
         EventRegistry.callEvent(new PlayerLevelUpRegister(this.player, lvlups, tMoney));
      }

   }
}
