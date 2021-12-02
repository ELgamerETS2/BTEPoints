package me.elgamer.btepoints.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import me.elgamer.btepoints.Main;

public class Points {

	public static void addPoints(String uuid, int points) {

		DataSource dataSource = Main.getInstance().dataSource;

		try (Connection conn = dataSource.getConnection(); PreparedStatement statement = conn.prepareStatement(
				"UPDATE points_data SET add_points=add_points+" + points + " WHERE uuid=?;"
				)){

			statement.setString(1, uuid);

			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
