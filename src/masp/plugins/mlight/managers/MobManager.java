package masp.plugins.mlight.managers;

import java.util.HashSet;
import java.util.Set;

import masp.plugins.mlight.data.MCreature;

public class MobManager {
	
	private Set<MCreature> creatures;
	
	public MobManager() {
		creatures = new HashSet<MCreature>();
	}
	
	public void addCreature(MCreature creature) {
		creatures.add(creature);
	}
	
	public MCreature getCreature(String name) {
		for (MCreature creature : creatures) {
			if (creature.getType().name().equalsIgnoreCase(name)) {
				return creature;
			}
		}
		throw new IllegalArgumentException("Unable to find any creature by that name");
	}
	
	public void removeCreature(String name) {
		creatures.remove(name);
	}
	
	public Set<MCreature> getCreatures() {
		return creatures;
	}
	
}
