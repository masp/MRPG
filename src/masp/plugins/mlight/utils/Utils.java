package masp.plugins.mlight.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.Settings;
import masp.plugins.mlight.data.effects.EffectCollection;
import masp.plugins.mlight.data.effects.EffectManager;
import masp.plugins.mlight.data.effects.EffectParticipant;
import masp.plugins.mlight.data.effects.SimpleEffectParticipant;
import masp.plugins.mlight.data.effects.types.MEffect;
import masp.plugins.mlight.data.effects.types.WeaponEffect;
import masp.plugins.mlight.data.effects.types.WeaponEffect.WeaponType;
import masp.plugins.mlight.data.player.MPlayer;
import masp.plugins.mlight.data.player.Title;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.google.common.base.Joiner;

public class Utils {
	
	private static final double KNOCKBACK = 0.3434159f;
	
	/**
	 * @return The total damage that all the damage effects return
	 */
	public static int getTotalDamage(EffectCollection sender, EffectCollection receiver) {
		HashMap<WeaponEffect, Integer> receivers = new HashMap<WeaponEffect, Integer>();
		HashMap<WeaponEffect, Integer> damagers = new HashMap<WeaponEffect, Integer>();
		
		for (MEffect effect : sender.getEffects()) {
			if (effect instanceof WeaponEffect) {
				WeaponEffect wEffect = (WeaponEffect) effect;
				if (wEffect.getType() == WeaponType.DAMAGE) {
					damagers.put(wEffect, (int) sender.getTotalEffects(wEffect));
					// Sanity check
					receivers.put((WeaponEffect) MRPG.getEffect(wEffect.getBaseName() + "_DEFENSE"), 0);
				}
			}
		}
	
		for (MEffect effect : receiver.getEffects()) {
			if (effect instanceof WeaponEffect) {
				WeaponEffect wEffect = (WeaponEffect) effect;
				if (wEffect.getType() == WeaponType.DEFENSE) {
					receivers.put(wEffect, (int) receiver.getTotalEffects(wEffect));
				}
			}
		}
		
		int totalDamage = 0;
		for (WeaponEffect effect : damagers.keySet()) {
			totalDamage += 
				(damagers.get(effect) - receivers.get(MRPG.getEffect(effect.getBaseName() + "_DEFENSE")));
		}
		
		// We don't allow negative damage
		return totalDamage < 0 ? 0 : totalDamage;
	}
	
	public static int getTotalDamage(EffectCollection mainSender, EffectCollection receiver, List<EffectCollection> senders) {
		HashMap<WeaponEffect, Integer> receivers = new HashMap<WeaponEffect, Integer>();
		HashMap<WeaponEffect, Integer> damagers = new HashMap<WeaponEffect, Integer>();
		
		senders.add(mainSender);
		
		for (EffectCollection sender : senders) {
			for (MEffect effect : sender.getEffects()) {
				if (effect instanceof WeaponEffect) {
					WeaponEffect wEffect = (WeaponEffect) effect;
					if (wEffect.getType() == WeaponType.DAMAGE) {
						damagers.put(wEffect, (int) sender.getTotalEffects(wEffect) + (damagers.get(wEffect) == null ? 0 : damagers.get(wEffect)));
						// Sanity check
						receivers.put(wEffect, 0);
					}
				}
			}
		}
	
		for (MEffect effect : receiver.getEffects()) {
			if (effect instanceof WeaponEffect) {
				WeaponEffect wEffect = (WeaponEffect) effect;
				if (wEffect.getType() == WeaponType.DEFENSE) {
					receivers.put(wEffect, (int) receiver.getTotalEffects(wEffect));
				}
			}
		}
		
		int totalDamage = 0;
		for (WeaponEffect effect : damagers.keySet()) {
			totalDamage += 
					(damagers.get(effect) - receivers.get(MRPG.getEffect(effect.getBaseName() + "_DEFENSE")));
		}
		
		// We don't allow negative damage
		return totalDamage < 0 ? 0 : totalDamage;
	}
	
	public static void knockback(EffectCollection effects, LivingEntity knocker, LivingEntity knocked) {
		Vector dir = knocker.getLocation().getDirection().normalize();
		dir.divide(new Vector(2, 0, 2));
		dir.setY(KNOCKBACK);
		knocked.setVelocity(dir);
	}
	
