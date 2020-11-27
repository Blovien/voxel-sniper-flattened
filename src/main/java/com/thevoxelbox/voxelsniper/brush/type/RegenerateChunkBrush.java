package com.thevoxelbox.voxelsniper.brush.type;

import com.thevoxelbox.voxelsniper.sniper.Sniper;
import com.thevoxelbox.voxelsniper.sniper.snipe.Snipe;
import com.thevoxelbox.voxelsniper.sniper.snipe.message.SnipeMessenger;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * Regenerates the target chunk.
 */
public class RegenerateChunkBrush extends AbstractBrush {

	@Override
	public void handleArrowAction(Snipe snipe) {
		generateChunk(snipe);
	}

	@Override
	public void handleGunpowderAction(Snipe snipe) {
		generateChunk(snipe);
	}

	@SuppressWarnings("deprecation")
	private void generateChunk(Snipe snipe) {
		Block targetBlock = getTargetBlock();
		Chunk chunk = targetBlock.getChunk();
		World world = getWorld();
		SnipeMessenger messenger = snipe.createMessenger();
		messenger.sendMessage("Generate that chunk! " + chunk.getX() + " " + chunk.getZ());
		world.regenerateChunk(chunk.getX(), chunk.getZ());
		world.refreshChunk(chunk.getX(), chunk.getZ());
	}

	@Override
	public void sendInfo(Snipe snipe) {
		SnipeMessenger messenger = snipe.createMessenger();
		messenger.sendBrushNameMessage();
		messenger.sendMessage(ChatColor.LIGHT_PURPLE + "Tread lightly.");
		messenger.sendMessage(ChatColor.LIGHT_PURPLE + "This brush will melt your spleen and sell your kidneys.");
	}
}
