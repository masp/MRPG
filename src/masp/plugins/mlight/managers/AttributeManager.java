package masp.plugins.mlight.managers;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import masp.plugins.mlight.data.Attribute;

public class AttributeManager {
	
	private Map<String, Attribute> skills = new LinkedHashMap<String, Attribute>();
	
	public Attribute getSkill(String name) {
		if (skills.containsKey(name)) {
			return skills.get(name);
		}
		throw new SkillClassNotFoundException(name);
	}
	
	public boolean skillExists(String name) {
		if (skills.containsKey(name)) {
			return true;
		}
		return false;
	}
	
	public void addSkill(Attribute skill) {
		if (!skills.containsKey(skill.getName())) {
			skills.put(skill.getName(), skill);
		}
	}
	
	public void removeSkill(String name) {
		skills.remove(name);
	}
	
	public void removeSkill(Attribute skill) {
		skills.remove(skill.getName());
	}
	
	public Collection<Attribute> getSkills() {
		return skills.values();
	}
	
	private class SkillClassNotFoundException extends IllegalArgumentException {
		private static final long serialVersionUID = -7360691963419675695L;
		
		private String name;
		
		public SkillClassNotFoundException(String name) {
			this.name = name;
		}
		
		@Override
		public String getMessage() {
			return "Skill class was unable to be found by the name " + name;
		}
		
	}
	
}
