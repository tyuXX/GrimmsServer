package org.gsdistance.grimmsServer.Stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TitleGenerator {
    private static final Map<String, String> moneyTitles = new HashMap<>();
    private static final Map<Long, String> blockBreakTitles = new HashMap<>();
    private static final Map<Long, String> killTitles = new HashMap<>();
    
    static {
        // Money titles (powers of 10)
        moneyTitles.put("10", "Pocket Change");
        moneyTitles.put("100", "Coin Collector");
        moneyTitles.put("1000", "Wealthy");
        moneyTitles.put("10000", "Rich");
        moneyTitles.put("100000", "Affluent");
        moneyTitles.put("1000000", "Millionaire");
        moneyTitles.put("10000000", "Multi-Millionaire");
        moneyTitles.put("100000000", "Hundred-Millionaire");
        moneyTitles.put("1000000000", "Billionaire");
        moneyTitles.put("10000000000", "Multi-Billionaire");
        moneyTitles.put("100000000000", "Hundred-Billionaire");
        moneyTitles.put("1000000000000", "Trillionaire");
        moneyTitles.put("10000000000000", "Multi-Trillionaire");
        moneyTitles.put("100000000000000", "Hundred-Trillionaire");
        moneyTitles.put("1000000000000000", "Quadrillionaire");
        moneyTitles.put("10000000000000000", "Multi-Quadrillionaire");
        moneyTitles.put("100000000000000000", "Hundred-Quadrillionaire");
        moneyTitles.put("1000000000000000000", "Quintillionaire");
        
        // Block break titles (powers of 10)
        blockBreakTitles.put(10L, "Novice Miner");
        blockBreakTitles.put(100L, "Apprentice Miner");
        blockBreakTitles.put(1000L, "Miner");
        blockBreakTitles.put(10000L, "Stone Breaker");
        blockBreakTitles.put(100000L, "Master Miner");
        blockBreakTitles.put(1000000L, "Legendary Miner");
        blockBreakTitles.put(10000000L, "Earth Shatterer");
        blockBreakTitles.put(100000000L, "World Digger");
        blockBreakTitles.put(1000000000L, "Void Miner");
        blockBreakTitles.put(10000000000L, "Dimension Breaker");
        blockBreakTitles.put(100000000000L, "Reality Shatterer");
        blockBreakTitles.put(1000000000000L, "Universe Digger");
        blockBreakTitles.put(10000000000000L, "Cosmic Miner");
        blockBreakTitles.put(100000000000000L, "Galaxy Breaker");
        blockBreakTitles.put(1000000000000000L, "Universal Miner");
        
        // Kill titles (powers of 10)
        killTitles.put(10L, "Novice Hunter");
        killTitles.put(100L, "Hunter");
        killTitles.put(1000L, "Slayer");
        killTitles.put(10000L, "Executioner");
        killTitles.put(100000L, "Destroyer");
        killTitles.put(1000000L, "Godslayer");
        killTitles.put(10000000L, "World Ender");
        killTitles.put(100000000L, "Mass Murderer");
        killTitles.put(1000000000L, "Genocidal");
        killTitles.put(10000000000L, "Apocalypse Bringer");
        killTitles.put(100000000000L, "Extinction Event");
        killTitles.put(1000000000000L, "Universe Ender");
        killTitles.put(10000000000000L, "Reality Destroyer");
        killTitles.put(100000000000000L, "Void Consumer");
        killTitles.put(1000000000000000L, "Oblivion");
    }
    
    public static List<String> getMoneyTitles(double money) {
        List<String> earnedTitles = new ArrayList<>();
        // Check all money titles from lowest to highest
        for (int i = 1; i <= 18; i++) {
            String powerOf10 = "1" + "0".repeat(i - 1);
            if (moneyTitles.containsKey(powerOf10) && money >= Double.parseDouble(powerOf10)) {
                earnedTitles.add(moneyTitles.get(powerOf10));
            }
        }
        return earnedTitles;
    }
    
    public static List<String> getBlockBreakTitles(long blockBreaks) {
        List<String> earnedTitles = new ArrayList<>();
        // Check all block break titles from lowest to highest
        for (long power = 10; power <= 1000000000000000L; power *= 10) {
            if (blockBreakTitles.containsKey(power) && blockBreaks >= power) {
                earnedTitles.add(blockBreakTitles.get(power));
            }
        }
        return earnedTitles;
    }
    
    public static List<String> getKillTitles(long kills) {
        List<String> earnedTitles = new ArrayList<>();
        // Check all kill titles from lowest to highest
        for (long power = 10; power <= 1000000000000000L; power *= 10) {
            if (killTitles.containsKey(power) && kills >= power) {
                earnedTitles.add(killTitles.get(power));
            }
        }
        return earnedTitles;
    }
    
    public static Map<String, String> getAllMoneyTitles() {
        return new HashMap<>(moneyTitles);
    }
    
    public static Map<Long, String> getAllBlockBreakTitles() {
        return new HashMap<>(blockBreakTitles);
    }
    
    public static Map<Long, String> getAllKillTitles() {
        return new HashMap<>(killTitles);
    }
}
