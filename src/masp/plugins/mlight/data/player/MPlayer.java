package masp.plugins.mlight.data.player;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.Settings;
import masp.plugins.mlight.data.Attribute;
import masp.plugins.mlight.data.Damageable;
import masp.plugins.mlight.data.MPlayerInventory;
import masp.plugins.mlight.data.effects.EffectCollection;
import masp.plugins.mlight.data.effects.EffectManager;
import masp.plugins.mlight.data.effects.Effected;
import masp.plugins.mlight.data.effects.types.MEffect;
import masp.plugins.mlight.data.items.MItem;
import masp.plugins.mlight.gui.menus.PlayerHud;
import masp.plugins.mlight.utils.Utils;
import net.minecraft.server.Packet18ArmAnimation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.Label;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.gui.WidgetAnim;
import org.getspout.spoutapi.player.SpoutPlayer;

public class MPlayer implements Effected, EffectCollection, Damageable {
	
	private String name;
	
	private long lastAttack;
	
	private int maxHealth;
	private int health;
	
	private int maxMana;
	private int mana;
	
	private int maxStamina;
	private int stamina;
	
	private int skillPoints;
	
	private double expRate = Settings.EXP_RATE;
	private double exp;
	
	private transient PlayerHud hud;
	
	private transient Label dangerLabel;
	private transient int dangerLevel;
	
	private MPlayerInventory inventory;
	
	private transient UUID selectedMob;
	
	private Map<Attribute, Integer> attributes = new LinkedHashMap<Attribute, Integer>();
	
	public MPlayer(Player player) {
		this.name = player.getName();
		
		this.health = Settings.DEFAULT_MAX_HEALTH;
		this.mana = Settings.DEFAULT_MAX_MANA;
		this.stamina = Settings.DEFAULT_MAX_STAMINA;
		
		this.maxHealth = Settings.DEFAULT_MAX_HEALTH;
		this.maxMana = Settings.DEFAULT_MAX_MANA;
		this.maxStamina = Settings.DEFAULT_MAX_STAMINA;
		
		this.exp = 0D;
		dangerLabel = new GenericLabel();
		
		if (getPlayer() != null) {
			setDangerLevel(Utils.getDanger(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockZ()));
		}
		
		for (Attribute sClass : MRPG.getAttributeManager().getSkills()) {
			attributes.put(sClass, 0);
		}
		
		inventory = new MPlayerInventory(this);
	}
	
	public void setSelectedMob(UUID id) {
		this.selectedMob = id;
	}
	
	public UUID getSelectedMob() {
		return selectedMob;
	}
	
	public Set<Attribute> getSkills() {
		return attributes.keySet();
	}
	
	public void setSkillValue(Attribute skill, int value) {
		attributes.put(skill, value);
	}
	
	public int getSkillValue(String skill) {
		for (Attribute sClass : getSkills()) {
			if (sClass.getName().equalsIgnoreCase(skill)) {
				return attributes.get(sClass);
			}
		}
		return 0;
	}
	
	public String getName() {
		return name;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(name);
	}
	
	public void setHud(PlayerHud hud) {
		this.hud = hud;
	}
	
	public Label getDangerLabel() {
		return dangerLabel;
	}
	
	public PlayerHud getHud() {
		return hud;
	}
	
	public void setExperience(double exp, boolean update) {
		if (update) {
			int nLevel = Utils.getLevelByExp(exp, Settings.EXP_RATE);
			if (nLevel != getLevel() && nLevel > getLevel()) {
				setLevel(nLevel);
				levelUp(nLevel - getLevel());
			}
		}
		this.exp = exp;
		
		if (getHud() != null) {
			getHud().updateExp(this);
		}
	}
	
	public void addExp(double exp) {
		this.setExperience(getExperience() + exp, true);
	}
	
	public void levelUp(int amount) {
		this.setSkillPoints(this.getSkillPoints() + (amount * Settings.AP_PER_LEVEL));
		SpoutManager.getPlayer(this.getPlayer()).sendNotification(
				ChatColor.GREEN + "Level Up!", 
				"You are now level " + (this.getLevel() + amount) + "!", 
				Material.DIAMOND_SWORD);
	}
	
	public double getExperience() {
		return exp;
	}
	
	public synchronized int getHealth() {
		return health;
	}
	
	public synchronized int getMaxHealth() {
		return maxHealth;
	}
	
	public synchronized void setHealth(int health) {
		this.health = health;
		if (getHud() != null) {
			getHud().getHealthBar().setCurrentValue(health);
		}
	}
	
	public void setMaxHealth(int health) {
		this.maxHealth = health;
		if (getHud() != null) {
			getHud().getHealthBar().setMaxValue(health);
		}
	}
	
