package com.thevoxelbox.voxelsniper.brush.type.blend;

import com.sk89q.worldedit.math.BlockVector3;
import com.thevoxelbox.voxelsniper.sniper.snipe.Snipe;
import com.thevoxelbox.voxelsniper.sniper.snipe.message.SnipeMessenger;
import com.thevoxelbox.voxelsniper.sniper.toolkit.ToolkitProperties;
import com.thevoxelbox.voxelsniper.util.Vectors;
import com.thevoxelbox.voxelsniper.util.math.MathHelper;
import com.thevoxelbox.voxelsniper.util.painter.Painters;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class BlendBallBrush extends AbstractBlendBrush {

	@Override
	public void handleCommand(String[] parameters, Snipe snipe) {
		SnipeMessenger messenger = snipe.createMessenger();
		if (parameters[0].equalsIgnoreCase("info")) {
			messenger.sendMessage(ChatColor.GOLD + "Blend Ball Parameters:");
			messenger.sendMessage(ChatColor.AQUA + "/b bb water -- toggle include or exclude (default: exclude) water");
			return;
		}
		super.handleCommand(parameters, snipe);
	}

	@Override
	public void blend(Snipe snipe) {
		ToolkitProperties toolkitProperties = snipe.getToolkitProperties();
		int brushSize = toolkitProperties.getBrushSize();
		int largeSphereVolume = (int) MathHelper.sphereVolume(brushSize + 2);
		int smallSphereVolume = (int) MathHelper.sphereVolume(brushSize);
		Block targetBlock = getTargetBlock();
		Map<BlockVector3, Block> largeSphere = new HashMap<>(largeSphereVolume);
		Map<BlockVector3, Block> smallSphere = new HashMap<>(smallSphereVolume);
		Map<BlockVector3, Material> smallSphereMaterials = new HashMap<>(smallSphereVolume);

		Painters.sphere()
			.center(targetBlock)
			.radius(brushSize + 2)
			.blockSetter(position -> largeSphere.put(position, getBlock(position)))
			.paint();

		Painters.sphere()
			.center(targetBlock)
			.radius(brushSize)
			.blockSetter(position -> {
				Block block = largeSphere.get(position);
				smallSphere.put(position, block);
				smallSphereMaterials.put(position, block.getType());
			})
			.paint();

		for (Block smallSphereBlock : smallSphere.values()) {
			BlockVector3 blockPosition = Vectors.of(smallSphereBlock);
			Map<Material, Integer> materialsFrequencies = new EnumMap<>(Material.class);

			Painters.cube()
				.center(smallSphereBlock)
				.radius(1)
				.blockSetter(position -> {
					if (position.equals(blockPosition)) return;
					Material material = largeSphere.get(position).getType();
					materialsFrequencies.merge(material, 1, Integer::sum);
				})
				.paint();

			Material material = findCommonMaterial(materialsFrequencies).getMaterial();
			if (material != null)
				smallSphereMaterials.put(blockPosition, material);
		}
		setBlocks(smallSphereMaterials);
	}
}
