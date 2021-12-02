package me.elgamer.btepoints.listeners;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.elgamer.btepoints.Main;

public class ChatListener implements Listener {

	Main instance;
	DataSource dataSource;
	
	public ChatListener(Main plugin, DataSource dataSource) {

		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		instance = plugin;
		this.dataSource = dataSource;
	}
	
	private Connection conn() throws SQLException {
		return dataSource.getConnection();
	}


	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {

		try (Connection conn = conn(); PreparedStatement statement = conn.prepareStatement(
				"UPDATE points_data SET messages=messages+" + 1 + " WHERE uuid=?;"
				)){

			statement.setString(1, e.getPlayer().getUniqueId().toString());
			statement.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

}
