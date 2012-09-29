package masp.plugins.mlight.config;

import java.io.File;
import java.io.IOException;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.data.Attribute;
import masp.plugins.mlight.data.effects.types.MEffect;

public class SkillClassConfiguration extends Configuration {

	public SkillClassConfiguration(MRPG plugin, File dir) {
		super("skill-classes", plugin, dir);
	}

	@Override
	public void onCreate() {
		getConfig().set("skill-classes.damage.cost", 1);
		getConfig().set("skill-classes.damage.max-level", 100);
		getConfig().set("skill-classes.damage.description", "Increases The Player's Damage!");
		getConfig().set("skill-classes.damage.effects.general_damage", 5);
		try {
			getConfig().save(getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRead() {
		if (getConfig().getConfigurationSection("skill-classes") != null) {
			for (String skillName : getConfig().getConfigurationSection("skill-classes").getKeys(false)) {
				
				int cost = getConfig().getInt("skill-classes." + skillName + ".cost", 1);
				int maxLevel = getConfig().getInt("skill-classes." + skillName + ".max-level", 100);
				String description = getConfig().getString("skill-classes." + skillName + ".description", "Default skill description :/");
				Attribute skill = new Attribute(skillName, description, cost, maxLevel);
				
				if (getConfig().getConfigurationSection("skill-classes." + skillName + ".effects") != null) {
					for (String effectName : getConfig().getConfigurationSection("skill-classes." + skillName + ".effects").getKeys(false)) {
						
						MEffect effect = MRPG.getEffectManager().getEffect(effectName);
						if (effect != null) {
							String sValue = getConfig().getString("skill-classes." + skillName + ".effects." + effectName);
							
							if (sValue.contains("%")) {
								try {
									skill.setEffectPercent(effect, Double.parseDouble(sValue.replaceAll("%", "")));
								} catch (NumberFormatException ex) {
									getPlugin().getLogger().info("Configuration Error: Effect value must be a number for effect " + effectName + " for skill " + skillName);
								}
							} else {
								try {
									skill.setEffectDecimal(effect, Double.parseDouble(sValue));
								} catch (NumberFormatException ex) {
									getPlugin().getLogger().info("Configuration Error: Effect value must be a number for effect " + effectName + " for skill " + skillName);
								}
							}
						}
						
					}
				}
				
				MRPG.getAttributeManager().addSkill(skill);
			}
		}
	}

}
