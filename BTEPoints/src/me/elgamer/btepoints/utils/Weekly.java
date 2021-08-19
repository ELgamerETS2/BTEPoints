package me.elgamer.btepoints.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.earth2me.essentials.Essentials;

import me.elgamer.btepoints.Main;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.types.InheritanceNode;

public class Weekly {

	Main instance = Main.getInstance(); 
	FileConfiguration config = instance.getConfig();

	static Essentials ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");

	int onlinePoints = config.getInt("onlinePoints");
	
	public void timerPoints() {

		for (Player p : Bukkit.getOnlinePlayers()) {

			if (ess.getUser(p).isAfk() == false) {
				addPoints(p.getUniqueId().toString(), onlinePoints);
			}

		}

	}

	public void createUserIfNew(String uuid) {

		if (userExists(uuid) == false) {

			try {

				PreparedStatement statement = instance.getConnection().prepareStatement
						("INSERT INTO " + instance.weeklyData + " (UUID,POINTS,MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY) VALUE (?,?,?,?,?,?,?,?,?)");
				statement.setString(1, uuid);
				statement.setInt(2, 0);
				statement.setInt(3, 0);
				statement.setInt(4, 0);
				statement.setInt(5, 0);
				statement.setInt(6, 0);
				statement.setInt(7, 0);
				statement.setInt(8, 0);
				statement.setInt(9, 0);
				statement.executeUpdate();		

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean userExists(String uuid) {

		try {

			PreparedStatement statement = instance.getConnection().prepareStatement
					("SELECT * FROM " + instance.weeklyData + " WHERE UUID=?");
			statement.setString(1, uuid);

			ResultSet results = statement.executeQuery();

			return (results.next());

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public void addPoints(String uuid, int points) {

		updateDay();
		createUserIfNew(uuid);
		int day = GetDay.getDay();

		String dayString = null;


		switch (day) {

		case 1: 
			dayString = "MONDAY";
			break;
		case 2: 
			dayString = "TUESDAY";
			break;
		case 3:
			dayString = "WEDNESDAY";
			break;
		case 4: 
			dayString = "THURSDAY";
			break;
		case 5: 
			dayString = "FRIDAY";
			break;
		case 6: 
			dayString = "SATURDAY";
			break;
		case 7: 
			dayString = "SUNDAY";
			break;

		}

		try {

			PreparedStatement statement = instance.getConnection().prepareStatement
					("UPDATE " + instance.weeklyData + " SET POINTS=POINTS+" + points + ", " + dayString + "=" + dayString + "+" + points + " WHERE UUID=?");
			statement.setString(1, uuid);
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		updateLeader();

	}

	public void removePoints(String uuid, int points) {

		updateDay();
		createUserIfNew(uuid);
		int day = GetDay.getDay();

		try {

			PreparedStatement statement = instance.getConnection().prepareStatement
					("UPDATE " + instance.weeklyData + " SET POINTS=POINTS-" + points + ", " + day + "=" + day + "-" + points + " WHERE UUID=?");
			statement.setString(1, uuid);
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		updateLeader();

	}

	public void updateDay() {

		try {
			PreparedStatement statement = instance.getConnection().prepareStatement
					("SELECT * FROM " + instance.data + " WHERE DATA=?");
			statement.setString(1, "day");
			ResultSet results = statement.executeQuery();
			results.next();

			int day;
			try {
				day = Integer.parseInt(results.getString("VALUE"));
			}
			catch (NumberFormatException e)
			{
				return;
			}

			String dayString = null;

			if (day != GetDay.getDay()) {

				switch (GetDay.getDay()) {

				case 1: 
					dayString = "MONDAY";
					break;
				case 2: 
					dayString = "TUESDAY";
					break;
				case 3: 
					dayString = "WEDNESDAY";
					break;
				case 4: 
					dayString = "THURSDAY";
					break;
				case 5: 
					dayString = "FRIDAY";
					break;
				case 6: 
					dayString = "SATURDAY";
					break;
				case 7: 
					dayString = "SUNDAY";
					break;

				}

				PreparedStatement update = instance.getConnection().prepareStatement
						("UPDATE " + instance.weeklyData + " SET POINTS=POINTS-" + dayString);
				update.executeUpdate();

				update = instance.getConnection().prepareStatement
						("UPDATE " + instance.weeklyData + " SET " + dayString + "=" + 0);
				update.executeUpdate();

				update = instance.getConnection().prepareStatement
						("UPDATE " + instance.data + " SET VALUE=" + String.valueOf(GetDay.getDay()) + " WHERE DATA=?");
				update.setString(1,"day");
				update.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void updateLeader() {

		if (!(currentLeader().equals(getLeader()))) {

			LuckPerms lp = Main.getLuckPerms();
			UserManager userManager = lp.getUserManager();
			CompletableFuture<User>  userFuture = null;

			if (!(getLeader().equals("null"))) {

				userFuture = userManager.loadUser(UUID.fromString(getLeader()));

				userFuture.thenAcceptAsync(user -> {

					InheritanceNode node = InheritanceNode.builder("leader").build();
					user.data().add(node);
					userManager.saveUser(user);

				});
			}

			if (!(currentLeader().equals("null"))) {

				userFuture = userManager.loadUser(UUID.fromString(currentLeader()));

				userFuture.thenAcceptAsync(user -> {

					InheritanceNode node = InheritanceNode.builder("leader").build();
					user.data().remove(node);
					userManager.saveUser(user);

				});
			}

			try {

				PreparedStatement statement = instance.getConnection().prepareStatement
						("UPDATE " + instance.data + " SET VALUE=? WHERE DATA=?");
				statement.setString(1, getLeader());
				statement.setString(2, "leader");
				statement.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

	public String currentLeader() {

		try {

			PreparedStatement statement = instance.getConnection().prepareStatement
					("SELECT * FROM " + instance.data + " WHERE DATA=?");
			statement.setString(1, "leader");
			ResultSet results = statement.executeQuery();
			results.next();

			return results.getString("VALUE");

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public String getLeader() {

		try {

			PreparedStatement statement = instance.getConnection().prepareStatement
					("SELECT * FROM " + instance.weeklyData + " ORDER BY POINTS DESC");
			ResultSet results = statement.executeQuery();
			results.next();

			return results.getString("UUID");

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public Leaderboard getOrderedPoints(String uuid, Leaderboard lead) {

		try {
			createUserIfNew(uuid);
			PreparedStatement statement = instance.getConnection().prepareStatement
					("SELECT * FROM " + instance.weeklyData + " ORDER BY POINTS DESC");
			ResultSet results = statement.executeQuery();
			int pos = 0;
			for (int i = 0; i < 9; i++) {

				if (results.next()) {
					pos += 1;
					lead.position[i] = pos;
					lead.uuids[i] = results.getString("UUID");
					lead.points[i] = results.getInt("POINTS");
				} else {
					return lead;
				}

			}

			return lead;

		} catch (SQLException e) {
			e.printStackTrace();
			return lead;
		}
	}
	
	public Leaderboard getPoints(String uuid, Leaderboard lead) {

		try {
			createUserIfNew(uuid);
			PreparedStatement statement = instance.getConnection().prepareStatement
					("SELECT * FROM " + instance.weeklyData + " ORDER BY POINTS DESC");
			ResultSet results = statement.executeQuery();
			int pos = 0;
			
			while (results.next()) {
				pos += 1;
				if (results.getString("UUID").equals(uuid)) {
					break;
				}
			}
			
			for (int j = 0; j < 5; j++) {
				pos -= 1;
				if (results.previous()) {
				} else {
					break;
				}
			}
			
			for (int i = 0; i < 9; i++) {

				if (results.next()) {
					pos += 1;
					lead.position[i] = pos;
					lead.uuids[i] = results.getString("UUID");
					lead.points[i] = results.getInt("POINTS");
				} else {
					return lead;
				}

			}

			return lead;

		} catch (SQLException e) {
			e.printStackTrace();
			return lead;
		}
	}
}
