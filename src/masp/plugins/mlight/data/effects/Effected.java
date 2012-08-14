package masp.plugins.mlight.data.effects;

import masp.plugins.mlight.data.effects.types.MEffect;

public interface Effected {
	
	public void onEffected(EffectParticipant participant);
	
	public void onEffected(MEffect effect, double amount);
	
}
