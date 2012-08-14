package masp.plugins.mlight.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import masp.plugins.mlight.MLight;
import masp.plugins.mlight.data.MCreature;
import masp.plugins.mlight.data.effects.types.MEffect;

import org.bukkit.entity.EntityType;

public class MobConfiguration extends Configuration {
	
	public static Map<String, Integer> mobHealths = new HashMap<String, Integer>();
	public static Map<String, Integer> mobDamages = new HashMap<String, Integer>();
	
	static {
		mobHealths.put("CREEPER", 20);
		mobDamages.put("CREEPER", 50);
		mobHealths.put("SKELETON", 20);
		mobDamages.put("SKELETON", 4);
		mobHealths.put("SPIDER", 16);
		mobDamages.put("SPIDER", 3);
		mobHealths.put("GIANT", 100);
		mobDamages.put("GIANT", 17);
		mobHealths.put("ZOMBIE", 20);
		mobDamages.put("ZOMBIE", 6);
		mobHealths.put("SLIME", 1);
		mobDamages.put("SLIME", 1);
		mobHealths.put("GHAST", 10);
		mobDamages.put("GHAST", 20);
		mobHealths.put("PIG_ZOMBIE", 20);
		mobDamages.put("PIG_ZOMBIE", 7);
		mobHealths.put("ENDERMAN", 40);
		mobDamages.put("ENDERMAN", 10);
		mobHealths.put("CAVE_SPIDER", 12);
		mobDamages.put("CAVE_SPIDER", 2);
		mobHealths.put("SILVERFISH", 8);
		mobDamages.put("SILVERFISH", 1);
		mobHealths.put("BLAZE", 20);
		mobDamages.put("BLAZE", 9);
		mobHealths.put("MAGMA_CUBE", 1);
		mobDamages.put("MAGMA_CUBE", 1);
		mobHealths.put("ENDER_DRAGON", 200);
		mobDamages.put("ENDER_DRAGON", 10);
		mobHealths.put("PIG", 10);
		mobDamages.put("PIG", 0);
		mobHealths.put("SHEEP", 8);
		mobDamages.put("SHEEP", 0);
		mobHealths.put("COW", 10);
		mobDamages.put("COW", 0);
		mobHealths.put("CHICKEN", 4);
		mobDamages.put("CHICKEN", 0);
		mobHealths.put("SQUID", 10);
		mobDamages.put("SQUID", 0);
		mobHealths.put("WOLF", 8);
		mobDamages.put("WOLF", 4);
		mobHealths.put("MUSHROOM_COW", 10);
		mobDamages.put("MUSHROOM_COW", 0);
		mobHealths.put("SNOWMAN", 6);
		mobDamages.put("SNOWMAN", 0);
		mobHealths.put("OCELOT", 10);
		mobDamages.put("OCELOT", 0);
		mobHealths.put("IRON_GOLEM", 100);
		mobDamages.put("IRON_GOLEM", 21);
		mobHealths.put("VILLAGER", 20);
		mobDamages.put("VILLAGER", 0);
	}
	
	public MobConfiguration(MLight plugin, File dir) {
		super("mobinfo", plugin, dir);
	}

	@Override
	public void onCreate() {
		for (EntityType type : EntityType.values()) {
			if (type.isAlive() && !(type.name().equalsIgnoreCase("player"))) {
				getConfig().set("mobs." + type.name().toLowerCase() + ".max-health", mobHealths.get(type.name()) * 5);
				getConfig().set("mobs." + type.name().toLowerCase() + ".effects.general_damage", mobDamages.get(type.name()) * 5);
			}
		}
		try {
			getConfig().save(getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRead() {
		for (EntityType type : EntityType.values()) {
			if (type.isAlive() && !(type.name().equalsIgnoreCase("player"))) {
				MCreature creature = new MCreature(type, getConfig().getInt("mobs." + type.name().toLowerCase() + ".max-health",  mobHealths.get(type.name()) * 5));
				getPlugin().getMobManager().addCreature(creature);
				/*
				 * Add the effects
				 */
				if (getConfig().getConfigurationSection("mobs." + type.name().toLowerCase() + ".effects") != null) {
					for (String sEffect : 
						getConfig().getConfigurationSection("mobs." + type.name().toLowerCase() + ".effects").getKeys(false)) {
						
						MEffect effect = getPlugin().getEffectManager().getEffect(sEffect);
						if (effect == null) {
							getPlugin().getLogger().warning("Configuration Error: No effect exists by the name " + sEffect + " in mob configuration for mob " + type.name());
							continue;
						}
						String effectAmount = getConfig().getString("mobs." + type.name().toLowerCase() + ".effects." + sEffect, "0");
						if (effectAmount.contains("%")) {
							effectAmount = effectAmount.replaceAll("%", "");
							try {
								creature.setEffectPercent(effect, Double.parseDouble(effectAmount));
							} catch (NumberFormatException ex) {
								getPlugin().getLogger().info("Configuration Error: Value for effect " + effect.getName() + " must be a number");
							}
						} else {
							try {
								creature.setEffectDecimal(effect, Double.parseDouble(effectAmount));
							} catch (NumberFormatException ex) {
								getPlugin().getLogger().info("Configuration Error: Value for effect " + effect.getName() + " must be a number");
							}
						}
					}
				}
				
			}
		}
	}

}
