package masp.plugins.mlight.utils;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.data.effects.EffectCollection;
import masp.plugins.mlight.data.effects.types.MEffect;
import masp.plugins.mlight.data.player.MPlayer;
import masp.plugins.mlight.managers.MobEffectManager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.getspout.spoutapi.SpoutManager;

public class TestCommands implements CommandExecutor, Listener {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			MPlayer player = MRPG.getPlayer((Player) sender);
			if (label.equalsIgnoreCase("mattr")) {
				if (player.getSelectedMob() != null) {
					if (args.length == 2) {
						MEffect effect = MRPG.getEffect(args[0]);
						double amount = Double.parseDouble(args[1]);
						LivingEntity mob = (LivingEntity) SpoutManager.getEntityFromId(player.getSelectedMob());
						MobEffectManager meManager = MRPG.getMobEffectManager();
						if (mob != null) {
							if (meManager.hasEffects(mob)) {
								meManager.getEffects(mob).setEffectDecimal(effect, amount);
							} else {
								meManager.addMobParticipant(mob).setEffectDecimal(effect, amount);
							}
						} else {
							player.sendMessage(ChatColor.RED + "The mob you have selected no longer exists");
							return true;
						}
					} else {
						player.sendMessage(ChatColor.RED + "Incorrect number of args: Correct amount is two");
					}
				} else {
					player.sendMessage(ChatColor.RED + "Please right click a mob before you use this command");
				}
				return true;
			} else if (label.equalsIgnoreCase("effects")) {
				player.sendMessage(ChatColor.RED + "--- Attack Effects ---");
				for (MEffect effect : player.getPrimaryWeapon().getAttack().getEffects()) {
					player.sendMessage(ChatColor.RED + effect.toString() + ": " + player.getPrimaryWeapon().getAttack().getTotalEffects(effect));
				}
				player.sendMessage(ChatColor.RED + "--- Defense Effects ---");
				for (MEffect effect : player.getPrimaryWeapon().getDefense().getEffects()) {
					player.sendMessage(ChatColor.RED + effect.toString() + ": " + player.getPrimaryWeapon().getDefense().getTotalEffects(effect));
				}
				player.sendMessage(ChatColor.RED + "-- End of Effects --");
				return true;
			} else if (label.equalsIgnoreCase("mheal")) {
				
			}
		}
		return false;
	}
	
	@EventHandler
	public void onEntityInteract(PlayerInteractEntityEvent event) {
		if (event.getRightClicked() instanceof LivingEntity) {
			MPlayer player = MRPG.getPlayer(event.getPlayer());
			player.setSelectedMob(event.getRightClicked().getUniqueId());
			player.sendMessage(ChatColor.BLUE + "Selected mob successfully!");
			EffectCollection collect = MRPG.getMobManager().getCreatureEffects((LivingEntity) event.getRightClicked());
			if (collect == null) return;
			for (MEffect effect : collect.getEffects()) {
				player.sendMessage(ChatColor.GREEN + effect.getName() + ": " + collect.getTotalEffects(effect));
			}
		}
	}

}
