package me.elgamer.btepoints;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import me.elgamer.btepoints.listeners.ChatListener;

public class Main extends JavaPlugin {

	//MySQL
	public DataSource dataSource;
	private Connection connection;
	public String host, database, username, password, pointsData, playerData, weeklyData, data;
	public int port;

	//Other
	static Main instance;
	static FileConfiguration config;

	@Override
	public void onEnable() {

		//Config Setup
		Main.instance = this;
		Main.config = this.getConfig();

		saveDefaultConfig();

		try {
			//MySQL
			host = config.getString("MySQL_host");
			port = config.getInt("MySQL_port");
			database = config.getString("MySQL_database");
			username = config.getString("MySQL_username");
			password = config.getString("MySQL_password");
			dataSource = MysqlSetup();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		
		new ChatListener(this, dataSource);

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


	private DataSource MysqlSetup() throws SQLException {
		
		MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();

		dataSource.setServerName(host);
		dataSource.setPortNumber(port);
		dataSource.setDatabaseName(database + "?&useSSL=false&");
		dataSource.setUser(username);
		dataSource.setPassword(password);

		testDataSource(dataSource);
		return dataSource;

	}
	
	private void testDataSource(DataSource dataSource) throws SQLException {

		try (Connection connection = dataSource.getConnection()) {
			if (!connection.isValid(1000)) {
				throw new SQLException("Could not establish database connection.");
			}
		}
	}

	public static Main getInstance() {
		return instance;
	}
}
