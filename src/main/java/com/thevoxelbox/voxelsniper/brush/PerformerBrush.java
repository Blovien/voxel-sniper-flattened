package com.thevoxelbox.voxelsniper.brush;

import com.thevoxelbox.voxelsniper.sniper.snipe.Snipe;

/**
 * @author Voxel
 */
public interface PerformerBrush extends Brush {

	void handlePerformerCommand(String[] parameters, Snipe snipe);

	void sendPerformerInfo(Snipe snipe);

	void initialize(Snipe snipe);
}
