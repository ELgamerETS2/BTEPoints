package me.elgamer.btepoints.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.elgamer.btepoints.Main;
import me.elgamer.btepoints.utils.Points;

public class ChatListener implements Listener {

	public  ChatListener(Main plugin) {

		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {

		Points p = new Points();

		p.addMessage(e.getPlayer().getUniqueId().toString());

	}

}
