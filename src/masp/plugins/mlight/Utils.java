package masp.plugins.mlight;

import java.util.HashMap;
import java.util.List;

import masp.plugins.mlight.data.effects.EffectCollection;
import masp.plugins.mlight.data.effects.types.MEffect;
import masp.plugins.mlight.data.effects.types.WeaponEffect;
import masp.plugins.mlight.data.effects.types.WeaponEffect.WeaponType;

import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

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
					receivers.put(wEffect, 0);
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
					(damagers.get(effect) - receivers.get(effect));
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
					(damagers.get(effect) - receivers.get(effect));
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
	
}