	public void displayNotification(final Label notification) {
		notification.setAnchor(WidgetAnchor.CENTER_CENTER).setHeight(10).setWidth(20).shiftXPos(-5).shiftYPos(-10);
		notification.animate(WidgetAnim.POS_Y, 1.f, (short) 20, (short) 1, false, false).animateStart();
		Bukkit.getScheduler().scheduleSyncDelayedTask(MRPG.getInstance(),
			new Runnable() {
				@Override
				public void run() {
					if (getPlayer() != null) {
						SpoutManager.getPlayer(getPlayer()).getMainScreen().removeWidget(notification);
					}
				}
			}, 20L
		);
		SpoutManager.getPlayer(this.getPlayer()).getMainScreen().attachWidget(MRPG.getInstance(), notification);
	}
	
	public void displaySideNotification(final Label notification) {
		
		notification.setAnchor(WidgetAnchor.TOP_RIGHT)
					.setHeight(GenericLabel.getStringHeight(notification.getText()))
					.setWidth(GenericLabel.getStringWidth(notification.getText()))
					.setY(4 + notification.getHeight())
					.setX(-5 - notification.getWidth());
		
		notification.animate(WidgetAnim.POS_Y, 1.f, (short) 20, (short) 1, false, false).animateStart();
		Bukkit.getScheduler().scheduleSyncDelayedTask(MRPG.getInstance(),
			new Runnable() {
				@Override
				public void run() {
					if (getPlayer() != null) {
						SpoutManager.getPlayer(getPlayer()).getMainScreen().removeWidget(notification);
					}
				}
			}, 20L
		);
		SpoutManager.getPlayer(this.getPlayer()).getMainScreen().attachWidget(MRPG.getInstance(), notification);
	}
	
	@Override
	public void damage(int damage, LivingEntity data) {
		if (damage <= 0) {
			return;
		}
		
		if (getHealth() - damage <= 0) {
			// Kill the player
			getPlayer().setHealth(0);
			setHealth(getMaxHealth());
			setStamina(getMaxStamina());
			setMana(getMaxMana());
			return;
		}
		
		setHealth(getHealth() - damage);
		
		sendMessage(ChatColor.BLUE + "You have received " + ChatColor.RED.toString() + damage + ChatColor.BLUE + " damage");
		
		// Add the effects
		CraftPlayer cPlayer = (CraftPlayer) getPlayer();
		((CraftWorld) getPlayer().getWorld()).getHandle().broadcastEntityEffect(cPlayer.getHandle(), (byte) 2);
		Packet18ArmAnimation bedPacket = new Packet18ArmAnimation();
		bedPacket.a = getPlayer().getEntityId();
		bedPacket.b = 3;
		cPlayer.getHandle().netServerHandler.sendPacket(bedPacket);
	}
	
	@Override
	public void onUneffected(EffectCollection participant) {
		for (MEffect effect : participant.getEffects()) {
			onUneffected(effect, participant.getTotalEffects(effect));
		}
	}
	
	@Override
	public void onUneffected(MEffect effect, double amount) {
		onEffected(effect, -amount);
	}

	@Override
	public void onEffected(EffectCollection participant) {
		for (MEffect effect : participant.getEffects()) {
			onEffected(effect, participant.getTotalEffects(effect));
		}
	}

	@Override
	public void onEffected(MEffect effect, double amount) {
		if (effect.getName().equalsIgnoreCase(EffectManager.HEALTH)) {
			this.setMaxHealth(getMaxHealth() + (int) amount);
		} else if (effect.getName().equalsIgnoreCase(EffectManager.STAMINA)) {
			this.setMaxStamina(getMaxStamina() + (int) amount);
		} else if (effect.getName().equalsIgnoreCase(EffectManager.MANA)) {
			this.setMaxMana(getMaxMana() + (int) amount);
		} else if (effect.getName().equalsIgnoreCase(EffectManager.EXP_RATE)) {
			this.setExpRate(amount);
		} else if (effect.getName().equalsIgnoreCase(EffectManager.ITEM_WEIGHT)) {
			double maxWeight = Settings.DEFAULT_MAX_WEIGHT + getTotalEffects(MRPG.getEffectManager().getEffect(EffectManager.MAX_ITEM_WEIGHT));
			double currWeight = getTotalEffects(effect);
			if (currWeight + amount >= maxWeight && currWeight >= maxWeight) {
				return;
			}
			removeSpeed(amount / maxWeight);
		}
	}
	
	public void removeSpeed(double speed) {
		SpoutPlayer player = SpoutManager.getPlayer(getPlayer());
		player.setWalkingMultiplier(player.getWalkingMultiplier() - speed < 0 ? 0 : player.getWalkingMultiplier() - speed);
		player.setAirSpeedMultiplier(player.getAirSpeedMultiplier() - speed < 0 ? 0 : player.getAirSpeedMultiplier() - speed);
		player.setSwimmingMultiplier(player.getSwimmingMultiplier() - speed < 0 ? 0 : player.getSwimmingMultiplier() - speed);
		player.setJumpingMultiplier(player.getJumpingMultiplier() - speed < 0 ? 0 : player.getJumpingMultiplier() - speed);
	}
	
	public void sendMessage(String message) {
		this.getPlayer().sendMessage(message);
	}

