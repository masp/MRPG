package masp.plugins.mlight.data.mobs.mobwrappers.passive;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntitySheep;
import net.minecraft.server.Item;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalBreed;
import net.minecraft.server.PathfinderGoalEatTile;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalFollowParent;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalPanic;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.PathfinderGoalTempt;
import net.minecraft.server.World;

public class MSheep extends EntitySheep {
	
	private int e;
	
	private PathfinderGoalEatTile f = new PathfinderGoalEatTile(this);
	
	@SuppressWarnings("unchecked")
	public MSheep(World world) {
		super(world);
		try {
			Field goala = this.goalSelector.getClass().getDeclaredField("a");
			goala.setAccessible(true);
			((List<PathfinderGoal>) goala.get(this.goalSelector)).clear();
			
			float f = 0.23F;
			
			this.goalSelector.a(0, new PathfinderGoalFloat(this));
		    this.goalSelector.a(1, new PathfinderGoalPanic(this, 0.38F));
		    this.goalSelector.a(2, new PathfinderGoalBreed(this, f));
		    this.goalSelector.a(3, new PathfinderGoalTempt(this, 0.25F, Item.WHEAT.id, false));
		    this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 0.25F));
		    this.goalSelector.a(5, this.f);
		    this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, f));
		    this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
		    this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	protected void bc() {
        this.e = this.f.f();
        super.bc();
    }
	
	@Override
	public void d() {
        if (this.world.isStatic) {
            this.e = Math.max(0, this.e - 1);
        }

        super.d();
    }
	
}
