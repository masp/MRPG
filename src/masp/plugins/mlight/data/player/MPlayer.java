package masp.plugins.mlight.data.player;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.Settings;
import masp.plugins.mlight.Utils;
import masp.plugins.mlight.data.Attribute;
import masp.plugins.mlight.data.Damageable;
import masp.plugins.mlight.data.MPlayerInventory;
import masp.plugins.mlight.data.effects.EffectCollection;
import masp.plugins.mlight.data.effects.EffectManager;
import masp.plugins.mlight.data.effects.Effected;
import masp.plugins.mlight.data.effects.types.MEffect;
import masp.plugins.mlight.data.items.MItem;
import masp.plugins.mlight.gui.menus.PlayerHud;
import net.minecraft.server.ContainerPlayer;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Packet18ArmAnimation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.getspout.spout.inventory.SpoutCraftingInventory;
import org.getspout.spout.player.SpoutCraftPlayer;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.Label;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public class MPlayer extends SpoutCraftPlayer implements Effected, EffectCollection, Damageable {
	
	private String name;
	
	private long lastAttack;
	
	private int maxHealth;
	private int health;
	
	private int skillPoints;
	
	private double expRate = Settings.EXP_RATE;
	private double exp;
	
	private PlayerHud hud;
	
	private Label dangerLabel;
	private int dangerLevel;
	
	private MPlayerInventory inventory;
	
	private transient UUID selectedMob;
	
	private Map<Attribute, Integer> skills = new LinkedHashMap<Attribute, Integer>();
	
	public MPlayer(CraftServer server, EntityPlayer vPlayer) {
		super(server, vPlayer);
		CraftPlayer player = vPlayer.netServerHandler.getPlayer();
		this.name = player.getName();
		this.health = Settings.DEFAULT_MAX_HEALTH;
		this.maxHealth = Settings.DEFAULT_MAX_HEALTH;
		this.exp = 0D;
		dangerLabel = new GenericLabel();
		
		if (getPlayer() != null) {
			setDangerLevel(Utils.getDanger(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockZ()));
		}
		
		for (Attribute sClass : MRPG.getAttributeManager().getSkills()) {
			skills.put(sClass, 0);
		}
	}
	
	public static boolean updateBukkitEntity(Player player) {
		if (!(player instanceof MPlayer)) {
			CraftPlayer cp = (CraftPlayer) player;
			EntityPlayer ep = cp.getHandle();
			return updateBukkitEntity(ep);
		}
		return false;
	}
	
	public static boolean updateBukkitEntity(EntityPlayer ep) {
		Field bukkitEntity;
		try {
			bukkitEntity = Entity.class.getDeclaredField("bukkitEntity");
			bukkitEntity.setAccessible(true);
			org.bukkit.entity.Entity e = (org.bukkit.entity.Entity) bukkitEntity.get(ep);
			System.out.println("Entity not Null: " + (ep.getName() != null));
			System.out.println("Server not Null: " + ((CraftServer) Bukkit.getServer() != null));
			if (e == null || !e.getClass().equals(MPlayer.class)) {
				bukkitEntity.set(ep, new MPlayer((CraftServer) Bukkit.getServer(), ep));
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static MPlayer getPlayer(Player player) {
		if (player instanceof MPlayer) {
			return (MPlayer) player;
		}
		if ((((CraftPlayer) player).getHandle()).getBukkitEntity() instanceof MPlayer) {
			return (MPlayer) ((((CraftPlayer) player).getHandle()).getBukkitEntity());
		}
		// We should never get here
		//Logger.getLogger("Minecraft").warning("Player: " + player.getName() + " was not properly updated during login!");
		updateBukkitEntity(player);
		return (MPlayer) ((((CraftPlayer) player).getHandle()).getBukkitEntity());
	}
	
	public void setSelectedMob(UUID id) {
		this.selectedMob = id;
	}
	
	public UUID getSelectedMob() {
		return selectedMob;
	}
	
	public Set<Attribute> getSkills() {
		return skills.keySet();
	}
	
	public void setSkillValue(Attribute skill, int value) {
		skills.put(skill, value);
	}
	
	public int getSkillValue(String skill) {
		for (Attribute sClass : getSkills()) {
			if (sClass.getName().equalsIgnoreCase(skill)) {
				return skills.get(sClass);
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
	
	public void setExperience(double exp) {
		this.exp = exp;
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
	
	public void setExp(int exp) {
		this.exp = exp;
	}
	
	public synchronized void setHealth(int health) {
		this.health = health;
		if (getHud() != null) {
			getHud().getHealthbar().setCurrentValue(health);
		}
	}
	
	public void setMaxHealth(int health) {
		this.maxHealth = health;
		if (getHud() != null) {
			getHud().getHealthbar().setMaxValue(health);
		}
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
		
		return skills + getPrimaryWeapon().getTotalEffectsDecimal(effect) + getInventory().getTotalEffectsDecimal(effect);
	}

	@Override
	public Set<MEffect> getEffects() {
		Set<MEffect> effects = new HashSet<MEffect>();
		for (Attribute sClass : getSkills()) {
			effects.addAll(sClass.getEffects());
		}
		effects.addAll(getPrimaryWeapon().getEffects());
		effects.addAll(getInventory().getEffects());
		return effects;
	}
	
	@Override
	public MPlayerInventory getInventory() {
		if (this.inventory == null) {
			createInventory(null);
		} else if (!(this.inventory).getHandle().equals(this.getHandle().inventory)) {
			createInventory(this.inventory.getName());
		}
		return this.inventory;
	}
	
	@Override
	public void createInventory(String name) {
		if (this.getHandle().activeContainer instanceof ContainerPlayer) {
			this.inventory = new MPlayerInventory(this, this.getHandle().inventory, new SpoutCraftingInventory(((ContainerPlayer) this.getHandle().activeContainer).craftInventory, ((ContainerPlayer) this.getHandle().activeContainer).resultInventory));
			if (name != null) {
				this.inventory.setName(name);
			}
		} else {
			this.inventory = new MPlayerInventory(this, this.getHandle().inventory, new SpoutCraftingInventory(((ContainerPlayer) this.getHandle().defaultContainer).craftInventory, ((ContainerPlayer) this.getHandle().defaultContainer).resultInventory));
			if (name != null) {
				this.inventory.setName(name);
			}
		}
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

}