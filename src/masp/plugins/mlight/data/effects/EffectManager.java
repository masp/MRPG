package masp.plugins.mlight.data.effects;

import java.util.HashSet;
import java.util.Set;

import masp.plugins.mlight.data.effects.types.DefenseResistEffect;
import masp.plugins.mlight.data.effects.types.MEffect;
import masp.plugins.mlight.exceptions.EffectNotFoundException;

public class EffectManager {
	
	private Set<MEffect> effects = new HashSet<MEffect>();
	
	public static final String MAX_HEALTH = "MAX_HEALTH",
			EXP_RATE = "EXP_RATE", 
			GENERAL_DAMAGE = "GENERAL_DAMAGE", 
			GENERAL_RESIST = "GENERAL_DEFENSE";
	
	public EffectManager() {
		effects.add(new MEffect(MAX_HEALTH));
		effects.add(new MEffect(EXP_RATE));
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
