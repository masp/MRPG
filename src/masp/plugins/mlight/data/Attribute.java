package masp.plugins.mlight.data;

import masp.plugins.mlight.data.effects.SimpleEffectParticipant;

public class Attribute extends SimpleEffectParticipant implements Comparable<Attribute> {
	
	private String name;
	private String description;
	private int cost;
	
	private int maxLevel;
	
	public Attribute(String name, String description, int cost, int maxLevel) {
		this.name = name;
		this.description = description;
		this.cost = cost;
		this.maxLevel = maxLevel;
	}
	
	public int getCost() {
		return cost;
	}
	
	public int getMaxLevel() {
		return maxLevel;
	}
	
	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
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
		if (o instanceof Attribute) {
			return ((Attribute) o).getName().equalsIgnoreCase(getName());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public int compareTo(Attribute skill) {
		return getName().compareToIgnoreCase(skill.getName());
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
