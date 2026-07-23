package org.gsdistance.grimmsServer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.Commands.GUtilCommand.SettingGUIListener;
import org.gsdistance.grimmsServer.Commands.MarketComand.MarketGUIListener;
import org.gsdistance.grimmsServer.Config.ConfigLoader;
import org.gsdistance.grimmsServer.Constructable.Item.CustomItemRegistry;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.Data.Player.PlayerTitleChecker;
import org.gsdistance.grimmsServer.Data.PlayerLoginLogManager;
import org.gsdistance.grimmsServer.Data.PluginDataStorage;
import org.gsdistance.grimmsServer.Events.Listeners.ServerStartupEvent;
import org.gsdistance.grimmsServer.Manage.EventRegistry;
import org.gsdistance.grimmsServer.Stats.HistoricalStatsManager;
import org.gsdistance.grimmsServer.Stats.PlayerStatLeaderBoard;
import org.gsdistance.grimmsServer.Stats.PlayerTitles;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class GrimmsServer extends JavaPlugin {
    public static JavaPlugin instance;
    public static Logger logger;
    public static PluginDataStorage pds;
    public static List<Function<JavaPlugin, String>> hotReloadFuncs;
    public static HistoricalStatsManager historicalStatsManager;

    public GrimmsServer() {
    }

    public void onEnable() {
        instance = this;
        logger = this.getLogger();
        pds = new PluginDataStorage(this);
        this.migrateData();
        historicalStatsManager = new HistoricalStatsManager(this);
        ConfigLoader.initialize(this);
        ConfigLoader.loadConfigFromFile();
        this.copyResourceFiles();
        this.getServer().getPluginManager().registerEvents(new EventRegistry(), this);
        this.getServer().getPluginManager().registerEvents(new MarketGUIListener(), this);
        this.getServer().getPluginManager().registerEvents(new SettingGUIListener(), this);
        ServerStartupEvent.Event();
        historicalStatsManager.start();
        PlayerLoginLogManager.initialize();
        PlayerTitles.populateDynamicTitles();
        PlayerTitleChecker.startPeriodicChecking();
        this.registerCustomItems();
    }

    public void onDisable() {
        historicalStatsManager.stop();
        PlayerTitleChecker.stopPeriodicChecking();
        super.onDisable();
    }

    public String[] fullHotReload() {
        String[] rt = new String[hotReloadFuncs.size()];

        for (int i = 0; i < hotReloadFuncs.size(); ++i) {
            rt[i] = (String) ((Function) hotReloadFuncs.get(i)).apply(instance);
        }

        return rt;
    }

    public void registerCustomItems(){
        CustomItemRegistry.registerGunTypes();
        CustomItemRegistry.registerAmmoTypes();
    }

    public void copyResourceFiles() {
        String dataFolderPath = String.valueOf(this.getDataFolder());
        File versionEmbedFile = new File(dataFolderPath + File.separatorChar + "embed" + File.separatorChar + "version.embed");
        if (versionEmbedFile.exists()) {
            boolean needsUpdate = false;

            try (InputStream jarStream = this.getClass().getResourceAsStream("/embed/version.embed")) {
                if (jarStream != null) {
                    try (InputStream fileStream = new FileInputStream(versionEmbedFile)) {
                        byte[] jarBytes = jarStream.readAllBytes();
                        byte[] fileBytes = fileStream.readAllBytes();
                        if (!Arrays.equals(jarBytes, fileBytes)) {
                            needsUpdate = true;
                        }
                    }
                }
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error comparing version.embed files", e);
            }

            if (needsUpdate) {
                Shared.updateResource("embed", true);
                Shared.updateResource("worldConstructors", true);
                logger.log(Level.INFO, "Updated resources from jar.");
            }
        } else {
            Shared.saveResourceIfNotExists("embed", true);
        }

    }

    private void migrateData() {

    }
}
