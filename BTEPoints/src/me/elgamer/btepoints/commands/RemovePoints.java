package me.elgamer.btepoints.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.elgamer.btepoints.utils.PlayerData;
import me.elgamer.btepoints.utils.Points;
import me.elgamer.btepoints.utils.Weekly;

public class RemovePoints implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof Player) {
			//Convert sender to player
			Player p = (Player) sender;

			if (!(p.hasPermission("btepoints.removepoints"))) {
				p.sendMessage(ChatColor.RED + "You do not have permission for this command!");
				return true;
			}
		}
		
		if (args.length != 2) {
			return false;
		}
		
		int value = 0;
		
		try {
			value = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			return false;
		}
		
		Points points = new Points();	
		Weekly weekly = new Weekly();
		PlayerData pd = new PlayerData();
		
		String uuid = pd.getUuid(args[0]);
		
		if (uuid == null) {
			sender.sendMessage(ChatColor.RED + args[0] + " does not exist!");
			return true;
		} else {
			points.removePoints(uuid, value);
			weekly.addPoints(uuid, value);
			sender.sendMessage(ChatColor.GREEN + "Removed " + args[1] + " points from " + args[0]);
			return true;
		}
		
	}
}
