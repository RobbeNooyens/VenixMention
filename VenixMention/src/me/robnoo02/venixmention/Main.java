package me.robnoo02.venixmention;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.ImmutableList;

/**
 * One class since this plugin only consists of 2 event methods.
 * This plugin colors tagged players with @-sign in chat.
 * This plugin is requested by @Pie3, so color codes are hardcoded to keep it lightweight.
 * 
 * @author Robnoo02
 *
 */
public class Main extends JavaPlugin implements Listener {

	/**
	 * Register events on enable.
	 */
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	/**
	 * Handles tab complete event.
	 * Automatically fills in the name of an online player based on first given letters.
	 */
	@EventHandler
	public void tabComplete(PlayerChatTabCompleteEvent e) {
		String message = e.getChatMessage();
		if (message.contains("@") && !message.substring(message.lastIndexOf("@"), message.length() - 1).contains(" ")) { // Checks if message contains @-sign.
			String name = message.substring(message.lastIndexOf("@"), message.length() - 1); // Saves @-sign followed by player name.
			for (Player p : Bukkit.getOnlinePlayers()) { // Iterates over online players.
				if (p.getName().toLowerCase().startsWith(name.toLowerCase().replaceAll("@", ""))) // Checks if player is tagged.
					e.getTabCompletions().add("@" + p.getName()); // Adds player as possible auto complete.
				else if (p.getDisplayName().toLowerCase().startsWith(name.toLowerCase())) // Checks if displayname is used.
					e.getTabCompletions().add("@" + p.getDisplayName()); // Adds displayname.
			}
		}
	}

	/**
	 * Handles chatmessage event.
	 * Checks whether message contains @ or not.
	 * Colors tagged name for tagged player.
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent e) {
		for (Player p : ImmutableList.copyOf(e.getRecipients())) { // Iterates over message receivers.
			if (e.getFormat().contains("@" + p.getName()) || e.getFormat().contains("@" + p.getDisplayName())) { // Checks if player is tagged.
				String format = e.getFormat();
				String replace = e.getFormat().contains("@" + p.getName()) ? "@" + p.getName() : "@" + p.getDisplayName(); // @-sign + player name
				e.getRecipients().remove(p); // Removes player from receivers and sends custom private message with colored name.
				int index = format.indexOf(replace); // Index to split format in 2 parts.
				String part1 = format.substring(0, index);
				String part2 = format.substring(index + replace.length(), format.length());
				String color = ChatColor.getLastColors(part1); // Saves last color to go further with after colored name.
				if (color.equals(""))
					color = "§r"; // Resets color to prevent coloring uncolored text.
				p.sendMessage(part1 + "§6" + replace + color + part2); // Sends single seperate message with colored name.
				p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 10); // Plays sound to notify player
				return; // Stops iterating over players.
			}
		}
	}
}
