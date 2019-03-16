package me.robnoo02.venixmention;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.ImmutableList;

public class Main extends JavaPlugin implements Listener {

	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void tabComplete(PlayerChatTabCompleteEvent e) {
		String message = e.getChatMessage();
		if(message.contains("@") && !message.substring(message.lastIndexOf("@"), message.length()-1).contains(" ")) {
			String name = message.substring(message.lastIndexOf("@"), message.length()-1);
			for(Player p: Bukkit.getOnlinePlayers()) {
				if(p.getName().toLowerCase().startsWith(name.toLowerCase()))
					e.getTabCompletions().add("@" + p.getName());
				else if(p.getDisplayName().toLowerCase().startsWith(name.toLowerCase()))
					e.getTabCompletions().add("@" + p.getDisplayName());
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent e) {
		e.setFormat("§c(§6" + e.getPlayer().getName() + "§c) §e" + e.getMessage());
		// Test 1
		for (Player p : ImmutableList.copyOf(e.getRecipients())) {
			if(e.getFormat().contains("@" + p.getName()) || e.getFormat().contains("@" + p.getCustomName())) {
				String format = e.getFormat();
				String replace = "@" + p.getName();
				e.getRecipients().remove(p);
				int index = format.indexOf(replace);
				Bukkit.getLogger().info("Index: " + String.valueOf(index));
				String part1 = format.substring(0, index);
				Bukkit.getLogger().info("Part1: " + part1);
				String part2 = format.substring(index + replace.length(), format.length());
				Bukkit.getLogger().info("part2: " + part2);
				String color = ChatColor.getLastColors(part1);
				if(color.equals(""))
					color = "§r";
				p.sendMessage(part1 + "§6" + replace + color + part2);
				return;
			}
		}
	}
}
