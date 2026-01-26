package org.gsdistance.grimmsServer.Config;

import java.util.ArrayList;

public enum ConfigKey {
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
    FORCE_AUTH("forceAuth", false);

    private final String key;
    private final Object defaultValue;

    ConfigKey(String key, Object defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }
}