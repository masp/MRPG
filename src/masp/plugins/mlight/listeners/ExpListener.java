package masp.plugins.mlight.listeners;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.Settings;
import masp.plugins.mlight.data.MCreature;
import masp.plugins.mlight.data.items.ExpBag;
import masp.plugins.mlight.data.player.MPlayer;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.inventory.SpoutItemStack;

public class ExpListener implements Listener {
	
	public static Map<Location, Long> placedBlocks;
	
	public ExpListener() {
		placedBlocks = new LinkedHashMap<Location, Long>() {
			private static final long serialVersionUID = 2623620773233514414L;
            private final int MAX_ENTRIES = Settings.MAX_TRACED_BLOCKS;

            @Override
            protected boolean removeEldestEntry(Map.Entry<Location, Long> eldest) {
                return size() > MAX_ENTRIES || eldest.getValue() + Settings.BLOCK_TRACKING_DURATION <= System.currentTimeMillis();
            }
		};
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		MPlayer player = MRPG.getPlayer(event.getPlayer());
		if (player != null) {
			double xp = MRPG.getExpManager().getBlockExp(event.getBlock());
			if (xp > 0) {
				if (!wasBlockPlaced(event.getBlock())) {
					player.addExp(xp);
					this.displayExpMessage(player, xp);
				} else {
					player.displaySideNotification(new GenericLabel("No exp gained - block placed too recently!")
						  .setTextColor(new Color(190, 190, 190)));
					placedBlocks.remove(event.getBlock().getLocation());
				}
			}
		}
	}
	
	/*
	 * All of the bottom code is borrowed from the Heroes plugin
	 * See https://github.com/khobbits/Heroes/blob/master/src/com/herocraftonline/dev/heroes/HBlockListener.java
	 */
	@EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        if (event.isCancelled()) {
            return;
        }

        List<Location> movedBlocks = new ArrayList<Location>();
        for (Block block : event.getBlocks()) {
            if (placedBlocks.containsKey(block.getLocation())) {
                movedBlocks.add(block.getLocation());
            }
        }

        int x = event.getDirection().getModX();
        int y = event.getDirection().getModY();
        int z = event.getDirection().getModZ();
        Long time = System.currentTimeMillis();
        for (Location loc : movedBlocks) {
            placedBlocks.put(loc.clone().add(x, y, z), time);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        
        Location loc = event.getBlock().getLocation();
        if (placedBlocks.containsKey(loc)) {
            placedBlocks.remove(loc);
            placedBlocks.put(event.getBlock().getRelative(event.getDirection()).getLocation(), System.currentTimeMillis());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Block block = event.getBlock();
        placedBlocks.put(block.getLocation().clone(), System.currentTimeMillis());
    }


    private boolean wasBlockPlaced(Block block) {
        Location loc = block.getLocation();

        if (placedBlocks.containsKey(loc)) {
            long timePlaced = placedBlocks.get(loc);
            if (timePlaced + Settings.BLOCK_TRACKING_DURATION > System.currentTimeMillis()) {
                return true;
            } else {
                placedBlocks.remove(loc);
                return false;
            }
        }
        return false;
    }
    
    /*
     * End Heroes
     */
	

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityKilled(EntityDamageEvent event) {
		// Entity died
		if (event.getEntity().isDead() && event.isCancelled()) {
			if (event.getEntity() instanceof LivingEntity) {
				LivingEntity entity = (LivingEntity) event.getEntity();
				if (entity instanceof Player == false) {
					MCreature creature = MRPG.getMobManager().getCreature(entity.getType().name().toLowerCase());
					if (creature.getExp() > 0D) {
						if (Settings.DROP_EXP) { 
							dropExp(creature, entity);
						} else {
							if (event instanceof EntityDamageByEntityEvent) {
								EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent) event;
								if (nEvent.getDamager() instanceof Player) {
									MPlayer attacker = MRPG.getPlayer((Player) nEvent.getDamager());
									attacker.addExp(creature.getExp());
									this.displayExpMessage(attacker, creature.getExp());
								}
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBagPickup(PlayerPickupItemEvent event) {
		final Item item = event.getItem();
		double exp = ExpBag.getExp(item);
		// If the bag is actually a bag
		if (exp != -1.0D) {
			event.setCancelled(true);
			final MPlayer player = MRPG.getPlayer(event.getPlayer());
			if (player == null) return;
			player.addExp(exp);
			this.displayExpMessage(player, exp);
			
			item.remove();
			player.getPlayer().getWorld().playSound(item.getLocation(), Sound.ORB_PICKUP, 1.2f, 1.2f);
		}
	}
	
	public void displayExpMessage(MPlayer player, double exp) {
		player.displaySideNotification(new GenericLabel("+" + exp + " EXP!").setTextColor(new Color(0, 245, 0)));
	}
	
	public void dropExp(MCreature creature, LivingEntity entity) {
		 MRPG.getExpManager().addAttachedSource(
			 entity.getWorld().dropItemNaturally(
				 entity.getLocation(), new SpoutItemStack(MRPG.getItemManager().getBag())).getUniqueId(), 
				 creature.getExp());
	}
	
}
