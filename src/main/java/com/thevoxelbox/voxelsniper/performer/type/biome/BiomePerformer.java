package com.thevoxelbox.voxelsniper.performer.type.biome;

import com.thevoxelbox.voxelsniper.performer.Performer;
import com.thevoxelbox.voxelsniper.sniper.snipe.performer.PerformerSnipe;
import com.thevoxelbox.voxelsniper.sniper.toolkit.ToolkitProperties;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

public class BiomePerformer implements Performer {

	private Biome biome;

	@Override
	public void initialize(PerformerSnipe snipe) {
		ToolkitProperties toolkitProperties = snipe.getToolkitProperties();
		this.biome = toolkitProperties.getBiome();
	}

	@Override
	public void perform(Block block) {
		if (block.getBiome() != this.biome) {
			block.getWorld().setBiome(block.getX(), block.getY(), block.getZ(), biome);
		}
	}

	@Override
	public void sendInfo(PerformerSnipe snipe) {
		snipe.createMessageSender()
			.performerNameMessage()
			.biomeNameMessage()
			.send();
	}
}
