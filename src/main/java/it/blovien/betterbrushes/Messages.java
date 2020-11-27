package it.blovien.betterbrushes;

import com.boydti.fawe.Fawe;
import com.boydti.fawe.FaweAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Messages {

	public static String PREFIX = ChatColor.DARK_GRAY + "(" + ChatColor.DARK_RED + ChatColor.BOLD + "FAVS" + ChatColor.DARK_GRAY + ")" + ChatColor.RESET;

	public static void send(CommandSender sender, String message) {
		sender.sendMessage(Messages.PREFIX + " " + message);
	}
}
