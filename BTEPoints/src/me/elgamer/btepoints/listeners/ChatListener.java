package me.elgamer.btepoints.listeners;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.elgamer.btepoints.Main;

public class ChatListener implements Listener {

	Main instance;
	
	public  ChatListener(Main plugin) {

		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		instance = plugin;
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {

		try {

			PreparedStatement statement = instance.getConnection().prepareStatement
					("UPDATE " + instance.pointsData + " SET MESSAGES=MESSAGES+" + 1 + " WHERE UUID=?");
			statement.setString(1, e.getPlayer().getUniqueId().toString());
			statement.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

}
