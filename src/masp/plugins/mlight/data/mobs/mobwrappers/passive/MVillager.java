package masp.plugins.mlight.data.mobs.mobwrappers.passive;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityVillager;
import net.minecraft.server.EntityZombie;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalAvoidPlayer;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalInteract;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalLookAtTradingPlayer;
import net.minecraft.server.PathfinderGoalMakeLove;
import net.minecraft.server.PathfinderGoalMoveIndoors;
import net.minecraft.server.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.PathfinderGoalOpenDoor;
import net.minecraft.server.PathfinderGoalPlay;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.PathfinderGoalRestrictOpenDoor;
import net.minecraft.server.PathfinderGoalTakeFlower;
import net.minecraft.server.PathfinderGoalTradeWithPlayer;
import net.minecraft.server.World;

public class MVillager extends EntityVillager {

	@SuppressWarnings("unchecked")
	public MVillager(World world) {
		super(world);
		
		try {
			Field goala = this.goalSelector.getClass().getDeclaredField("a");
			goala.setAccessible(true);
			((List<PathfinderGoal>) goala.get(this.goalSelector)).clear();

			this.goalSelector.a(0, new PathfinderGoalFloat(this));
	        this.goalSelector.a(1, new PathfinderGoalAvoidPlayer(this, EntityZombie.class, 8.0F, 0.3F, 0.35F));
	        this.goalSelector.a(1, new PathfinderGoalTradeWithPlayer(this));
	        this.goalSelector.a(1, new PathfinderGoalLookAtTradingPlayer(this));
	        this.goalSelector.a(2, new PathfinderGoalMoveIndoors(this));
	        this.goalSelector.a(3, new PathfinderGoalRestrictOpenDoor(this));
	        this.goalSelector.a(4, new PathfinderGoalOpenDoor(this, true));
	        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 0.3F));
	        this.goalSelector.a(6, new PathfinderGoalMakeLove(this));
	        this.goalSelector.a(7, new PathfinderGoalTakeFlower(this));
	        this.goalSelector.a(8, new PathfinderGoalPlay(this, 0.32F));
	        this.goalSelector.a(9, new PathfinderGoalInteract(this, EntityHuman.class, 3.0F, 1.0F));
	        this.goalSelector.a(9, new PathfinderGoalInteract(this, EntityVillager.class, 5.0F, 0.02F));
	        this.goalSelector.a(9, new PathfinderGoalRandomStroll(this, 0.3F));
	        this.goalSelector.a(10, new PathfinderGoalLookAtPlayer(this, EntityLiving.class, 8.0F));
	        
		} catch (Exception ex) {
			ex.printStackTrace();
		}
			
	}

}
