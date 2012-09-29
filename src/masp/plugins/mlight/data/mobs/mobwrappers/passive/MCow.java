package masp.plugins.mlight.data.mobs.mobwrappers.passive;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.server.EntityCow;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalBreed;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalFollowParent;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalPanic;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.PathfinderGoalTempt;
import net.minecraft.server.World;

public class MCow extends EntityCow {

	
	@SuppressWarnings("unchecked")
	public MCow(World world) {
		super(world);
		
		
		try {
			Field goala = this.goalSelector.getClass().getDeclaredField("a");
			goala.setAccessible(true);
			((List<PathfinderGoal>) goala.get(this.goalSelector)).clear();
			
			this.goalSelector.a(0, new PathfinderGoalFloat(this));
	        this.goalSelector.a(1, new PathfinderGoalPanic(this, 0.38F));
	        this.goalSelector.a(2, new PathfinderGoalBreed(this, 0.2F));
	        this.goalSelector.a(3, new PathfinderGoalTempt(this, 0.25F, Item.WHEAT.id, false));
	        this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 0.25F));
	        this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 0.2F));
	        this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
	        this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
