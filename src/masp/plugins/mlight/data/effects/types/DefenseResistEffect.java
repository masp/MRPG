package masp.plugins.mlight.data.effects.types;

import masp.plugins.mlight.data.effects.types.WeaponEffect.WeaponType;


public class DefenseResistEffect {
	
	private WeaponEffect damage;
	private WeaponEffect resist;
	
	private String name;
	
	public DefenseResistEffect(String name) {
		this.name = name;
		
		damage = new WeaponEffect(name, WeaponType.DAMAGE);
		resist = new WeaponEffect(name, WeaponType.DEFENSE);
	}
	
	public String getName() {
		return name;
	}
	
	public WeaponEffect getResist() {
		return resist;
	}
	
	public WeaponEffect getDamage() {
		return damage;
	}
}
