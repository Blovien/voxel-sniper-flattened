package com.thevoxelbox.voxelsniper.performer.type.ink;

import com.thevoxelbox.voxelsniper.performer.Performer;
import com.thevoxelbox.voxelsniper.sniper.snipe.performer.PerformerSnipe;
import com.thevoxelbox.voxelsniper.sniper.toolkit.ToolkitProperties;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public class InkPerformer implements Performer {

	private BlockData blockData;

	@Override
	public void initialize(PerformerSnipe snipe) {
		ToolkitProperties toolkitProperties = snipe.getToolkitProperties();
		this.blockData = toolkitProperties.getBlockData();
	}

	@Override
	public void perform(Block block) {
		block.setBlockData(this.blockData);
	}

	@Override
	public void sendInfo(PerformerSnipe snipe) {
		snipe.createMessageSender()
			.performerNameMessage()
			.blockDataMessage()
			.send();
	}
}
