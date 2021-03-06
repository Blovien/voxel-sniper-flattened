package com.thevoxelbox.voxelsniper;

import com.boydti.fawe.Fawe;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

public class Favs {
    private final VoxelSniperPlugin plugin;

    @SuppressWarnings("ConstantConditions")
	public Favs(JavaPlugin plugin) {
        this.plugin = (VoxelSniperPlugin) plugin;
        try {

			setupCommand("/p", (sender, command, label, args) -> {
				if (sender instanceof Player && sender.hasPermission("voxelsniper.sniper")) {
					Player player = (Player) sender;
					@Nullable PluginCommand cmd = plugin.getCommand("p");
					plugin.onCommand(player, cmd, label, args);
				}
				return false;
			});
			setupCommand("/d", (sender, command, label, args) -> {
				if (sender instanceof Player && sender.hasPermission("voxelsniper.sniper")) {
					Player player = (Player) sender;
					@Nullable PluginCommand cmd = plugin.getCommand("d");
					plugin.onCommand(player, cmd, label, args);
				}
				return false;
			});
        } catch (Throwable ignore) {}
    }

    public static void callEvent(Event event) {
        if (Fawe.isMainThread()) {
            Bukkit.getPluginManager().callEvent(event);
        } else {
            if (event.isAsynchronous()) {
                Bukkit.getPluginManager().callEvent(event);
            } else {
                try {
                    PluginManager plm = Bukkit.getPluginManager();
                    Class<? extends PluginManager> clazz = plm.getClass();
                    Method methodFireEvent = clazz.getDeclaredMethod("fireEvent", Event.class);
                    methodFireEvent.setAccessible(true);
                    methodFireEvent.invoke(plm, event);
                } catch (Throwable ignore) {}
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
	public void setupCommand(final String label, final CommandExecutor cmd) {
        plugin.getCommand(label).setExecutor(cmd);
    }
}
