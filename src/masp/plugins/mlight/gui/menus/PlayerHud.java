package masp.plugins.mlight.gui.menus;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.data.player.MPlayer;
import masp.plugins.mlight.gui.widgets.GenericBar;
import masp.plugins.mlight.utils.Data;
import masp.plugins.mlight.utils.Utils;

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
	
	private GenericBar healthBar;
	private GenericBar manaBar;
	private GenericBar staminaBar;
	
	private Label level;
	
	private Texture currExp;
	
	private Texture bgExp;
	
	private Texture bgAp;
	
	private Label apText;
	
	public PlayerHud(final MRPG plugin, final SpoutPlayer player) {
		final MPlayer mPlayer = MRPG.getPlayer(player);
		
		int backHeight = 166 / 3;
		int backWidth = 337 / 3;
		
		this.setLayout(ContainerType.OVERLAY)
			.setWidth(backWidth)
			.setHeight(backHeight)
			.setAnchor(WidgetAnchor.BOTTOM_LEFT);
		
		//level = new GenericLabel("1");
		
		bgExp = new GenericTexture("mrpg_hud_3_label.png");
		bgExp.setAnchor(WidgetAnchor.TOP_LEFT)
			 .setFixed(true)
			 .setWidth(54)
			 .setHeight(50);
		
		currExp = new GenericTexture("exp_bar_" + Utils.getExpTexture(mPlayer) + ".png");
		currExp.setAnchor(WidgetAnchor.TOP_LEFT)
			   .setFixed(true)
			   .setWidth(40)
			   .setHeight(40)
			   .setMarginLeft(7)
			   .setMarginTop(5)
			   .setPriority(RenderPriority.Low);
		
		level = new GenericLabel(Integer.toString(mPlayer.getLevel()));
		level.setFixed(true)
			 .setAnchor(WidgetAnchor.TOP_LEFT)
			 .setWidth(GenericLabel.getStringWidth(level.getText()))
			 .setHeight(GenericLabel.getStringHeight(level.getText()))
			 .setMarginLeft(currExp.getMarginLeft() + (currExp.getWidth() / 2 - level.getWidth() / 2))
			 .setMarginTop(currExp.getMarginTop() + (currExp.getHeight() / 2 - level.getHeight() / 2))
			 .setPriority(RenderPriority.Lowest);
		
		bgAp = new GenericTexture(Data.HUD_BUBBLE);
		bgAp.setFixed(true)
			.setWidth(16)
			.setHeight(16)
			.setAnchor(WidgetAnchor.TOP_LEFT)
			.setMarginLeft(bgExp.getWidth() - 12)
			.setMarginTop(bgExp.getHeight() - 12)
			.setPriority(RenderPriority.Low);
		
		apText = new GenericLabel(Integer.toString(mPlayer.getSkillPoints()));
		apText.setFixed(true)
			  .setWidth(GenericLabel.getStringWidth(apText.getText()))
			  .setHeight(GenericLabel.getStringHeight(apText.getText()))
			  .setMarginLeft(bgAp.getMarginLeft() + bgAp.getWidth() / 2 - apText.getWidth() / 2)
			  .setMarginTop(bgAp.getMarginTop() + bgAp.getHeight() / 2 - apText.getHeight() / 2)
			  .setPriority(RenderPriority.Lowest);
		
		
		healthBar = new GenericBar("http://imageshack.us/a/img96/2773/wipreddepleted001.png",
								   "http://imageshack.us/a/img571/9848/wipred001.png",
								   100, 8, new Color(255, 255, 255));

		healthBar.setMaxValue(mPlayer.getMaxHealth());
		healthBar.setValue(mPlayer.getHealth());
		
		healthBar.setFixed(true)
				 .setAnchor(WidgetAnchor.TOP_LEFT)
				 .setMarginLeft(bgExp.getWidth() - 5)
				 .setMarginTop(14);
		
		manaBar = new GenericBar("http://imageshack.us/a/img443/806/wipbluedepleted001.png",
				   				 "http://imageshack.us/a/img22/4250/wipblue001.png",
				   				 100, 8, new Color(255, 255, 255));
		
		manaBar.setFixed(true)
			   .setAnchor(WidgetAnchor.TOP_LEFT)
			   .setMarginLeft(bgExp.getWidth() - 5)
			   .setMarginTop(healthBar.getMarginTop() + healthBar.getHeight() + 1);
		
		manaBar.setMaxValue(mPlayer.getMaxMana());
		manaBar.setValue(mPlayer.getMaxMana());
		
		
		staminaBar = new GenericBar("http://imageshack.us/a/img502/5195/wipgolddepleted001.png",
				   					"http://imageshack.us/a/img843/3645/wipgold001.png",
				   					 100, 8, new Color(255, 255, 255));
		
		staminaBar.setMaxValue(mPlayer.getMaxStamina());
		staminaBar.setValue(mPlayer.getStamina());
		
		staminaBar.setFixed(true)
			   .setAnchor(WidgetAnchor.TOP_LEFT)
			   .setMarginLeft(bgExp.getWidth() - 5)
			   .setMarginTop(manaBar.getMarginTop() + manaBar.getHeight() + 1);
		
		addChildren(healthBar, manaBar, staminaBar, bgExp, currExp, level, apText, bgAp);
	}
	
	public void updateExp(MPlayer player) {
		int pos = Utils.getExpTexture(player);
		currExp.setUrl("exp_bar_" + pos + ".png");
	}
	
	public Label getLevel() {
		return level;
	}
	
	public Label getSkillPoints() {
		return apText;
	}
	
	public GenericBar getStaminaBar() {
		return staminaBar;
	}
	
	public GenericBar getManaBar() {
		return manaBar;
	}
	
	public GenericBar getHealthBar() {
		return healthBar;
	}
	
}
