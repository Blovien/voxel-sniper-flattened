package com.thevoxelbox.voxelsniper.command.executor;

import com.thevoxelbox.voxelsniper.VoxelSniperPlugin;
import com.thevoxelbox.voxelsniper.command.CommandExecutor;
import com.thevoxelbox.voxelsniper.command.TabCompleter;
import com.thevoxelbox.voxelsniper.config.VoxelSniperConfig;
import com.thevoxelbox.voxelsniper.sniper.Sniper;
import com.thevoxelbox.voxelsniper.sniper.SniperRegistry;
import com.thevoxelbox.voxelsniper.sniper.toolkit.BlockTracer;
import com.thevoxelbox.voxelsniper.sniper.toolkit.Toolkit;
import com.thevoxelbox.voxelsniper.sniper.toolkit.ToolkitProperties;
import com.thevoxelbox.voxelsniper.util.message.Messenger;
import it.blovien.betterbrushes.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BiomeExecutor implements CommandExecutor, TabCompleter {

	private VoxelSniperPlugin plugin;

	public BiomeExecutor(VoxelSniperPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void executeCommand(CommandSender sender, String[] arguments) {
		SniperRegistry sniperRegistry = this.plugin.getSniperRegistry();
		Player player = (Player) sender;
		Sniper sniper = sniperRegistry.getSniper(player);
		if (sniper == null) return;
		Toolkit toolkit = sniper.getCurrentToolkit();
		if (toolkit == null) return;
		ToolkitProperties toolkitProperties = toolkit.getProperties();
		if (toolkitProperties == null) return;
		Messenger messenger = new Messenger(sender);
		if (arguments.length == 0) {
			BlockTracer blockTracer = toolkitProperties.createBlockTracer(player);
			Block targetBlock = blockTracer.getTargetBlock();
			if (targetBlock != null) {
				Biome targetBiome = targetBlock.getBiome();
				toolkitProperties.setBiome(targetBiome);
				messenger.sendBiomeNameMessage(targetBiome);
			}
			return;
		}

		Optional<Biome> selectedBiome = Arrays.stream(Biome.values())
			.filter(biome -> arguments[0].equalsIgnoreCase(biome.name()))
			.findFirst();

		if (selectedBiome.isPresent()) {
			toolkitProperties.setBiome(selectedBiome.get());
			messenger.sendBiomeNameMessage(selectedBiome.get());
		} else {
			Messages.send(sender, ChatColor.RED + "You have entered an invalid Biome.");
		}
	}

	@Override
	public List<String> complete(CommandSender sender, String[] arguments) {
		if (arguments.length == 1) {
			String argument = arguments[0];
			String argumentLowered = argument.toLowerCase();
			return Stream.of(Arrays.toString(Biome.values()))
				.filter(biome -> biome.startsWith(argumentLowered))
				.collect(Collectors.toList());
		}

		return Collections.emptyList();
	}
}
