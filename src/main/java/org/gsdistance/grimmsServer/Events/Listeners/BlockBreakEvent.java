package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.gsdistance.grimmsServer.Commands.GAuthCommand.GAuthBaseCommand;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.Item.ItemLevelHandler;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerLevelHandler;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Constructable.World.ChunkMetadata;
import org.gsdistance.grimmsServer.Data.Player.PlayerCapability;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.WorldStats;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockBreakEvent {
    public BlockBreakEvent() {
    }

    public static void Event(org.bukkit.event.block.BlockBreakEvent event) {
        if (!GAuthBaseCommand.isLoggedIn(event.getPlayer())) {
            event.getPlayer().sendMessage(ChatColor.RED + "You must login with /gAuth login <password> to perform this action.");
            event.setCancelled(true);
            return;
        }

        ChunkMetadata chunkMetadata = ChunkMetadata.getChunkMetadata(event.getBlock().getChunk());
        if (!event.getPlayer().hasPermission("grimmsserver.faction.bypass") && chunkMetadata.factionUUID != null) {
            Faction faction = Faction.getFaction(chunkMetadata.factionUUID);
            if (faction != null && !faction.isMember(event.getPlayer().getUniqueId())) {
                event.getPlayer().sendMessage("§cYou cannot break blocks in this area, it is claimed by " + faction.name + "!");
                event.setCancelled(true);
                return;
            }
        }

        if (event.getPlayer().getType() == EntityType.PLAYER) {
            PlayerStats.getPlayerStats(event.getPlayer()).changeStat("money", (int) Math.round(PlayerLevelHandler.getLevelHandler(event.getPlayer()).getLesserMoneyMultiplier()));
            PlayerStats.getPlayerStats(event.getPlayer()).changeStat("tPoint", (int) Math.round((double) 3.0F * PlayerLevelHandler.getLevelHandler(event.getPlayer()).getMoneyMultiplier()));
            PlayerStats.getPlayerStats(event.getPlayer()).changeStat("block_break_count", 1);
            PlayerLevelHandler.getLevelHandler(event.getPlayer()).addExp(3.0F);
            if (ItemLevelHandler.isItemLevelable(event.getPlayer().getInventory().getItemInMainHand())) {
                ItemLevelHandler.getLevelHandler(event.getPlayer()).addXp(5.0F);
            }
        }

        WorldStats.getWorldStats(event.getBlock().getWorld()).changeStat("block_break_count", 1);
        WorldStats.getWorldStats(event.getBlock().getWorld()).changeStat("wPoint", 3);
        
        // Handle Veinminer capability
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(event.getPlayer());
        if (playerMetadata.capabilities.containsKey(PlayerCapability.VEINMINER) && 
            playerMetadata.settings.contains(PlayerCapability.VEINMINER.capabilityId)) {
            handleVeinminer(event);
        }
    }
    
    private static void handleVeinminer(org.bukkit.event.block.BlockBreakEvent event) {
        Block originalBlock = event.getBlock();
        Material blockType = originalBlock.getType();
        
        // Check if it's an ore
        if (!isOre(blockType)) {
            return;
        }
        
        // Find all adjacent same-type ores within 8 blocks
        Set<Block> oreBlocks = findAdjacentOres(originalBlock, blockType, 8);
        
        // Remove the original block since we already have its event
        oreBlocks.remove(originalBlock);
        
        if (oreBlocks.isEmpty()) {
            return;
        }
        
        // Break all found ores and teleport drops
        for (Block oreBlock : oreBlocks) {
            // Collect drops before breaking
            List<org.bukkit.inventory.ItemStack> drops = new ArrayList<>(oreBlock.getDrops(event.getPlayer().getInventory().getItemInMainHand()));
            
            // Break the block
            oreBlock.setType(Material.AIR);
            
            // Teleport drops to original block location
            for (org.bukkit.inventory.ItemStack drop : drops) {
                Item droppedItem = oreBlock.getWorld().dropItemNaturally(originalBlock.getLocation(), drop);
                droppedItem.setPickupDelay(0);
            }
            
            // Fire a block break event for this block to register stats
            org.bukkit.event.block.BlockBreakEvent veinEvent = new org.bukkit.event.block.BlockBreakEvent(oreBlock, event.getPlayer());
            org.bukkit.Bukkit.getPluginManager().callEvent(veinEvent);
        }
    }
    
    private static boolean isOre(Material material) {
        return material == Material.COAL_ORE || 
               material == Material.DEEPSLATE_COAL_ORE ||
               material == Material.IRON_ORE || 
               material == Material.DEEPSLATE_IRON_ORE ||
               material == Material.GOLD_ORE || 
               material == Material.DEEPSLATE_GOLD_ORE ||
               material == Material.DIAMOND_ORE || 
               material == Material.DEEPSLATE_DIAMOND_ORE ||
               material == Material.EMERALD_ORE || 
               material == Material.DEEPSLATE_EMERALD_ORE ||
               material == Material.REDSTONE_ORE || 
               material == Material.DEEPSLATE_REDSTONE_ORE ||
               material == Material.LAPIS_ORE || 
               material == Material.DEEPSLATE_LAPIS_ORE ||
               material == Material.COPPER_ORE || 
               material == Material.DEEPSLATE_COPPER_ORE ||
               material == Material.NETHER_GOLD_ORE ||
               material == Material.NETHER_QUARTZ_ORE;
    }
    
    private static Set<Block> findAdjacentOres(Block startBlock, Material targetMaterial, int range) {
        Set<Block> found = new HashSet<>();
        List<Block> toCheck = new ArrayList<>();
        toCheck.add(startBlock);
        
        while (!toCheck.isEmpty()) {
            Block current = toCheck.remove(0);
            if (found.contains(current)) {
                continue;
            }
            
            if (current.getType() == targetMaterial) {
                found.add(current);
                
                // Check neighbors within range
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        for (int z = -1; z <= 1; z++) {
                            if (x == 0 && y == 0 && z == 0) {
                                continue;
                            }
                            
                            Block neighbor = current.getRelative(x, y, z);
                            if (!found.contains(neighbor) && 
                                !toCheck.contains(neighbor) &&
                                neighbor.getLocation().distance(startBlock.getLocation()) <= range) {
                                toCheck.add(neighbor);
                            }
                        }
                    }
                }
            }
        }
        
        return found;
    }
}
