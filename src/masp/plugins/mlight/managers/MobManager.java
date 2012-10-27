package masp.plugins.mlight.managers;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.data.MCreature;
import masp.plugins.mlight.data.effects.EffectCollection;
import masp.plugins.mlight.data.effects.EffectParticipant;
import masp.plugins.mlight.data.effects.types.MEffect;
import masp.plugins.mlight.data.mobs.mobwrappers.hostile.MBlaze;
import masp.plugins.mlight.data.mobs.mobwrappers.hostile.MCaveSpider;
import masp.plugins.mlight.data.mobs.mobwrappers.hostile.MCreeper;
import masp.plugins.mlight.data.mobs.mobwrappers.hostile.MEnderDragon;
import masp.plugins.mlight.data.mobs.mobwrappers.hostile.MEnderman;
import masp.plugins.mlight.data.mobs.mobwrappers.hostile.MGhast;
import masp.plugins.mlight.data.mobs.mobwrappers.hostile.MGiant;
import masp.plugins.mlight.data.mobs.mobwrappers.hostile.MMagmaCube;
import masp.plugins.mlight.data.mobs.mobwrappers.hostile.MSilverfish;
import masp.plugins.mlight.data.mobs.mobwrappers.hostile.MSkeleton;
import masp.plugins.mlight.data.mobs.mobwrappers.hostile.MSlime;
import masp.plugins.mlight.data.mobs.mobwrappers.hostile.MSpider;
import masp.plugins.mlight.data.mobs.mobwrappers.hostile.MZombie;
import masp.plugins.mlight.data.mobs.mobwrappers.hostile.MZombiePigman;
import masp.plugins.mlight.data.mobs.mobwrappers.passive.MChicken;
import masp.plugins.mlight.data.mobs.mobwrappers.passive.MCow;
import masp.plugins.mlight.data.mobs.mobwrappers.passive.MCowMushroom;
import masp.plugins.mlight.data.mobs.mobwrappers.passive.MIronGolem;
import masp.plugins.mlight.data.mobs.mobwrappers.passive.MOcelot;
import masp.plugins.mlight.data.mobs.mobwrappers.passive.MPig;
import masp.plugins.mlight.data.mobs.mobwrappers.passive.MSheep;
import masp.plugins.mlight.data.mobs.mobwrappers.passive.MSnowGolem;
import masp.plugins.mlight.data.mobs.mobwrappers.passive.MSquid;
import masp.plugins.mlight.data.mobs.mobwrappers.passive.MVillager;
import masp.plugins.mlight.data.mobs.mobwrappers.passive.MWolf;
import masp.plugins.mlight.utils.Utils;

import org.bukkit.entity.LivingEntity;

public class MobManager {
	
	private Set<MCreature> creatures;
	
	public MobManager() {
		creatures = new HashSet<MCreature>();
		
		// Register all the custom mobs
		try {
			Method a = net.minecraft.server.EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
			a.setAccessible(true);
			
			a.invoke(a, MZombie.class, "Zombie", 54);
			a.invoke(a, MSpider.class, "Spider", 52);
			a.invoke(a, MCreeper.class, "Creeper", 50);
			a.invoke(a, MSkeleton.class, "Skeleton", 51);
			a.invoke(a, MEnderman.class, "Enderman", 58);
			a.invoke(a, MGiant.class, "Giant", 53);
			a.invoke(a, MSlime.class, "Slime", 55);
			a.invoke(a, MGhast.class, "Ghast", 56);
			a.invoke(a, MZombiePigman.class, "PigZombie", 57);
			a.invoke(a, MCaveSpider.class, "CaveSpider", 59);
			a.invoke(a, MSilverfish.class, "Silverfish", 60);
			a.invoke(a, MBlaze.class, "Blaze", 61);
			a.invoke(a, MMagmaCube.class, "LavaSlime", 62);
			a.invoke(a, MEnderDragon.class, "EnderDragon", 63);
			a.invoke(a, MPig.class, "Pig", 90);
			a.invoke(a, MSheep.class, "Sheep", 91);
			a.invoke(a, MCow.class, "Cow", 92);
			a.invoke(a, MChicken.class, "Chicken", 93);
			a.invoke(a, MSquid.class, "Squid", 94);
			a.invoke(a, MWolf.class, "Wolf", 95);
			a.invoke(a, MCowMushroom.class, "MushroomCow", 96);
			a.invoke(a, MSnowGolem.class, "SnowMan", 97);
			a.invoke(a, MOcelot.class, "Ozelot", 98);
			a.invoke(a, MIronGolem.class, "VillagerGolem", 99);
			a.invoke(a, MVillager.class, "Villager", 120);
		} catch (Exception ex) {
			MRPG.getInstance().getLogger().warning("Error initializing custom mobs!");
			ex.printStackTrace();
		}
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
	
	/*
	 * The return is not a direct connection to the living entity. Must be done by alterior methods.
	 */
	public EffectCollection getCreatureEffects(LivingEntity entity) {
		EffectParticipant collection = Utils.deepCopy(getCreature(entity.getType().name()));
		MobEffectManager mEffManager = MRPG.getMobEffectManager();
		if (mEffManager.hasEffects(entity)) {
			EffectParticipant mobPart = mEffManager.getEffects(entity);
			for (MEffect effect : mobPart.getEffects()) {
				collection.setEffectDecimal(effect, 
						(mobPart.getTotalEffectsDecimal(effect) + collection.getTotalEffectsDecimal(effect)) +
						((mobPart.getTotalEffectsDecimal(effect) + collection.getTotalEffectsDecimal(effect)) * 
								(mobPart.getTotalEffectsPercent(effect) + collection.getTotalEffectsPercent(effect))));
			}
		}
		return collection;
	}
	
	public void removeCreature(String name) {
		creatures.remove(name);
	}
	
	public Set<MCreature> getCreatures() {
		return creatures;
	}
	
}
