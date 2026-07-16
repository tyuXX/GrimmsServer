package org.gsdistance.grimmsServer.Config;

import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.ArrayList;

public enum ConfigKey {
    CONFIG_VERSION("config_version", GrimmsServer.instance.getDescription().getVersion()),
    MODULE_JOBS("module_Jobs", true),
    MODULE_FACTIONS("module_Factions", true),
    MODULE_MARKET("module_Market", true),
    MODULE_LEADERBOARD("module_Leaderboard", true),
    DISABLED_COMMANDS("disabledCommands", new ArrayList<String>()),
    MODULE_CHAT("module_Chat", true),
    MODULE_HOMES("module_Homes", true),
    MODULE_LEVELING("module_Leveling", true),
    MODULE_TITLES("module_Titles", true),
    MODULE_RELICS("module_Relics", true),
    MODULE_EVENTS("module_Events", true),
    MODULE_UTILS("module_Utils", true),
    MODULE_RANKS("module_Ranks", true),
    JOIN_MESSAGE("joinMessage", true),
    MODULE_DIMENSIONS("module_Dimensions", true),
    DISABLED_DIMENSIONS("disabledDimensions", new ArrayList<String>()),
    BANNED_WORDS("bannedWords", new ArrayList<String>()),
    FORCE_DISABLE_BC("forceDisableBroadcasts", false),
    FORCE_AUTH("forceAuth", false),
    MARKET_GUI_ENABLED("market_gui_enabled", true),
    MARKET_RIPOFF_MULTIPLIER("market_ripoff_multiplier", 10.0),
    MARKET_MIN_PRICE("market_min_price", 0.25),
    MARKET_PRICE_REFRESH_INTERVAL("market_price_refresh_interval", 300),
    LOG_LEVEL("log_level", "Info"),
    PRESTIGE_CAPABILITY_AUTOSELL_PRICE("prestige_capability_autosell_price", 4),
    PRESTIGE_CAPABILITY_MAGNET_PRICE("prestige_capability_magnet_price", 2),
    PRESTIGE_CAPABILITY_VEINMINER_PRICE("prestige_capability_veinminer_price", 3),
    PRESTIGE_CAPABILITY_SATURATION_PERK_PRICE("prestige_capability_saturation_perk_price", 10);

    private final String key;
    private final Object defaultValue;

    ConfigKey(String key, Object defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return this.key;
    }

    public Object getDefaultValue() {
        return this.defaultValue;
    }

    // $FF: synthetic method
    private static ConfigKey[] $values() {
        return new ConfigKey[]{MODULE_JOBS, MODULE_FACTIONS, MODULE_MARKET, MODULE_LEADERBOARD, DISABLED_COMMANDS, MODULE_CHAT, MODULE_HOMES, MODULE_LEVELING, MODULE_TITLES, MODULE_RELICS, MODULE_EVENTS, MODULE_UTILS, MODULE_RANKS, JOIN_MESSAGE, MODULE_DIMENSIONS, DISABLED_DIMENSIONS, BANNED_WORDS, FORCE_DISABLE_BC, FORCE_AUTH, MARKET_GUI_ENABLED, MARKET_RIPOFF_MULTIPLIER, MARKET_MIN_PRICE, MARKET_PRICE_REFRESH_INTERVAL, LOG_LEVEL};
    }
}
