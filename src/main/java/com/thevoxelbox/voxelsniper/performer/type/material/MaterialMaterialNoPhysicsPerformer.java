package com.thevoxelbox.voxelsniper.performer.type.material;

import com.thevoxelbox.voxelsniper.performer.Performer;
import com.thevoxelbox.voxelsniper.sniper.snipe.performer.PerformerSnipe;
import com.thevoxelbox.voxelsniper.sniper.toolkit.ToolkitProperties;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class MaterialMaterialNoPhysicsPerformer implements Performer {

	private Material material;
	private Material replaceMaterial;

	@Override
	public void initialize(PerformerSnipe snipe) {
		ToolkitProperties toolkitProperties = snipe.getToolkitProperties();
		this.material = toolkitProperties.getBlockType();
		this.replaceMaterial = toolkitProperties.getReplaceBlockType();
	}

	@Override
	public void perform(Block block) {
		if (block.getType() == this.replaceMaterial) {
			block.setType(this.material, false);
		}
	}

	@Override
	public void sendInfo(PerformerSnipe snipe) {
		snipe.createMessageSender()
			.performerNameMessage()
			.blockTypeMessage()
			.replaceBlockTypeMessage()
			.send();
	}
}
