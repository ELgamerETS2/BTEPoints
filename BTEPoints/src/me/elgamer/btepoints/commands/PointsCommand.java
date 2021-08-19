package me.elgamer.btepoints.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.elgamer.btepoints.utils.Leaderboard;
import me.elgamer.btepoints.utils.PlayerData;
import me.elgamer.btepoints.utils.Points;
import me.elgamer.btepoints.utils.Weekly;

public class PointsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("&cYou cannot use this command!");
			return true;
		}

		Player p = (Player) sender;

		Points mysql = new Points();
		Weekly weekly = new Weekly();
		PlayerData pd = new PlayerData();
		Leaderboard lead = new Leaderboard();

		if (args.length == 0) {

			lead = mysql.getPoints(p.getUniqueId().toString(), lead);

			if (lead == null) {
				p.sendMessage(ChatColor.RED + "Not enough entries to create a leaderboard!");
				return true;

			}

			if (lead.points[0] == 0) {
				p.sendMessage(ChatColor.RED + "Nobody has points!");
				return true;
			}

			p.sendMessage(String.format("%-6s%-8s%-16s", "#", "Points" , "Username"));
			p.sendMessage("------------------------");

			String[] names = pd.getNames(lead.uuids);

			for (int i = 0; i < lead.points.length; i++) {

				if (names[i] == null) {
					break;
				}
				p.sendMessage(String.format("%-6s%-8s%-16s", lead.position[i], lead.points[i] , names[i]));

			}
			return true;
		}

		if (args.length != 1 && args.length != 2 ) {
			p.sendMessage(ChatColor.RED + "/points top/<name> or /points weekly top/<name> ");
			return true;
		}

		if (args[0].equalsIgnoreCase("top")) {
			lead = mysql.getOrderedPoints(p.getUniqueId().toString(), lead);

			if (lead == null) {
				p.sendMessage(ChatColor.RED + "Not enough entries to create a leaderboard!");
				return true;
			}

			if (lead.points[0] == 0) {
				p.sendMessage(ChatColor.RED + "Nobody has points!");
				return true;
			}

			p.sendMessage(String.format("%-6s%-8s%-16s", "#", "Points" , "Username"));
			p.sendMessage("------------------------");

			String[] names = pd.getNames(lead.uuids);

			for (int i = 0; i < lead.points.length; i++) {

				if (names[i] == null) {
					break;
				}
				p.sendMessage(String.format("%-6s%-8s%-16s", lead.position[i], lead.points[i] , names[i]));

			}

			return true;
			
		} else if (args[0].equalsIgnoreCase("weekly")) {
			
			if (args.length == 1) {
				
				lead = weekly.getPoints(p.getUniqueId().toString(), lead);

				if (lead == null) {
					p.sendMessage(ChatColor.RED + "Not enough entries to create a leaderboard!");
					return true;

				}

				if (lead.points[0] == 0) {
					p.sendMessage(ChatColor.RED + "Nobody has points!");
					return true;
				}

				p.sendMessage(String.format("%-6s%-8s%-16s", "#", "Points" , "Username"));
				p.sendMessage("------------------------");

				String[] names = pd.getNames(lead.uuids);

				for (int i = 0; i < lead.points.length; i++) {

					if (names[i] == null) {
						break;
					}
					p.sendMessage(String.format("%-6s%-8s%-16s", lead.position[i], lead.points[i] , names[i]));

				}
				return true;
				
			}
			
			if (args[1].equalsIgnoreCase("top")) {
				
				lead = weekly.getOrderedPoints(p.getUniqueId().toString(), lead);

				if (lead == null) {
					p.sendMessage(ChatColor.RED + "Not enough entries to create a leaderboard!");
					return true;
				}

				if (lead.points[0] == 0) {
					p.sendMessage(ChatColor.RED + "Nobody has points!");
					return true;
				}

				p.sendMessage(String.format("%-6s%-8s%-16s", "#", "Points" , "Username"));
				p.sendMessage("------------------------");

				String[] names = pd.getNames(lead.uuids);

				for (int i = 0; i < lead.points.length; i++) {

					if (names[i] == null) {
						break;
					}
					p.sendMessage(String.format("%-6s%-8s%-16s", lead.position[i], lead.points[i] , names[i]));

				}

				return true;
			} else {
				
				String uuid = pd.getUuid(args[1]);

				if (uuid == null) {
					p.sendMessage(ChatColor.RED + "This player has never connected to the server!");
					return true;
				} 

				if (weekly.userExists(uuid)) {
					lead = weekly.getPoints(uuid, lead);
				} else {
					p.sendMessage(ChatColor.RED + "This player has not connected to the server!");
					return true;
				}

				if (lead == null) {
					p.sendMessage(ChatColor.RED + "Not enough entries to create a leaderboard!");
					return true;

				}

				if (lead.points[0] == 0) {
					p.sendMessage(ChatColor.RED + "Nobody has points!");
					return true;
				}

				p.sendMessage(String.format("%-6s%-8s%-16s", "#", "Points" , "Username"));
				p.sendMessage("------------------------");

				String[] names = pd.getNames(lead.uuids);

				for (int i = 0; i < lead.points.length; i++) {

					if (names[i] == null) {
						break;
					}
					p.sendMessage(String.format("%-6s%-8s%-16s", lead.position[i], lead.points[i] , names[i]));

				}

				return true;
			}
			
		} else {

			String uuid = pd.getUuid(args[0]);

			if (uuid == null) {
				p.sendMessage(ChatColor.RED + "This player has never connected to the server!");
				return true;
			} 

			if (mysql.userExists(uuid)) {
				lead = mysql.getPoints(uuid, lead);
			} else {
				p.sendMessage(ChatColor.RED + "This player has not connected to the server!");
				return true;
			}

			if (lead == null) {
				p.sendMessage(ChatColor.RED + "Not enough entries to create a leaderboard!");
				return true;

			}

			if (lead.points[0] == 0) {
				p.sendMessage(ChatColor.RED + "Nobody has points!");
				return true;
			}

			p.sendMessage(String.format("%-6s%-8s%-16s", "#", "Points" , "Username"));
			p.sendMessage("------------------------");

			String[] names = pd.getNames(lead.uuids);

			for (int i = 0; i < lead.points.length; i++) {

				if (names[i] == null) {
					break;
				}
				p.sendMessage(String.format("%-6s%-8s%-16s", lead.position[i], lead.points[i] , names[i]));

			}

			return true;
		}
	}

}
