package me.elgamer.btepoints.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.elgamer.btepoints.Main;
import me.elgamer.btepoints.utils.PlayerData;
import me.elgamer.btepoints.utils.Points;
import me.elgamer.btepoints.utils.Weekly;

public class JoinListener implements Listener {
	
	public JoinListener(Main plugin) {

		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		
		PlayerData pd = new PlayerData();
		Weekly w = new Weekly();
		Points p = new Points();
		String uuid = e.getPlayer().getUniqueId().toString();
		String name = e.getPlayer().getName();
		
		if (pd.userExists(uuid)) {
			pd.updateName(uuid, name);
		} else {
			pd.createUser(uuid, name);
		}
		
		p.createUserIfNew(uuid);
		w.createUserIfNew(uuid);
		w.updateDay();
		w.updateLeader();
	}

}
