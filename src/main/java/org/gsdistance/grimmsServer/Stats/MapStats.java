package org.gsdistance.grimmsServer.Stats;

import org.bukkit.World;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Dictionary;
import java.util.Hashtable;

public class MapStats {
    public static final Dictionary<String, PersistentDataType<?, ?>> Stats = new Hashtable<>();
    static {
        Stats.put("death_count", PersistentDataType.INTEGER);
        Stats.put("join_count", PersistentDataType.INTEGER);
    }
    public static final Dictionary<String,String> StatNames = new Hashtable<>();
    static {
        StatNames.put("death_count", "Death Count");
        StatNames.put("join_count", "Join Count");
    }

}
