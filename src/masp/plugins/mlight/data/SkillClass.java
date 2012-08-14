package masp.plugins.mlight.data;

import masp.plugins.mlight.data.effects.SimpleEffectParticipant;

public class SkillClass extends SimpleEffectParticipant implements Comparable<SkillClass> {
	
	private String name;
	private String description;
	
	public SkillClass(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;
		if (o instanceof SkillClass) {
			return ((SkillClass) o).getName().equalsIgnoreCase(getName());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public int compareTo(SkillClass skill) {
		return getName().compareToIgnoreCase(skill.getName());
	}
	
}
