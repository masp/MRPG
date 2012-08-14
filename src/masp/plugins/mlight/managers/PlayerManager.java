package masp.plugins.mlight.managers;

import java.util.HashMap;
import java.util.Map;

import masp.plugins.mlight.data.player.MPlayer;

import org.bukkit.entity.Player;

public class PlayerManager {
	
	private Map<String, MPlayer> players;
	
	public PlayerManager() {
		players = new HashMap<String, MPlayer>();
	}
	
	public void addPlayer(MPlayer player) {
		if (!players.containsKey(player.getName())) {
			players.put(player.getName(), player);
		}
	}
	
	public MPlayer getPlayer(String name) {
		return players.get(name);
	}
	
	public MPlayer getPlayer(Player player) {
		return getPlayer(player.getName());
	}
	
	public void removePlayer(String name) {
		if (players.containsKey(name)) {
			players.remove(name);
		}
	}
	
}
