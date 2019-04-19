package com.thevoxelbox.voxelsniper.brush;

import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.SnipeData;
import com.thevoxelbox.voxelsniper.Undo;
import org.bukkit.Chunk;

/**
 * Regenerates the target chunk.
 *
 * @author Mick
 */
public class RegenerateChunkBrush extends Brush {

	/**
	 *
	 */
	public RegenerateChunkBrush() {
		this.setName("Chunk Generator 40k");
	}

	private void generateChunk(SnipeData v) {
		Chunk chunk = this.getTargetBlock()
			.getChunk();
		Undo undo = new Undo();
		for (int z = CHUNK_SIZE; z >= 0; z--) {
			for (int x = CHUNK_SIZE; x >= 0; x--) {
				for (int y = this.getWorld()
					.getMaxHeight(); y >= 0; y--) {
					undo.put(chunk.getBlock(x, y, z));
				}
			}
		}
		v.owner()
			.storeUndo(undo);
		v.sendMessage("Generate that chunk! " + chunk.getX() + " " + chunk.getZ());
		this.getWorld()
			.regenerateChunk(chunk.getX(), chunk.getZ());
		this.getWorld()
			.refreshChunk(chunk.getX(), chunk.getZ());
	}

	@Override
	protected final void arrow(SnipeData v) {
		this.generateChunk(v);
	}

	@Override
	protected final void powder(SnipeData v) {
		this.generateChunk(v);
	}

	@Override
	public final void info(Message vm) {
		vm.brushName(this.getName());
		vm.brushMessage("Tread lightly.");
		vm.brushMessage("This brush will melt your spleen and sell your kidneys.");
	}

	@Override
	public String getPermissionNode() {
		return "voxelsniper.brush.regeneratechunk";
	}
}
