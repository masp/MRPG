package masp.plugins.mlight.data.effects;

import masp.plugins.mlight.data.effects.types.MEffect;

public interface Effected {
	
	public void onEffected(EffectCollection participant);
	
	public void onEffected(MEffect effect, double amount);
	
	public void onUneffected(EffectCollection participant);
	
	public void onUneffected(MEffect effect, double amount);
	
}
