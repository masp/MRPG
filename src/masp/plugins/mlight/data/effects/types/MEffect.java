package masp.plugins.mlight.data.effects.types;


public class MEffect implements Comparable<MEffect> {
	private String name;
	
	public MEffect(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;
		if (o instanceof MEffect) {
			return ((MEffect) o).getName().equalsIgnoreCase(getName());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public int compareTo(MEffect effect) {
		return getName().compareToIgnoreCase(effect.getName());
	}
}