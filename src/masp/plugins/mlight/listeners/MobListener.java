package masp.plugins.mlight.listeners;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.data.mobs.MDangerMob;
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
import net.minecraft.server.EntityGiantZombie;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.ChunkLoadEvent;

public class MobListener implements Listener {
	
	@EventHandler
	public void removeCreatureOnDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getEntity();
			if (MRPG.getMobEffectManager().hasEffects(entity)) {
				MRPG.getMobEffectManager().removeEffects(entity);
			}
		}
	}
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {

		Location location = event.getLocation();
		Entity entity = event.getEntity();
		EntityType cType = event.getEntityType();
		World world = location.getWorld();
		
		net.minecraft.server.World mcWorld = ((CraftWorld) world).getHandle();
		net.minecraft.server.Entity mcEntity = ((CraftEntity) entity).getHandle();
		
		replaceEntity(cType, mcEntity, mcWorld, location);
	}
	
	/*@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void loadCreatures(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		int distance = 64;
		for (Entity entity : player.getNearbyEntities(distance, 256, distance)) {
			EntityType type = entity.getType();
			if (entity instanceof LivingEntity && entity instanceof Player == false) {
				replaceEntity(type, ((CraftEntity) entity).getHandle(), ((CraftWorld) player.getWorld()).getHandle(), entity.getLocation());
			}
		}
	}*/
	
	@EventHandler
	public void onChunkLoaded(ChunkLoadEvent event) {
		final Chunk chunk = event.getChunk();
		for (Entity entity : chunk.getEntities()) {
			if (entity instanceof LivingEntity) {
				replaceEntity(entity.getType(), 
							  ((CraftEntity) entity).getHandle(),
							  ((CraftWorld) entity.getWorld()).getHandle(),
							  entity.getLocation());
			}
		}
	}
	
	public void replaceEntity(EntityType cType, 
							  net.minecraft.server.Entity mcEntity, 
							  net.minecraft.server.World mcWorld, 
							  Location location) {
		
		if (mcEntity instanceof MDangerMob) {
			if (((MDangerMob) mcEntity).isChanged()) {
				return;
			}
		}
		
		if (cType == EntityType.ZOMBIE) {
			
			MZombie zombie = new MZombie(mcWorld, location);
			zombie.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntityZombie) mcEntity);
			mcWorld.addEntity(zombie, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.SPIDER) {
			
			MSpider spider = new MSpider(mcWorld, location);
			spider.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntitySpider) mcEntity);
			mcWorld.addEntity(spider, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.ENDERMAN) {
			
			MEnderman enderman = new MEnderman(mcWorld, location);
			enderman.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntityEnderman) mcEntity);
			mcWorld.addEntity(enderman, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.SKELETON) {
			
			MSkeleton skeleton = new MSkeleton(mcWorld, location);
			skeleton.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntitySkeleton) mcEntity);
			mcWorld.addEntity(skeleton, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.CREEPER) {
			
			MCreeper creeper = new MCreeper(mcWorld, location);
			creeper.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntityCreeper) mcEntity);
			mcWorld.addEntity(creeper, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.GIANT) {
			
			MGiant giant = new MGiant(mcWorld, location);
			giant.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((EntityGiantZombie) mcEntity);
			mcWorld.addEntity(giant, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.SLIME) {
			
			MSlime slime = new MSlime(mcWorld, location);
			slime.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntitySlime) mcEntity);
			mcWorld.addEntity(slime, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.GHAST) {
			
			MGhast ghast = new MGhast(mcWorld, location);
			ghast.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntityGhast) mcEntity);
			mcWorld.addEntity(ghast, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.PIG_ZOMBIE) {
			
			MZombiePigman zombiePig = new MZombiePigman(mcWorld, location);
			zombiePig.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntityPigZombie) mcEntity);
			mcWorld.addEntity(zombiePig, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.CAVE_SPIDER) {
			
			MCaveSpider caveSpider = new MCaveSpider(mcWorld, location);
			caveSpider.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntityCaveSpider) mcEntity);
			mcWorld.addEntity(caveSpider, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.SILVERFISH) {
			
			MSilverfish silverfish = new MSilverfish(mcWorld, location);
			silverfish.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntitySilverfish) mcEntity);
			mcWorld.addEntity(silverfish, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.BLAZE) {
			
			MBlaze blaze = new MBlaze(mcWorld, location);
			blaze.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntityBlaze) mcEntity);
			mcWorld.addEntity(blaze, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.MAGMA_CUBE) {
			
			MMagmaCube mCube = new MMagmaCube(mcWorld, location);
			mCube.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntityMagmaCube) mcEntity);
			mcWorld.addEntity(mCube, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.ENDER_DRAGON) {
			
			MEnderDragon dragon = new MEnderDragon(mcWorld, location);
			dragon.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntityEnderDragon) mcEntity);
			mcWorld.addEntity(dragon, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.PIG && mcEntity instanceof MPig == false) {
			
			MPig pig = new MPig(mcWorld);
			pig.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntityPig) mcEntity);
			mcWorld.addEntity(pig, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.SHEEP && mcEntity instanceof MSheep == false) {
			
			MSheep sheep = new MSheep(mcWorld);
			sheep.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntitySheep) mcEntity);
			mcWorld.addEntity(sheep, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.COW && mcEntity instanceof MCow == false) {
			
			MCow cow = new MCow(mcWorld);
			cow.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntityCow) mcEntity);
			mcWorld.addEntity(cow, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.CHICKEN && mcEntity instanceof MChicken == false) {
			
			MChicken chicken = new MChicken(mcWorld);
			chicken.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntityChicken) mcEntity);
			mcWorld.addEntity(chicken, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.SQUID && mcEntity instanceof MSquid == false) {
			
			MSquid squid = new MSquid(mcWorld);
			squid.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntitySquid) mcEntity);
			mcWorld.addEntity(squid, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.WOLF && mcEntity instanceof MWolf == false) {
			
			MWolf wolf = new MWolf(mcWorld);
			wolf.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntityWolf) mcEntity);
			mcWorld.addEntity(wolf, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.MUSHROOM_COW && mcEntity instanceof MCowMushroom == false) {
			
			MCowMushroom cow = new MCowMushroom(mcWorld);
			cow.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntityMushroomCow) mcEntity);
			mcWorld.addEntity(cow, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.SNOWMAN && mcEntity instanceof MSnowGolem == false) {
			
			MSnowGolem sGolem = new MSnowGolem(mcWorld);
			sGolem.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntitySnowman) mcEntity);
			mcWorld.addEntity(sGolem, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.OCELOT && mcEntity instanceof MOcelot == false) {
			
			MOcelot ocelot = new MOcelot(mcWorld);
			ocelot.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntityOcelot) mcEntity);
			mcWorld.addEntity(ocelot, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.IRON_GOLEM && mcEntity instanceof MIronGolem == false) {
			
			MIronGolem golem = new MIronGolem(mcWorld);
			golem.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntityIronGolem) mcEntity);
			mcWorld.addEntity(golem, SpawnReason.CUSTOM);
			
		} else if (cType == EntityType.VILLAGER && mcEntity instanceof MVillager == false) {
			
			MVillager villager = new MVillager(mcWorld);
			villager.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntityVillager) mcEntity);
			mcWorld.addEntity(villager, SpawnReason.CUSTOM);
			
		}
	}
	
}
