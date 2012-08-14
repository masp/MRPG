package masp.plugins.mlight.data.player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import masp.plugins.mlight.MLight;
import masp.plugins.mlight.Settings;
import masp.plugins.mlight.data.Damageable;
import masp.plugins.mlight.data.MItem;
import masp.plugins.mlight.data.SkillClass;
import masp.plugins.mlight.data.effects.EffectManager;
import masp.plugins.mlight.data.effects.EffectParticipant;
import masp.plugins.mlight.data.effects.EffectCollection;
import masp.plugins.mlight.data.effects.Effected;
import masp.plugins.mlight.data.effects.types.MEffect;
import net.minecraft.server.Packet18ArmAnimation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class MPlayer implements Effected, EffectCollection, Damageable {
	
	private String name;
	
	private long lastAttack;
	
	private int maxHealth;
	private int health;
	
	private ArmorParticipant armor;
	
	private int skillPoints;
	
	private double expRate = Settings.EXP_RATE;
	private double exp;
	
	private Map<SkillClass, Integer> skills = new HashMap<SkillClass, Integer>();
	
	public MPlayer(Player player) {
		this.name = player.getName();
		this.health = Settings.DEFAULT_MAX_HEALTH;
		this.maxHealth = Settings.DEFAULT_MAX_HEALTH;
		this.exp = 0D;
		armor = new ArmorParticipant(player.getName());
	}
	
	public ArmorParticipant getArmor() {
		return armor;
	}
	
	public Set<SkillClass> getSkills() {
		return skills.keySet();
	}
	
	public int getSkillValue(String skill) {
		for (SkillClass sClass : getSkills()) {
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
	}
	
	public void setMaxHealth(int health) {
		this.maxHealth = health;
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
		
		int convertedHealth = (getHealth() * 20) / getMaxHealth();
		this.getPlayer().setHealth(convertedHealth);
		
		sendMessage(ChatColor.BLUE + "You have received " + ChatColor.RED.toString() + damage + ChatColor.BLUE + " damage");
		
		// Add the effects
		CraftPlayer cPlayer = (CraftPlayer) getPlayer();
		Packet18ArmAnimation bedPacket = new Packet18ArmAnimation();
		bedPacket.a = getPlayer().getEntityId();
		bedPacket.b = 3;
		cPlayer.getHandle().netServerHandler.sendPacket(bedPacket);
		Packet18ArmAnimation damagePacket = new Packet18ArmAnimation();
		damagePacket.a = getPlayer().getEntityId();
		damagePacket.b = 2;
		cPlayer.getHandle().netServerHandler.sendPacket(damagePacket);
		for (Entity entity : getPlayer().getNearbyEntities(100D, 100D, 100D)) {
			if (entity instanceof Player) {
				((CraftPlayer) ((Player) entity)).getHandle().netServerHandler.sendPacket(damagePacket);
			}
		}
	}

	@Override
	public void onEffected(EffectParticipant participant) {
		for (MEffect effect : participant.getEffects()) {
			onEffected(effect, participant.getTotalEffects(effect));
		}
	}

	@Override
	public void onEffected(MEffect effect, double amount) {
		if (effect.getName().equalsIgnoreCase(EffectManager.MAX_HEALTH)) {
			this.setMaxHealth(getMaxHealth() + (int) amount);
		} else if (effect.getName().equalsIgnoreCase(EffectManager.EXP_RATE)) {
			this.setExpRate(amount);
		}
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
	}

	public double getExpRate() {
		return expRate;
	}

	public void setExpRate(double expRate) {
		this.expRate = expRate;
	}
	
	public MItem getPrimaryWeapon() {
		return MLight.getInstance().getItemManager().
				getItem(getPlayer().getItemInHand().getTypeId()) == null ?
						MLight.getInstance().getItemManager().getItem(0) :
							MLight.getInstance().getItemManager().
							getItem(getPlayer().getItemInHand().getTypeId());
	}

	@Override
	public double getTotalEffects(MEffect effect) {
		double skills = 0.D;
		for (SkillClass sClass : getSkills()) {
			skills += sClass.getTotalEffects(effect);
		}
		
		return skills + getPrimaryWeapon().getAttack().getTotalEffects(effect) + getArmor().getTotalEffects(effect);
	}

	@Override
	public double getTotalEffectsPercent(MEffect effect) {
		double skills = 0.D;
		for (SkillClass sClass : getSkills()) {
			skills += sClass.getTotalEffectsPercent(effect);
		}
		
		return skills + getPrimaryWeapon().getTotalEffectsPercent(effect) + getArmor().getTotalEffectsPercent(effect);
	}

	@Override
	public double getTotalEffectsDecimal(MEffect effect) {
		double skills = 0.D;
		for (SkillClass sClass : getSkills()) {
			skills += sClass.getTotalEffectsDecimal(effect);
		}
		
		return skills + getPrimaryWeapon().getTotalEffectsDecimal(effect) + getArmor().getTotalEffectsDecimal(effect);
	}

	@Override
	public Set<MEffect> getEffects() {
		Set<MEffect> effects = new HashSet<MEffect>();
		for (SkillClass sClass : getSkills()) {
			effects.addAll(sClass.getEffects());
		}
		effects.addAll(getPrimaryWeapon().getEffects());
		effects.addAll(getArmor().getEffects());
		return effects;
	}

}