package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Constructable.World.ChunkMetadata;

public class ShowClaims {
    private static final int VISUAL_RADIUS = 3; // Show 3 chunks in each direction
    private static final int CHUNK_SIZE = 16;

    public ShowClaims() {
    }

    public static boolean subCommand(Player player) {
        PlayerMetadata playerMetadata = PlayerMetadata.getPlayerMetadata(player);
        if (playerMetadata.factionUUID == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return false;
        }

        Faction playerFaction = Faction.getFaction(playerMetadata.factionUUID);
        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "Your faction could not be found.");
            return false;
        }

        int playerChunkX = player.getLocation().getChunk().getX();
        int playerChunkZ = player.getLocation().getChunk().getZ();
        int playerY = player.getLocation().getBlockY();

        int claimsShown = 0;

        for (int x = playerChunkX - VISUAL_RADIUS; x <= playerChunkX + VISUAL_RADIUS; x++) {
            for (int z = playerChunkZ - VISUAL_RADIUS; z <= playerChunkZ + VISUAL_RADIUS; z++) {
                ChunkMetadata chunkMetadata = ChunkMetadata.getChunkMetadata(player.getWorld().getChunkAt(x, z));
                if (chunkMetadata != null && chunkMetadata.factionUUID != null) {
                    Color particleColor;
                    if (chunkMetadata.factionUUID.equals(playerFaction.uuid)) {
                        particleColor = Color.GREEN; // Your faction's claims
                    } else {
                        particleColor = Color.RED; // Other faction's claims
                    }

                    // Draw chunk border with particles
                    drawChunkBorder(player, x, z, playerY, particleColor);
                    claimsShown++;
                }
            }
        }

        player.sendMessage(ChatColor.GREEN + "Showing " + ChatColor.YELLOW + claimsShown + ChatColor.GREEN + " claimed chunks around you.");
        return true;
    }

    private static void drawChunkBorder(Player player, int chunkX, int chunkZ, int y, Color color) {
        int minX = chunkX * CHUNK_SIZE;
        int minZ = chunkZ * CHUNK_SIZE;
        int maxX = minX + CHUNK_SIZE;
        int maxZ = minZ + CHUNK_SIZE;

        Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1.0f);

        // Draw the four edges of the chunk
        for (int i = 0; i < CHUNK_SIZE; i++) {
            // North edge
            player.getWorld().spawnParticle(Particle.DUST, minX + i + 0.5, y, minZ + 0.5, 1, dustOptions);
            // South edge
            player.getWorld().spawnParticle(Particle.DUST, minX + i + 0.5, y, maxZ - 0.5, 1, dustOptions);
            // West edge
            player.getWorld().spawnParticle(Particle.DUST, minX + 0.5, y, minZ + i + 0.5, 1, dustOptions);
            // East edge
            player.getWorld().spawnParticle(Particle.DUST, maxX - 0.5, y, minZ + i + 0.5, 1, dustOptions);
        }
    }
}
