package com.thevoxelbox.voxelsniper.brush;

import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.SnipeData;
import org.bukkit.ChatColor;
import org.bukkit.Material;

/**
 * @author Monofraps
 */
@SuppressWarnings("deprecation")
public abstract class BlendBrushBase extends Brush {

	private static int maxBlockMaterialID;
	protected boolean excludeAir = true;
	protected boolean excludeWater = true;

	static {
		// Find highest placeable block ID
		for (Material material : Material.values()) {
			maxBlockMaterialID = ((material.isBlock() && (material.getId() > maxBlockMaterialID)) ? material.getId() : maxBlockMaterialID);
		}
	}

	/**
	 *
	 */
	protected abstract void blend(SnipeData v);

	@Override
	protected final void arrow(SnipeData v) {
		this.excludeAir = false;
		this.blend(v);
	}

	@Override
	protected final void powder(SnipeData v) {
		this.excludeAir = true;
		this.blend(v);
	}

	@Override
	public final void info(Message vm) {
		vm.brushName(this.getName());
		vm.size();
		vm.voxel();
		vm.custom(ChatColor.BLUE + "Water Mode: " + (this.excludeWater ? "exclude" : "include"));
	}

	@Override
	public void parameters(String[] par, SnipeData v) {
		for (int i = 1; i < par.length; ++i) {
			if (par[i].equalsIgnoreCase("water")) {
				this.excludeWater = !this.excludeWater;
				v.sendMessage(ChatColor.AQUA + "Water Mode: " + (this.excludeWater ? "exclude" : "include"));
			}
		}
	}

	/**
	 *
	 */
	protected static int getMaxBlockMaterialID() {
		return maxBlockMaterialID;
	}

	/**
	 *
	 */
	protected static void setMaxBlockMaterialID(int maxBlockMaterialID) {
		BlendBrushBase.maxBlockMaterialID = maxBlockMaterialID;
	}

	/**
	 *
	 */
	protected final boolean isExcludeAir() {
		return this.excludeAir;
	}

	/**
	 *
	 */
	protected final void setExcludeAir(boolean excludeAir) {
		this.excludeAir = excludeAir;
	}

	/**
	 *
	 */
	protected final boolean isExcludeWater() {
		return this.excludeWater;
	}

	/**
	 *
	 */
	protected final void setExcludeWater(boolean excludeWater) {
		this.excludeWater = excludeWater;
	}
}
