package org.gsdistance.grimmsServer.Config;

import java.util.ArrayList;

public enum ConfigKey {
    MODULE_JOBS("module_Jobs", true),
    MODULE_FACTIONS("module_Factions", true),
    MODULE_MARKET("module_Market", true),
    MODULE_LEADERBOARD("module_Leaderboard", true),
    DISABLED_COMMANDS("disabledCommands", new ArrayList()),
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
    DISABLED_DIMENSIONS("disabledDimensions", new ArrayList()),
    BANNED_WORDS("bannedWords", new ArrayList()),
    FORCE_DISABLE_BC("forceDisableBroadcasts", false),
    FORCE_AUTH("forceAuth", false),
    MARKET_GUI_ENABLED("market_gui_enabled", true),
    MARKET_RIPOFF_MULTIPLIER("market_ripoff_multiplier", 10.0),
    MARKET_MIN_PRICE("market_min_price", 0.25),
    MARKET_PRICE_REFRESH_INTERVAL("market_price_refresh_interval", 300),
    LOG_LEVEL("log_level", "Info");

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