	public static String shorten(double num) {
		String string = Double.toString(num);
		if (Integer.parseInt(string.split("\\.")[1]) == 0) {
			return string.split("\\.")[0];
		} else {
			DecimalFormat format = new DecimalFormat("0.##");
			return format.format(num);
		}
	}
	
	public static String getNiceName(String badName) {
		badName = badName.toLowerCase();
		badName = badName
				.replaceAll("_", " ");
		List<String> capitalized = new ArrayList<String>();
		for (String split : badName.split(" ")) {
			capitalized.add(StringUtils.capitalize(split));
		}
		Joiner joiner = Joiner.on(" ").skipNulls();
		return joiner.join(capitalized);
	}
	
	public static int getDanger(World world, int x, int z) {
		int compX = world.getSpawnLocation().getBlockX();
		int compZ = world.getSpawnLocation().getBlockZ();
		int dist = (int) Math.sqrt(Math.pow(x - compX, 2) + Math.pow(z - compZ, 2));
		return (dist / Settings.DANGER_DISTANCE) + 1; // Prevents having a danger level of 0.
	}
	
	public static float positive(float num) {
		return num < 0 ? 0 : num;
	}
	
	public static double average(double... args) {
		double total = 0D;
		for (double value : args) {
			total += value;
		}
		return total / args.length;
	}
	
	public static EffectParticipant deepCopy(EffectParticipant object) {
		EffectParticipant clone = new SimpleEffectParticipant();
		for (MEffect effect : object.getEffects()) {
			clone.setEffectDecimal(effect, object.getTotalEffectsDecimal(effect));
			clone.setEffectPercent(effect, object.getTotalEffectsPercent(effect));
		}
		return clone;
	}
	
	public static void addMobEffects(LivingEntity entity, int dangerLevel) {
		if (MRPG.getMobEffectManager().hasEffects(entity)) return;
		EffectParticipant participant = MRPG.getMobEffectManager().addMobParticipant(entity);
		participant.setEffectDecimal(MRPG.getEffect(EffectManager.HEALTH), (dangerLevel - 1) * Settings.DANGER_HEALTH_INCREASE);
		participant.setEffectDecimal(MRPG.getEffect(EffectManager.GENERAL_DAMAGE), (dangerLevel - 1) * Settings.DANGER_DAMAGE_INCREASE);
	}
	
	public static boolean equals(Location loc, Location loc2, boolean height) {
		if (height) {
			return  (loc.getBlockX() == loc2.getBlockX()) &&
					(loc.getBlockY() == loc2.getBlockY()) &&
					(loc.getBlockZ() == loc2.getBlockZ());
		} else {
			return  (loc.getBlockX() == loc2.getBlockX()) &&
					(loc.getBlockZ() == loc2.getBlockZ());
		}
	}
	
	public static int getExpTexture(MPlayer player) {
		double baseExp = Utils.getExpByLevel(player.getLevel(), Settings.EXP_RATE);
		double plExp = player.getExperience() - baseExp;
		double maxExp = Utils.getExpByLevel(player.getLevel() + 1, Settings.EXP_RATE);
		double diffProp = maxExp - baseExp;
		return Math.min(50, Math.max(0, (int) Math.floor((plExp * 50) / diffProp)));
	}
	
	public static int getLevelByExp(double exp, double exp_rate) {
		int level = 1;
		if (exp == 0) {
			return 1;
		}
		while (level <= 9999) { // Hard-coded max level to prevent expensive computations and unnecessary overflow.
			int nExp = Utils.getExpByLevel(level, exp_rate);
			if (nExp > exp) {
				return level - 1;
			}
			level++;
		}
		return level;
	}
	
	public static String joinTitles(List<Title> titles, String separator) {
		if (titles.size() == 0) return "";
		StringBuilder builder = new StringBuilder();
		for (Title title : titles) {
			builder.append(title.getTitle() + separator);
		}
		builder.delete(builder.length() - separator.length(), builder.length());
		return builder.toString();
	}
	
	public static int getExpByLevel(int level, double exp_rate) {
		int sumExp = 0;
		for (int i = 1; i < level; i++) {
			sumExp += Math.floor(i + exp_rate * Math.pow(2.D, ((double) i) / 7.D));
		}
		return (int) Math.floor(sumExp / 4);
	}
	
	public static void printInventory(Player player) {
		for (ItemStack item : player.getInventory().getContents()) {
			if (item != null && item.getTypeId() != 0) {
				player.sendMessage(ChatColor.RED + "Item Stack: " + item.getType());
			}
		}
	}
	
}
