package masp.plugins.mlight.exceptions;

public class EffectNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 2509621052021832830L;
	
	private String effect;
	
	public EffectNotFoundException(String effect) {
		this.effect = effect;
	}
	
	public String getEffect() {
		return effect;
	}
	
	@Override
	public String getMessage() {
		return "Effect was unable to be found: " + getEffect();
	}

}
