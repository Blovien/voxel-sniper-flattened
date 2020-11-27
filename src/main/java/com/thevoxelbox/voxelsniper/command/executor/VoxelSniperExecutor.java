package com.thevoxelbox.voxelsniper.command.executor;

import com.thevoxelbox.voxelsniper.VoxelSniperPlugin;
import com.thevoxelbox.voxelsniper.brush.BrushRegistry;
import com.thevoxelbox.voxelsniper.brush.property.BrushProperties;
import com.thevoxelbox.voxelsniper.command.CommandExecutor;
import com.thevoxelbox.voxelsniper.performer.PerformerRegistry;
import com.thevoxelbox.voxelsniper.performer.property.PerformerProperties;
import com.thevoxelbox.voxelsniper.sniper.Sniper;
import com.thevoxelbox.voxelsniper.sniper.SniperRegistry;
import com.thevoxelbox.voxelsniper.sniper.toolkit.Toolkit;
import com.thevoxelbox.voxelsniper.sniper.toolkit.ToolkitProperties;
import com.thevoxelbox.voxelsniper.util.text.NumericParser;
import it.blovien.betterbrushes.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class VoxelSniperExecutor implements CommandExecutor {

	private VoxelSniperPlugin plugin;

	public VoxelSniperExecutor(VoxelSniperPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void executeCommand(CommandSender sender, String[] arguments) {
		SniperRegistry sniperRegistry = this.plugin.getSniperRegistry();
		Player player = (Player) sender;
		Sniper sniper = sniperRegistry.getSniper(player);
		if (sniper == null) {
			return;
		}
		if (arguments.length >= 1) {
			String firstArgument = arguments[0];
			if (firstArgument.equalsIgnoreCase("brushes")) {
				BrushRegistry brushRegistry = this.plugin.getBrushRegistry();
				Map<String, BrushProperties> brushes = brushRegistry.getBrushesProperties();
				Set<String> aliases = brushes.keySet();
				String aliasesString = String.join(", ", aliases);
				Messages.send(sender,aliasesString);
				return;
			} else if (firstArgument.equalsIgnoreCase("range")) {
				Toolkit toolkit = sniper.getCurrentToolkit();
				if (toolkit == null) {
					return;
				}
				ToolkitProperties toolkitProperties = toolkit.getProperties();
				if (toolkitProperties == null) {
					return;
				}
				if (arguments.length == 2) {
					Integer range = NumericParser.parseInteger(arguments[1]);
					if (range == null) {
						Messages.send(sender, "Can't parse number.");
						return;
					}
					if (range < 1) {
						Messages.send(sender, "Values less than 1 are not allowed.");
					}
					toolkitProperties.setBlockTracerRange(range);
				} else {
					toolkitProperties.setBlockTracerRange(0);
				}
				Integer blockTracerRange = toolkitProperties.getBlockTracerRange();
				Messages.send(sender,ChatColor.GOLD + "Distance Restriction toggled " + ChatColor.DARK_RED + (blockTracerRange == null ? "off" : "on") + ChatColor.GOLD + ". Range is " + ChatColor.LIGHT_PURPLE + blockTracerRange);
				return;
			} else if (firstArgument.equalsIgnoreCase("perf")) {
				PerformerRegistry performerRegistry = this.plugin.getPerformerRegistry();
				Map<String, PerformerProperties> performerProperties = performerRegistry.getPerformerProperties();
				Set<String> aliases = performerProperties.keySet();
				String aliasesString = String.join(", ", aliases);
				Messages.send(sender,ChatColor.AQUA + "Available performers (abbreviated):");
				Messages.send(sender,aliasesString);
				return;
			} else if (firstArgument.equalsIgnoreCase("perflong")) {
				PerformerRegistry performerRegistry = this.plugin.getPerformerRegistry();
				String names = performerRegistry.getPerformerProperties()
					.values()
					.stream()
					.map(PerformerProperties::getName)
					.collect(Collectors.joining(", "));
				Messages.send(sender,ChatColor.AQUA + "Available performers:");
				Messages.send(sender,names);
				return;
			} else if (firstArgument.equalsIgnoreCase("enable")) {
				sniper.setEnabled(true);
				Messages.send(sender,"VoxelSniper is " + (sniper.isEnabled() ? "enabled" : "disabled"));
				return;
			} else if (firstArgument.equalsIgnoreCase("disable")) {
				sniper.setEnabled(false);
				Messages.send(sender,"VoxelSniper is " + (sniper.isEnabled() ? "enabled" : "disabled"));
				return;
			} else if (firstArgument.equalsIgnoreCase("toggle")) {
				sniper.setEnabled(!sniper.isEnabled());
				Messages.send(sender,"VoxelSniper is " + (sniper.isEnabled() ? "enabled" : "disabled"));
				return;
			}
		}
		Messages.send(sender, ChatColor.DARK_RED + "VoxelSniper - Current Brush Settings:");
		sniper.sendInfo(sender);
	}
}
