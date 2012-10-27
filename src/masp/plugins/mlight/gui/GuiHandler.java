package masp.plugins.mlight.gui;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.data.player.MPlayer;
import masp.plugins.mlight.gui.menus.AttributeMenu;
import masp.plugins.mlight.gui.menus.MCustomInventory;
import masp.plugins.mlight.gui.menus.PlayerHud;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.event.input.KeyPressedEvent;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.gui.ContainerType;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;
import org.getspout.spoutapi.player.SpoutPlayer;

public class GuiHandler implements BindingExecutionDelegate, Listener {

	@Override
	public void keyPressed(KeyBindingEvent event) {
		// STUB
	}

	@Override
	public void keyReleased(KeyBindingEvent event) {
		if (event.getPlayer().getActiveScreen() == ScreenType.GAME_SCREEN) {
			if (event.getBinding().getId().equalsIgnoreCase("attribute-menu-button")) {
				AttributeMenu aMenu = new AttributeMenu(event.getPlayer());
				event.getPlayer().getMainScreen().attachPopupScreen(aMenu);
				return;
			}
		}
	}
	
	@EventHandler
	public void onKeyPress(KeyPressedEvent event) { 
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
			return;
		}
		if (event.getKey().equals(event.getPlayer().getInventoryKey())) {
			SpoutPlayer player = event.getPlayer();
			if (player.getActiveScreen() == ScreenType.GAME_SCREEN || player.getActiveScreen() == ScreenType.PLAYER_INVENTORY) {
				final MCustomInventory inv = new MCustomInventory(MRPG.getPlayer(player));
				inv.makeGui();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(SpoutCraftEnableEvent event) {
		final SpoutPlayer sPlayer = SpoutManager.getPlayer(event.getPlayer());
		PlayerHud hud = new PlayerHud(MRPG.getInstance(), sPlayer);
		hud.setLayout(ContainerType.OVERLAY)
		   .setAnchor(WidgetAnchor.TOP_LEFT)
		   .setX(10)
		   .setY(10)
		   .setWidth(100)
		   .setHeight(100)
		   .setFixed(true);
		final MPlayer mPlayer = MRPG.getPlayer(sPlayer);
		mPlayer.setHud(hud);
		sPlayer.getMainScreen().attachWidgets(MRPG.getInstance(), hud, mPlayer.getDangerLabel());
	}
	
	@EventHandler
	public void updateDanger(PlayerRespawnEvent event) {
		MPlayer player = MRPG.getPlayer(event.getPlayer());
		player.setDangerLevel(1);
	}

}
