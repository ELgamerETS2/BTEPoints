package me.elgamer.btepoints.listeners;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.elgamer.btepoints.Main;
import me.elgamer.btepoints.utils.Points;
import me.elgamer.btepoints.utils.Weekly;

public class QuitListener implements Listener {
	
	public QuitListener(Main plugin) {

		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		
		Main instance = Main.getInstance(); 
		FileConfiguration config = instance.getConfig();
		int messagesPerPoint = config.getInt("messagesPerPoint");
		
		Points p = new Points();
		Weekly w = new Weekly();
		double messageCount = p.getMessageCount(e.getPlayer().getUniqueId().toString());
		int points = (int) Math.floor(messageCount/messagesPerPoint);
		
		
		p.addPoints(e.getPlayer().getUniqueId().toString(), points);
		w.addPoints(e.getPlayer().getUniqueId().toString(), points);
		
		p.removeMessages(e.getPlayer().getUniqueId().toString(), points*messagesPerPoint);
	}

}