	public int getSkillPoints() {
		return skillPoints;
	}
	
	public void setLastAttack(long time) {
		this.lastAttack = time;
	}
	
	public long getLastAttack() {
		return lastAttack;
	}

	public void setSkillPoints(int skillPoints) {
		this.skillPoints = skillPoints;
		if (getHud() != null) {
			getHud().getSkillPoints().setText(Integer.toString(skillPoints));
		}
	}

	public double getExpRate() {
		return expRate;
	}

	public void setExpRate(double expRate) {
		this.expRate = expRate;
	}
	
	public MItem getPrimaryWeapon() {
		return MRPG.getItemManager().
				getItem(getPlayer().getItemInHand()) == null ?
						MRPG.getItemManager().getItem(new ItemStack(Material.AIR)) :
							MRPG.getItemManager().
								getItem(getPlayer().getItemInHand());
	}
	
	public int getLevel() {
		return Utils.getLevelByExp(this.getExperience(), Settings.EXP_RATE);
	}
	
	public void setLevel(int level) {
		if (getHud() != null) {
			getHud().getLevel().setText(Integer.toString(level));
		}
	}

	@Override
	public double getTotalEffects(MEffect effect) {
		return ((getTotalEffectsPercent(effect) / 100D) * getTotalEffectsDecimal(effect)) + getTotalEffectsDecimal(effect);
	}

	@Override
	public double getTotalEffectsPercent(MEffect effect) {
		double skills = 0.D;
		for (Attribute sClass : getSkills()) {
			skills += sClass.getTotalEffectsPercent(effect) * getSkillValue(sClass.getName());
		}
		
		return skills + getPrimaryWeapon().getTotalEffectsPercent(effect) + getInventory().getTotalEffectsPercent(effect);
	}

	@Override
	public double getTotalEffectsDecimal(MEffect effect) {
		double skills = 0.D;
		for (Attribute sClass : getSkills()) {
			skills += sClass.getTotalEffectsDecimal(effect) * getSkillValue(sClass.getName());
		}
		
		return skills + getPrimaryWeapon().getAttack().getTotalEffectsDecimal(effect) + getInventory().getTotalEffectsDecimal(effect);
	}

	@Override
	public Set<MEffect> getEffects() {
		Set<MEffect> effects = new HashSet<MEffect>();
		for (Attribute sClass : getSkills()) {
			effects.addAll(sClass.getEffects());
		}
		effects.addAll(getPrimaryWeapon().getAttack().getEffects());
		effects.addAll(getInventory().getEffects());
		return effects;
	}
	
	public MPlayerInventory getInventory() {
		return inventory;
	}

	public int getDangerLevel() {
		return dangerLevel;
	}

	public void setDangerLevel(int dangerLevel) {
		this.dangerLevel = dangerLevel;
		updateDangerLabel();
	}
	
	public void updateDangerLabel() {
		getDangerLabel().setText("Danger Level: " + getDangerLevel());
		getDangerLabel().setTextColor(new Color(0.9f, 0.f, 0.1f));
		getDangerLabel().setAnchor(WidgetAnchor.BOTTOM_LEFT)
						.setX(10)
						.setY(-10)
						.setWidth(GenericLabel.getStringWidth(getDangerLabel().getText()))
						.setHeight(GenericLabel.getStringHeight(getDangerLabel().getText()))
						.setFixed(true);
	}

	public int getMaxMana() {
		return maxMana;
	}

	public void setMaxMana(int maxMana) {
		this.maxMana = maxMana;
		
		if (getHud() != null) {
			getHud().getManaBar().setMaxValue((float) maxMana);
		}
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		
		if (mana > getMaxMana()) {
			mana = getMaxMana();
		}
		if (mana < 0) {
			mana = 0;
		}
		
		this.mana = mana;
		
		if (getHud() != null) {
			getHud().getManaBar().setCurrentValue((float) mana);
		}
	}

	public int getMaxStamina() {
		return maxStamina;
	}

	public void setMaxStamina(int maxStamina) {
		this.maxStamina = maxStamina;
		if (getHud() != null) {
			getHud().getStaminaBar().setMaxValue((float) maxStamina);
		}
	}

	public int getStamina() {
		return stamina;
	}

	public void setStamina(int stamina) {
		if (stamina > getMaxStamina()) {
			stamina = getMaxStamina();
		}
		if (stamina < 0) {
			stamina = 0;
		}
		
		this.stamina = stamina;
		
		if (getPlayer() != null) {
			if (getStamina() == getMaxStamina()) {
				getPlayer().setFoodLevel(20);
			} else if (((float) getStamina() / (float) getMaxStamina()) * 100.f <= Settings.STAMINA_DAMAGE_DECREASE_LIMIT) {
				getPlayer().setFoodLevel(1);
			} else {
				getPlayer().setFoodLevel(19);
			}
		}
		
		if (getHud() != null) {
			getHud().getStaminaBar().setCurrentValue((float) stamina);
		}
	}

}