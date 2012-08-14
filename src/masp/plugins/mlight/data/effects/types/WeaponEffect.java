package masp.plugins.mlight.data.effects.types;

public class WeaponEffect extends MEffect {
	
	private WeaponType type;
	private String baseName;
	
	public WeaponEffect(String name, WeaponType type) {
		super(name + "_" + type.name());
		
		this.baseName = name;
		this.type = type;
	}
	
	public WeaponType getType() {
		return type;
	}
	
	public String getBaseName() {
		return baseName;
	}
	
	public enum WeaponType {
		DEFENSE, DAMAGE;
	}
	
}
