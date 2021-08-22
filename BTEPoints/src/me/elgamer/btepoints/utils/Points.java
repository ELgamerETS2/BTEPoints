package me.elgamer.btepoints.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import me.elgamer.btepoints.Main;

public class Points {

	public static void addPoints(String uuid, int points) {

		Main instance = Main.getInstance();

		try {
			PreparedStatement statement = instance.getConnection().prepareStatement
					("UPDATE " + instance.pointsData + " SET ADD_POINTS=ADD_POINTS+" + points + " WHERE UUID=?");
			statement.setString(1, uuid);

			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
