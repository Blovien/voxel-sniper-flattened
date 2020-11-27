package com.thevoxelbox.voxelsniper.brush.type;

import com.thevoxelbox.voxelsniper.brush.type.AbstractBrush;
import com.thevoxelbox.voxelsniper.brush.type.ErodeBrush;
import com.thevoxelbox.voxelsniper.brush.type.blend.BlendBallBrush;
import com.thevoxelbox.voxelsniper.sniper.snipe.Snipe;

public class ErodeBlendBrush extends AbstractBrush {

	private BlendBallBrush blendBallBrush;
	private ErodeBrush erodeBrush;

	public ErodeBlendBrush() {
		this.blendBallBrush = new BlendBallBrush();
		this.erodeBrush = new ErodeBrush();
	}

	@Override
	public void handleCommand(String[] parameters, Snipe snipe) {
		erodeBrush.handleCommand(parameters, snipe);
		blendBallBrush.handleCommand(parameters, snipe);
	}

	@Override
	public void handleArrowAction(Snipe snipe) {
		this.blendBallBrush.setAirExcluded(false);
		this.blendBallBrush.setTargetBlock(this.getTargetBlock());
		this.blendBallBrush.handleArrowAction(snipe);
		this.erodeBrush.setTargetBlock(this.getTargetBlock());
		this.erodeBrush.handleArrowAction(snipe);
	}

	@Override
	public void handleGunpowderAction(Snipe snipe) {
		this.blendBallBrush.setAirExcluded(false);
		this.blendBallBrush.setTargetBlock(this.getTargetBlock());
		this.blendBallBrush.handleArrowAction(snipe);
		this.erodeBrush.setTargetBlock(this.getTargetBlock());
		this.erodeBrush.handleGunpowderAction(snipe);
	}

	@Override
	public void sendInfo(Snipe snipe) {
		this.erodeBrush.sendInfo(snipe);
		this.blendBallBrush.sendInfo(snipe);
	}
}
