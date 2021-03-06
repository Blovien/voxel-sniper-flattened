package com.thevoxelbox.voxelsniper.performer.type.combo;

import com.thevoxelbox.voxelsniper.performer.Performer;
import com.thevoxelbox.voxelsniper.sniper.snipe.performer.PerformerSnipe;
import com.thevoxelbox.voxelsniper.sniper.toolkit.ToolkitProperties;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.List;

public class ExcludeComboPerformer implements Performer {

	private List<BlockData> excludeList;
	private BlockData blockData;

	@Override
	public void initialize(PerformerSnipe snipe) {
		ToolkitProperties toolkitProperties = snipe.getToolkitProperties();
		this.blockData = toolkitProperties.getBlockData();
		this.excludeList = toolkitProperties.getVoxelList();
	}

	@Override
	public void perform(Block block) {
		BlockData blockData = block.getBlockData();
		if (!this.excludeList.contains(blockData)) {
			block.setBlockData(this.blockData);
		}
	}

	@Override
	public void sendInfo(PerformerSnipe snipe) {
		snipe.createMessageSender()
			.performerNameMessage()
			.voxelListMessage()
			.blockTypeMessage()
			.blockDataMessage()
			.send();
	}
}
