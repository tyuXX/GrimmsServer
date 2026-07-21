package org.gsdistance.grimmsServer.Stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TitleGenerator {
    private static final Map<String, String> moneyTitles = new HashMap<>();
    private static final Map<Long, String> blockBreakTitles = new HashMap<>();
    private static final Map<Long, String> killTitles = new HashMap<>();
    private static final Map<String, String> titleDescriptions = new HashMap<>();
    
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
        killTitles.put(10000L, "Executioneer");
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
        
        // Money title descriptions
        titleDescriptions.put("Pocket Change", "A small amount of money, but it's a start.");
        titleDescriptions.put("Coin Collector", "Starting to accumulate some wealth.");
        titleDescriptions.put("Wealthy", "You have a respectable amount of money.");
        titleDescriptions.put("Rich", "Money is no longer a concern for you.");
        titleDescriptions.put("Affluent", "You live a life of luxury and comfort.");
        titleDescriptions.put("Millionaire", "You've reached the first major milestone of wealth.");
        titleDescriptions.put("Multi-Millionaire", "Your wealth continues to grow exponentially.");
        titleDescriptions.put("Hundred-Millionaire", "You're among the wealthiest individuals.");
        titleDescriptions.put("Billionaire", "You've achieved extraordinary financial success.");
        titleDescriptions.put("Multi-Billionaire", "Your influence and wealth are immense.");
        titleDescriptions.put("Hundred-Billionaire", "You control vast economic resources.");
        titleDescriptions.put("Trillionaire", "Wealth beyond imagination.");
        titleDescriptions.put("Multi-Trillionaire", "Economic power on a global scale.");
        titleDescriptions.put("Hundred-Trillionaire", "Your wealth rivals entire nations.");
        titleDescriptions.put("Quadrillionaire", "Unprecedented accumulation of resources.");
        titleDescriptions.put("Multi-Quadrillionaire", "Wealth that defies comprehension.");
        titleDescriptions.put("Hundred-Quadrillionaire", "You possess more than can be counted.");
        titleDescriptions.put("Quintillionaire", "The pinnacle of financial achievement.");
        
        // Block break title descriptions
        titleDescriptions.put("Novice Miner", "Just starting to break blocks.");
        titleDescriptions.put("Apprentice Miner", "Learning the ways of the mine.");
        titleDescriptions.put("Miner", "A dedicated worker in the mines.");
        titleDescriptions.put("Stone Breaker", "You've broken through many obstacles.");
        titleDescriptions.put("Master Miner", "Your mining skills are exceptional.");
        titleDescriptions.put("Legendary Miner", "Your mining prowess is the stuff of legends.");
        titleDescriptions.put("Earth Shatterer", "You've broken through the earth itself.");
        titleDescriptions.put("World Digger", "You've dug through entire worlds.");
        titleDescriptions.put("Void Miner", "You mine where others cannot reach.");
        titleDescriptions.put("Dimension Breaker", "You break through dimensional barriers.");
        titleDescriptions.put("Reality Shatterer", "Your mining transcends reality.");
        titleDescriptions.put("Universe Digger", "You've dug through the fabric of the universe.");
        titleDescriptions.put("Cosmic Miner", "You mine among the stars themselves.");
        titleDescriptions.put("Galaxy Breaker", "You break through entire galaxies.");
        titleDescriptions.put("Universal Miner", "You mine across all of existence.");
        
        // Kill title descriptions
        titleDescriptions.put("Novice Hunter", "Just starting your hunting journey.");
        titleDescriptions.put("Hunter", "You've proven yourself as a capable hunter.");
        titleDescriptions.put("Slayer", "You've slain many foes in battle.");
        titleDescriptions.put("Executioneer", "You deliver final judgment to your enemies.");
        titleDescriptions.put("Destroyer", "You leave nothing but destruction in your wake.");
        titleDescriptions.put("Godslayer", "You've defeated beings of immense power.");
        titleDescriptions.put("World Ender", "You've brought about the end of worlds.");
        titleDescriptions.put("Mass Murderer", "Your kill count is truly staggering.");
        titleDescriptions.put("Genocidal", "You've eliminated entire populations.");
        titleDescriptions.put("Apocalypse Bringer", "You are the harbinger of doom.");
        titleDescriptions.put("Extinction Event", "You've caused the extinction of species.");
        titleDescriptions.put("Universe Ender", "You've ended entire universes.");
        titleDescriptions.put("Reality Destroyer", "You've torn apart the fabric of reality.");
        titleDescriptions.put("Void Consumer", "You consume all in your path, even the void.");
        titleDescriptions.put("Oblivion", "You are the end of all things.");
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
    
    public static Map<String, String> getAllTitleDescriptions() {
        return new HashMap<>(titleDescriptions);
    }
    
    public static String getTitleDescription(String title) {
        return titleDescriptions.get(title);
    }
}
