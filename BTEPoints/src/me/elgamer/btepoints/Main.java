package me.elgamer.btepoints;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.elgamer.btepoints.commands.AddPoints;
import me.elgamer.btepoints.commands.PointsCommand;
import me.elgamer.btepoints.commands.RemovePoints;
import me.elgamer.btepoints.listeners.ChatListener;
import me.elgamer.btepoints.listeners.JoinListener;
import me.elgamer.btepoints.listeners.QuitListener;
import me.elgamer.btepoints.utils.GetDay;
import me.elgamer.btepoints.utils.Points;
import me.elgamer.btepoints.utils.Weekly;
import net.luckperms.api.LuckPerms;

public class Main extends JavaPlugin {

	//MySQL
	private Connection connection;
	public String host, database, username, password, pointsData, playerData, weeklyData, data;
	public int port;

	//Other
	static Main instance;
	static FileConfiguration config;
	
	public static int timerIntervalSec;
	
	int minute = (int) 1200L;
	
	public static LuckPerms lp = null;


	@Override
	public void onEnable() {

		//Config Setup
		Main.instance = this;
		Main.config = this.getConfig();
		
		saveDefaultConfig();
		
		timerIntervalSec = config.getInt("timerInterval") * 60;

		//MySQL		
		mysqlSetup();
		
		//Creates the mysql table if not existing
		createPointsTable();
		createPlayerDatabase();
		
		createWeeklyTable();
		createPointsData();
		
		//Listeners
		new ChatListener(this);
		new JoinListener(this);
		new QuitListener(this);
		
		//Commands
		getCommand("addpoints").setExecutor(new AddPoints());
		getCommand("removepoints").setExecutor(new RemovePoints());
		getCommand("points").setExecutor(new PointsCommand());
		
		//LuckPerms
		setupLuckPerms();
				
		Points points = new Points();
		Weekly weekly = new Weekly();
		
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				points.timerPoints();
				weekly.timerPoints();
				getConnection();
			}
		}, 0L, minute * config.getInt("timerInterval"));

	}
	
	public void onDisable() {

		//MySQL
		try {
			if (connection != null && !connection.isClosed()) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MySQL disconnected from " + config.getString("MySQL_database"));
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void mysqlSetup() {

		host = config.getString("MySQL_host");
		port = config.getInt("MySQL_port");
		database = config.getString("MySQL_database");
		username = config.getString("MySQL_username");
		password = config.getString("MySQL_password");
		pointsData = config.getString("MySQL_pointsData");
		playerData = config.getString("MySQL_playerData");
		weeklyData = config.getString("MySQL_weeklyData");
		data = config.getString("MySQL_data");

		try {

			synchronized (this) {
				if (connection != null && !connection.isClosed()) {
					return;
				}

				Class.forName("com.mysql.jdbc.Driver");
				setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" 
						+ this.port + "/" + this.database, this.username, this.password + "autoReconnect=true&useSSL=false"));

				Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MySQL connected to " + config.getString("MySQL_database"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public Connection getConnection() {
		
		try {
			if (connection == null || connection.isClosed()) {
				mysqlSetup();
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public void createPointsTable() {
		try {
			PreparedStatement statement = instance.getConnection().prepareStatement
					("CREATE TABLE IF NOT EXISTS " + pointsData
							+ " ( UUID VARCHAR(36) NOT NULL , POINTS INT NOT NULL , MESSAGES INT NOT NULL , UNIQUE (UUID))");
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createPlayerDatabase() {
		try {
			PreparedStatement statement = instance.getConnection().prepareStatement
					("CREATE TABLE IF NOT EXISTS " + playerData
							+ " ( UUID VARCHAR(36) NOT NULL , NAME VARCHAR(36) NOT NULL , UNIQUE (UUID))");
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createWeeklyTable() {
		try {
			PreparedStatement statement = instance.getConnection().prepareStatement
					("CREATE TABLE IF NOT EXISTS " + weeklyData
							+ " ( UUID VARCHAR(36) NOT NULL , POINTS INT NOT NULL , MONDAY INT NOT NULL , TUESDAY INT NOT NULL , WEDNESDAY INT NOT NULL , THURSDAY INT NOT NULL , FRIDAY INT NOT NULL , SATURDAY INT NOT NULL , SUNDAY INT NOT NULL , UNIQUE (UUID))");
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createPointsData() {
		try {
			PreparedStatement statement = instance.getConnection().prepareStatement
					("CREATE TABLE IF NOT EXISTS " + data
							+ " ( DATA VARCHAR(36) NOT NULL , VALUE VARCHAR(36) NOT NULL , UNIQUE (DATA))");
			statement.executeUpdate();
			addDay();
			addLeader();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public void addDay() {
		try {
			PreparedStatement statement = instance.getConnection().prepareStatement
					("SELECT * FROM " + data + " WHERE DATA=?");
			statement.setString(1, "day");
			ResultSet results = statement.executeQuery();
			if (!(results.next())) {
				statement = instance.getConnection().prepareStatement
						("INSERT INTO " + data + " (DATA,VALUE) VALUE (?,?)");
				statement.setString(1, "day");
				statement.setString(2, String.valueOf(GetDay.getDay()));
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void addLeader() {
		try {
			PreparedStatement statement = instance.getConnection().prepareStatement
					("SELECT * FROM " + data + " WHERE DATA=?");
			statement.setString(1, "leader");
			ResultSet results = statement.executeQuery();
			if (!(results.next())) {
				statement = instance.getConnection().prepareStatement
						("INSERT INTO " + data + " (DATA,VALUE) VALUE (?,?)");
				statement.setString(1, "leader");
				statement.setString(2, "null");
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static LuckPerms getLuckPerms() {
		return lp;
	}
	
	private boolean setupLuckPerms() {
		RegisteredServiceProvider<LuckPerms> provider = getServer().getServicesManager().getRegistration(LuckPerms.class);
		lp = provider.getProvider();
		return lp != null;
	}
	
	public static int getInterval() {
		return timerIntervalSec;
	}

}
