package org.gsdistance.grimmsServer;

import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.Events.EventTrigger;
import org.gsdistance.grimmsServer.Events.ServerStartupEvent;

import java.util.logging.Logger;

public final class GrimmsServer extends JavaPlugin {
    public static JavaPlugin instance;
    public static Logger logger;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        logger = this.getLogger();
        getServer().getPluginManager().registerEvents(new EventTrigger(), this);
        ServerStartupEvent.Event();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
