package masp.plugins.mlight.listeners;

import java.sql.SQLException;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.Utils;
import masp.plugins.mlight.data.Attribute;
import masp.plugins.mlight.data.items.MItem;
import masp.plugins.mlight.data.player.MPlayer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.player.SpoutPlayer;

public class GeneralListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(final SpoutCraftEnableEvent event) {
		MPlayer player = MRPG.getPlayer(event.getPlayer());
		System.out.println("REACHED PLAYER NULL " + (player != null));
		for (int i = 36; i < 40; i++) {
			ItemStack armor = player.getInventory().getItem(i) == null ? 
					new ItemStack(Material.AIR, 1) : player.getInventory().getItem(i); 
			MItem item = MRPG.getItemManager().getItem(armor);
			player.getInventory().setItem(i, null);
				player.onEffected(item);
			player.getInventory().setItem(i, armor);
		}
		player.onEffected(MRPG.getItemManager().getItem(player.getItemInHand()));
		for (Attribute sClass : player.getSkills()) {
			player.onEffected(sClass);
		}
		player.setSkillPoints(999);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		if (!SpoutManager.getPlayer(event.getPlayer()).isSpoutCraftEnabled()) return;
		try {
			MRPG.getDataManager().savePlayer(MRPG.getPlayer(event.getPlayer()));
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(PlayerMoveEvent event) {
		if (event.isCancelled()) {
			return;
		}
		Location to = event.getTo();
		Location from = event.getFrom();
		MPlayer player = MRPG.getPlayer(event.getPlayer());
		if (!Utils.equals(to, from, false)) {
			if (Utils.getDanger(to.getWorld(), to.getBlockX(), to.getBlockZ()) != player.getDangerLevel()) {
				player.setDangerLevel(Utils.getDanger(to.getWorld(), to.getBlockX(), to.getBlockZ()));
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onArmorExchange(InventoryClickEvent event) {
		if (event.getSlotType() == SlotType.ARMOR) {
			SpoutPlayer player = SpoutManager.getPlayer((Player) event.getWhoClicked());
			MPlayer mPlayer = MRPG.getPlayer(player);
			if (event.getCurrentItem() == null || event.getCurrentItem().getTypeId() == 0) {
				mPlayer.onEffected(MRPG.getItemManager().getItem(event.getCursor()).getDefense());
			} else if (event.getCursor() == null || event.getCursor().getTypeId() == 0){
				mPlayer.onUneffected(MRPG.getItemManager().getItem(event.getCurrentItem()).getDefense());
			} else {
				mPlayer.onUneffected(MRPG.getItemManager().getItem(event.getCurrentItem()).getDefense());
				mPlayer.onEffected(MRPG.getItemManager().getItem(event.getCursor()).getDefense());
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryOpen(InventoryOpenEvent event) {
		MRPG.getInstance().getLogger().info("REACHED");
	}
}
