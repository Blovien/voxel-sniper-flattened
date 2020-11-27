package com.thevoxelbox.voxelsniper.performer.type.material;

import com.thevoxelbox.voxelsniper.performer.Performer;
import com.thevoxelbox.voxelsniper.sniper.snipe.performer.PerformerSnipe;
import com.thevoxelbox.voxelsniper.sniper.toolkit.ToolkitProperties;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.List;

public class ExcludeMaterialPerformer implements Performer {

	private List<BlockData> excludeList;
	private Material type;

	@Override
	public void initialize(PerformerSnipe snipe) {
		ToolkitProperties toolkitProperties = snipe.getToolkitProperties();
		this.type = toolkitProperties.getBlockType();
		this.excludeList = toolkitProperties.getVoxelList();
	}

	@Override
	public void perform(Block block) {
		BlockData blockData = block.getBlockData();
		if (!this.excludeList.contains(blockData)) {
			block.setType(this.type);
		}
	}

	@Override
	public void sendInfo(PerformerSnipe snipe) {
		snipe.createMessageSender()
			.performerNameMessage()
			.voxelListMessage()
			.blockTypeMessage()
			.send();
	}
}
