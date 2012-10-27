package masp.plugins.mlight.data.effects;

import java.util.HashSet;
import java.util.Set;

import masp.plugins.mlight.data.effects.types.DefenseResistEffect;
import masp.plugins.mlight.data.effects.types.MEffect;
import masp.plugins.mlight.exceptions.EffectNotFoundException;

public class EffectManager {
	
	private Set<MEffect> effects = new HashSet<MEffect>();
	
	public static final String
			EXP_RATE = "EXP_RATE", 
			GENERAL_DAMAGE = "GENERAL_DAMAGE", 
			GENERAL_RESIST = "GENERAL_DEFENSE",
			ITEM_WEIGHT = "ITEM_WEIGHT",
			MAX_ITEM_WEIGHT = "MAX_ITEM_WEIGHT",
			HEALTH = "HEALTH",
			MANA = "MANA",
			STAMINA = "STAMINA",
			STAMINA_HIT_EFFICIENCY = "STAMINA_HIT_EFFICIENCY",
			STAMINA_DAMAGE_EFFICIENCY = "STAMINA_DAMAGE_EFFICIENCY",
			CRITICAL_HIT_CHANCE = "CRITICAL_HIT_CHANCE",
			BLOCKING_CHANCE = "BLOCKING_CHANCE",
			HEALTH_REGEN_AMOUNT = "HEALTH_REGEN_AMOUNT",
			WALK_SPEED = "WALK_SPEED",
			SWIM_SPEED = "SWIM_SPEED",
			JUMP_HEIGHT = "JUMP_HEIGHT",
			JUMP_DISTANCE = "JUMP_DISTANCE";
	
	public EffectManager() {
		effects.add(new MEffect(EXP_RATE));
		effects.add(new MEffect(ITEM_WEIGHT));
		effects.add(new MEffect(MAX_ITEM_WEIGHT));
		effects.add(new MEffect(HEALTH));
		effects.add(new MEffect(CRITICAL_HIT_CHANCE));
		effects.add(new MEffect(HEALTH_REGEN_AMOUNT));
		effects.add(new MEffect(WALK_SPEED));
		effects.add(new MEffect(SWIM_SPEED));
		effects.add(new MEffect(JUMP_HEIGHT));
		effects.add(new MEffect(JUMP_DISTANCE));
		effects.add(new MEffect(MANA));
		effects.add(new MEffect(STAMINA));
		effects.add(new MEffect(STAMINA_HIT_EFFICIENCY));
		effects.add(new MEffect(STAMINA_DAMAGE_EFFICIENCY));
		DefenseResistEffect general = new DefenseResistEffect("GENERAL");
		effects.add(general.getDamage());
		effects.add(general.getResist());
	}
	
	public Set<MEffect> getEffects() {
		return effects;
	}
	
	public MEffect getEffect(String name) throws EffectNotFoundException {
		for (MEffect effect : effects) {
			if (effect.getName().equalsIgnoreCase(name)) {
				return effect;
			}
		}
		throw new EffectNotFoundException(name);
	}
	
	public boolean addEffect(MEffect effect) {
		return effects.add(effect);
	}
	
}
