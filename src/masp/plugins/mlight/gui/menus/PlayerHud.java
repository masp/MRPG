package masp.plugins.mlight.gui.menus;

import masp.plugins.mlight.Data;
import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.data.player.MPlayer;
import masp.plugins.mlight.gui.widgets.Bar;
import masp.plugins.mlight.gui.widgets.GenericBar;

import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.ContainerType;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.Label;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.Texture;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PlayerHud extends GenericContainer {
	
	private Bar healthBar;
	private Texture background;
	private Label pName;
	private Label sPoints;
	//private Texture face;
	
	public PlayerHud(final MRPG plugin, final SpoutPlayer player) {
		final MPlayer mPlayer = MRPG.getPlayer(player);
		
		int backHeight = 166 / 3;
		int backWidth = 337 / 3;
		
		this.setLayout(ContainerType.OVERLAY)
			.setWidth(backWidth)
			.setHeight(backHeight);
		
		pName = new GenericLabel(player.getName());
		pName.setTextColor(new Color(139, 90, 43))
			 .setFixed(true)
			 .setMarginLeft(8)
			 .setMarginTop(5)
			 .setWidth(GenericLabel.getStringWidth(pName.getText()))
			 .setHeight(GenericLabel.getStringHeight(pName.getText()));
		
		sPoints = new GenericLabel(Integer.toString(mPlayer.getSkillPoints()));
		sPoints.setTextColor(new Color(0, 100, 0))
			   .setFixed(true)
			   .setMarginLeft(backWidth - 20)
			   .setMarginTop(5)
			   .setWidth(GenericLabel.getStringWidth(sPoints.getText()))
			   .setHeight(GenericLabel.getStringHeight(sPoints.getText()));
		
		background = new GenericTexture(Data.HUD_BACKGROUND);
		background.setPriority(RenderPriority.Highest)
				  .setAnchor(WidgetAnchor.TOP_LEFT)
				  .setWidth(backWidth)
				  .setHeight(backHeight)
				  .setFixed(true);
				
		/*face = new GenericTexture(Data.FACE_TEXTURE
									  .replaceAll("%name%", player.getName()));
		face.setAnchor(WidgetAnchor.TOP_LEFT)
			.setMarginLeft(5)
			.setMarginTop(5)
			.setWidth(16)
			.setHeight(16);*/
		
		
		healthBar = new GenericBar(mPlayer.getMaxHealth(), mPlayer.getHealth());
		healthBar.getGradient()
				 .setTopColor(new Color(208, 0, 0))
		 		 .setBottomColor(new Color(128, 0, 0))
		 		 .setWidth(100)
		 		 .setHeight(8);
		
		healthBar.setFixed(true)
				 .setAnchor(WidgetAnchor.TOP_LEFT)
				 .setWidth(100)
				 .setHeight(8)
				 .setMarginTop(18)
				 .setMarginLeft(6);
		
		healthBar.init();
		
		addChildren(healthBar, background, pName, sPoints);
	}
	
	public Label getSkillPoints() {
		return sPoints;
	}
	
	public Bar getHealthbar() {
		return healthBar;
	}
	
}
