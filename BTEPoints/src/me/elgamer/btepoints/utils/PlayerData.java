package me.elgamer.btepoints.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import me.elgamer.btepoints.Main;

public class PlayerData {
	
	public static void setBuildTime(String uuid, int time) {
		
		DataSource dataSource = Main.getInstance().dataSource;

		try (Connection conn = dataSource.getConnection(); PreparedStatement statement = conn.prepareStatement(
				"UPDATE player_data SET building_time=? WHERE uuid=?;"
				)){

			statement.setString(2, uuid);
			statement.setInt(1, time);
			
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static int getBuildTime(String uuid) {
		
		DataSource dataSource = Main.getInstance().dataSource;

		try (Connection conn = dataSource.getConnection(); PreparedStatement statement = conn.prepareStatement(
				"SELECT building_time FROM player_data WHERE uuid=?;"
				)){

			statement.setString(1, uuid);

			ResultSet results = statement.executeQuery();

			if (results.next()) {
				return (results.getInt("building_time"));
			} else {
				return 0;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}		
		
	}
}
