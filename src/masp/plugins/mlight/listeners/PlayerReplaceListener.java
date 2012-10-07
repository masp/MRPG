package masp.plugins.mlight.listeners;

import java.sql.SQLException;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.data.player.MPlayer;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerReplaceListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event) {
		try {
			if (!event.getPlayer().getClass().equals(MPlayer.class)) {
				MPlayer.updateBukkitEntity(((CraftPlayer) event.getPlayer()).getHandle());
			}
			
			MPlayer player = MPlayer.getPlayer(event.getPlayer());
			MRPG.getDataManager().loadPlayer(player);
			MRPG.getPlayerManager().addPlayer(player);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
}
