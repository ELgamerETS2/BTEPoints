package me.elgamer.btepoints.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.elgamer.btepoints.Main;

public class PlayerData {

	Main instance = Main.getInstance();

	public Boolean userExists(String uuid) {


		try {

			PreparedStatement statement = instance.getConnection().prepareStatement
					("SELECT * FROM " + instance.playerData + " WHERE UUID=?");
			statement.setString(1, uuid);
			ResultSet results = statement.executeQuery();

			if (results.next()) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void updateName(String uuid, String name) {

		try {

			PreparedStatement statement = instance.getConnection().prepareStatement
					("UPDATE " + instance.playerData + " SET NAME=? WHERE UUID=?");
			statement.setString(2, uuid);
			statement.setString(1, name);
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void createUser(String uuid, String name) {

		try {

			PreparedStatement statement = instance.getConnection().prepareStatement
					("INSERT INTO " + instance.playerData + " (UUID,NAME) VALUE (?,?)");
			statement.setString(1, uuid);
			statement.setString(2, name);
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public String createUser(String uuid) {

		try {

			PreparedStatement statement = instance.getConnection().prepareStatement
					("INSERT INTO " + instance.playerData + " (UUID,NAME) VALUE (?,?)");
			statement.setString(1, uuid);
			
			String name = MojangAPI.getName(uuid);
			
			statement.setString(2, name);
			statement.executeUpdate();
			
			return name;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public String getName(String uuid) {
		
		try {

			PreparedStatement statement = instance.getConnection().prepareStatement
					("SELECT * FROM " + instance.playerData + " WHERE UUID=?");
			statement.setString(1, uuid);
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				return results.getString("NAME");
			} else if (uuid == null) {
				return null;
			} else {
				return createUser(uuid);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public String[] getNames(String[] uuids) {

		String[] names = new String[uuids.length];
		
		for (int i = 0; i < uuids.length; i++) {
			
			names[i] = getName(uuids[i]);
			
		}

		return names;
	}
	
	public String getUuid(String name) {
		
		try {

			PreparedStatement statement = instance.getConnection().prepareStatement
					("SELECT * FROM " + instance.playerData + " WHERE NAME=?");
			statement.setString(1, name);
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				return results.getString("UUID");
			} else {
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
