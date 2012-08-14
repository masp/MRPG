package masp.plugins.mlight.data.effects;

import java.util.Set;

import masp.plugins.mlight.data.effects.types.MEffect;

public interface EffectCollection {
	
	public double getTotalEffects(MEffect effect);
	
	public double getTotalEffectsPercent(MEffect effect);
	
	public double getTotalEffectsDecimal(MEffect effect);
	
	public Set<MEffect> getEffects();
	
}
