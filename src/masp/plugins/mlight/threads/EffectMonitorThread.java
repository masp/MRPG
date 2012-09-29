package masp.plugins.mlight.threads;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.data.effects.EffectManager;
import masp.plugins.mlight.data.player.MPlayer;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class EffectMonitorThread implements Runnable {
	
	@Override
	public void run() {
		for (World world : Bukkit.getWorlds()) {
			for (Player vPlayer : world.getPlayers()) {
				this.onTick(MRPG.getPlayer(vPlayer));
			}
		}
	}
	
	public void onTick(MPlayer player) {
		if (player == null || player.getPlayer() == null) return;
		int regenAmount = (int) player.getTotalEffects(MRPG.getEffectManager().getEffect(EffectManager.HEALTH_REGEN_AMOUNT));
		if (player.getHealth() + regenAmount < 0) {
			player.setHealth(1);
			return;
		}
		player.setHealth(Math.min(player.getHealth() + regenAmount, player.getMaxHealth()));
	}
	
}
