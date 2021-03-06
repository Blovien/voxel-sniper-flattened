package com.thevoxelbox.voxelsniper.brush.type.rotation;

import com.thevoxelbox.voxelsniper.brush.type.AbstractBrush;
import com.thevoxelbox.voxelsniper.sniper.snipe.Snipe;
import com.thevoxelbox.voxelsniper.sniper.snipe.message.SnipeMessenger;
import com.thevoxelbox.voxelsniper.sniper.toolkit.ToolkitProperties;
import com.thevoxelbox.voxelsniper.util.material.Materials;
import com.thevoxelbox.voxelsniper.util.text.NumericParser;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

// The X Y and Z variable names in this file do NOT MAKE ANY SENSE. Do not attempt to actually figure out what on earth is going on here. Just go to the
// original 2d horizontal brush if you wish to make anything similar to this, and start there. I didn't bother renaming everything.
public class Rotation2DVerticalBrush extends AbstractBrush {

	private int mode;
	private int brushSize;
	private BlockData[][][] snap;
	private double angle;

	@Override
	public void handleCommand(String[] parameters, Snipe snipe) {
		SnipeMessenger messenger = snipe.createMessenger();
		Double angle = NumericParser.parseDouble(parameters[0]);
		if (angle == null) {
			messenger.sendMessage("Exception while parsing parameter: " + parameters[0]);
			return;
		}
		this.angle = Math.toRadians(angle);
		messenger.sendMessage(ChatColor.GREEN + "Angle set to " + this.angle);
	}

	@Override
	public void handleArrowAction(Snipe snipe) {
		ToolkitProperties toolkitProperties = snipe.getToolkitProperties();
		this.brushSize = toolkitProperties.getBrushSize();
		if (this.mode == 0) {
			this.getMatrix();
			this.rotate();
		} else {
			SnipeMessenger messenger = snipe.createMessenger();
			messenger.sendMessage(ChatColor.RED + "Something went wrong.");
		}
	}

	@Override
	public void handleGunpowderAction(Snipe snipe) {
		ToolkitProperties toolkitProperties = snipe.getToolkitProperties();
		this.brushSize = toolkitProperties.getBrushSize();
		if (this.mode == 0) {
			this.getMatrix();
			this.rotate();
		} else {
			SnipeMessenger messenger = snipe.createMessenger();
			messenger.sendMessage(ChatColor.RED + "Something went wrong.");
		}
	}

	private void getMatrix() {
		int brushSize = (this.brushSize * 2) + 1;
		this.snap = new BlockData[brushSize][brushSize][brushSize];
		Block targetBlock = this.getTargetBlock();
		int sx = targetBlock.getX() - this.brushSize;
		for (int x = 0; x < this.snap.length; x++) {
			int sz = targetBlock.getZ() - this.brushSize;
			for (int z = 0; z < this.snap.length; z++) {
				int sy = targetBlock.getY() - this.brushSize;
				for (int y = 0; y < this.snap.length; y++) {
					Block block = clampY(sx, sy, sz); // why is this not sx + x, sy + y sz + z?
					this.snap[x][y][z] = block.getBlockData();
					block.setType(Material.AIR);
					sy++;
				}
				sz++;
			}
			sx++;
		}
	}

	private void rotate() {
		double brushSizeSquared = Math.pow(this.brushSize + 0.5, 2);
		double cos = Math.cos(this.angle);
		double sin = Math.sin(this.angle);
		boolean[][] doNotFill = new boolean[this.snap.length][this.snap.length];
		// I put y in the inside loop, since it doesn't have any power functions, should be much faster.
		// Also, new array keeps track of which x and z coords are being assigned in the rotated space so that we can
		// do a targeted filling of only those columns later that were left out.
		Block targetBlock = this.getTargetBlock();
		for (int x = 0; x < this.snap.length; x++) {
			int xx = x - this.brushSize;
			double xSquared = Math.pow(xx, 2);
			for (int z = 0; z < this.snap.length; z++) {
				int zz = z - this.brushSize;
				if (xSquared + Math.pow(zz, 2) <= brushSizeSquared) {
					double newX = (xx * cos) - (zz * sin);
					double newZ = (xx * sin) + (zz * cos);
					doNotFill[(int) newX + this.brushSize][(int) newZ + this.brushSize] = true;
					for (int y = 0; y < this.snap.length; y++) {
						int yy = y - this.brushSize;
						BlockData blockData = this.snap[y][x][z];
						Material type = blockData.getMaterial();
						if (Materials.isEmpty(type)) {
							continue;
						}
						setBlockData(targetBlock.getX() + yy, targetBlock.getY() + (int) newX, targetBlock.getZ() + (int) newZ, blockData);
					}
				}
			}
		}
		for (int x = 0; x < this.snap.length; x++) {
			double xSquared = Math.pow(x - this.brushSize, 2);
			int fx = x + targetBlock.getX() - this.brushSize;
			for (int z = 0; z < this.snap.length; z++) {
				if (xSquared + Math.pow(z - this.brushSize, 2) <= brushSizeSquared) {
					int fz = z + targetBlock.getZ() - this.brushSize;
					if (!doNotFill[x][z]) {
						// smart fill stuff
						for (int y = 0; y < this.snap.length; y++) {
							int fy = y + targetBlock.getY() - this.brushSize;
							Material a = getBlockType(fy, fx + 1, fz);
							Material b = getBlockType(fy, fx, fz - 1);
							Material c = getBlockType(fy, fx, fz + 1);
							Material d = getBlockType(fy, fx - 1, fz);
							BlockData aData = getBlockData(fy, fx + 1, fz);
							BlockData bData = getBlockData(fy, fx, fz - 1);
							BlockData dData = getBlockData(fy, fx - 1, fz);
							BlockData winner;
							if (a == b || a == c || a == d) { // I figure that since we are already narrowing it down to ONLY the holes left behind, it
								// should
								// be fine to do all 5 checks needed to be legit about it.
								winner = aData;
							} else if (b == d || c == d) {
								winner = dData;
							} else {
								winner = bData; // blockPositionY making this default, it will also automatically cover situations where B = C;
							}
							this.setBlockData(fy, fx, fz, winner);
						}
					}
				}
			}
		}
	}

	@Override
	public void sendInfo(Snipe snipe) {
		SnipeMessenger messenger = snipe.createMessenger();
		messenger.sendBrushNameMessage();
	}
}
