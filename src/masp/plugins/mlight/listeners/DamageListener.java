package masp.plugins.mlight.listeners;

import java.util.ArrayList;

import masp.plugins.mlight.MLight;
import masp.plugins.mlight.Settings;
import masp.plugins.mlight.Utils;
import masp.plugins.mlight.data.Damageable;
import masp.plugins.mlight.data.MCreature;
import masp.plugins.mlight.data.effects.EffectCollection;
import masp.plugins.mlight.data.player.MPlayer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class DamageListener implements Listener {
	
	private MLight plugin;
	
	public DamageListener(MLight plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void handleCustomDamage(EntityDamageEvent event) {
		event.setCancelled(true);
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent) event;
			if (event.getEntity() instanceof Player) {
				if (nEvent.getDamager() instanceof Player) {
					MPlayer attacker = plugin.getPlayer(((Player) nEvent.getDamager()).getName());
					MPlayer defender = plugin.getPlayer(((Player) event.getEntity()).getName());
					handlePlayerAttack(attacker, defender);
				}  else if (nEvent.getDamager() instanceof LivingEntity) {
					MPlayer defender = plugin.getPlayer((Player) event.getEntity());
					handleEntityAttackPlayer((LivingEntity) nEvent.getDamager(), defender);
				} else if (nEvent.getDamager() instanceof Projectile) {
					if (nEvent.getDamager() instanceof Fireball) {
						Fireball ball = (Fireball) nEvent.getDamager();
						handleEntityAttackPlayer(ball.getShooter(), plugin.getPlayer((Player) nEvent.getEntity()));
					} else if (nEvent.getDamager() instanceof Arrow) {
						Arrow arrow = (Arrow) nEvent.getDamager();
						if (arrow.getShooter() instanceof Player) {
							MPlayer attacker = plugin.getPlayer((Player) arrow.getShooter());
							handlePlayerShootPlayer(attacker, plugin.getPlayer((Player) event.getEntity()), arrow);
						} else {
							handleEntityShootPlayer(arrow.getShooter(), plugin.getPlayer((Player) event.getEntity()), arrow);
						}
					}
				}
			} else if (event.getEntity() instanceof LivingEntity) {
				if (nEvent.getDamager() instanceof Player) {
					MPlayer attacker = plugin.getPlayer(((Player) nEvent.getDamager()).getName());
					handlePlayerAttackEntity(attacker, (LivingEntity) event.getEntity());
				} else if (nEvent.getDamager() instanceof LivingEntity) {
					handleEntityAttackEntity((LivingEntity) nEvent.getDamager(), (LivingEntity) event.getEntity());
				} else if (nEvent.getDamager() instanceof Projectile) {
					if (nEvent.getDamager() instanceof Fireball) {
						handleEntityAttackEntity(((Fireball) nEvent.getDamager()).getShooter(), (LivingEntity) nEvent.getEntity());
					} else if (nEvent.getDamager() instanceof Arrow) {
						if (((Arrow) nEvent.getDamager()).getShooter() instanceof Player) {
							MPlayer attacker = plugin.getPlayer((Player) ((Arrow) nEvent.getDamager()).getShooter());
							handlePlayerShootEntity(attacker, (LivingEntity) nEvent.getEntity(), (Arrow) nEvent.getDamager());
						} else {
							handleEntityShootEntity((LivingEntity) ((Arrow) nEvent.getDamager()).getShooter(), 
													(LivingEntity) nEvent.getEntity(),
													(Arrow) nEvent.getDamager());
						}
					}
				}
			}
		} else {
			if (event.getEntity() instanceof LivingEntity) {
				handleMiscDamage((LivingEntity) event.getEntity(), event, event.getCause());
			}
		}
	}
	
	@EventHandler
	public void onRegainHealth(EntityRegainHealthEvent event) {
		event.setCancelled(true);
	}
	
	/*
	 * All the normal entity damage events
	 */
	
	public void handlePlayerAttack(MPlayer attacker, MPlayer defender) {
		if (System.currentTimeMillis() - attacker.getLastAttack() >= Settings.DEFAULT_ATTACK_SPEED) {
			attacker.setLastAttack(System.currentTimeMillis());
			int totalWeaponDamage = Utils.getTotalDamage(attacker, defender);
			attacker.getPlayer().sendMessage(ChatColor.BLUE + "You dealt " + ChatColor.GREEN + totalWeaponDamage + ChatColor.BLUE + " damage!");
			defender.damage(totalWeaponDamage, attacker.getPlayer());
			Utils.knockback(attacker, attacker.getPlayer(), defender.getPlayer());
		}
	}
	
	public void handlePlayerAttackEntity(MPlayer attacker, LivingEntity defender) {
		if (System.currentTimeMillis() - attacker.getLastAttack() >= Settings.DEFAULT_ATTACK_SPEED) {
			attacker.setLastAttack(System.currentTimeMillis());
			MCreature creature = plugin.getMobManager().getCreature(defender.getType().name());
			int totalWeaponDamage = Utils.getTotalDamage(attacker, creature);
			attacker.getPlayer().sendMessage(ChatColor.BLUE + "You dealt " + ChatColor.GREEN + totalWeaponDamage + ChatColor.BLUE + " damage!");
			// Replicate the damage
			creature.damage(totalWeaponDamage, attacker.getPlayer(), defender);
		}
	}
	
	public void handleEntityAttackPlayer(LivingEntity attacker, MPlayer defender) {
		MCreature creature = plugin.getMobManager().getCreature(attacker.getType().name());
		int totalDamage = Utils.getTotalDamage(creature, defender);
		if (attacker instanceof Slime) {
			Slime slime = (Slime) attacker;
			// Remove damage if the slime is 0, well, remove it as much as default configured.
			if (slime.getSize() == 1) {
				totalDamage -= Settings.CONVERSION_FACTOR;
			} else {
				// Slime is size 4, 20 damage which is equal to 2 hearts, size if small/2, damage is 10, which is equal to one heart.
				totalDamage *= slime.getSize();
			}
		} else if (attacker instanceof Wolf) {
			if (!((Wolf) attacker).isTamed()) {
				totalDamage /= 2;
			}
		}
		
		defender.damage(totalDamage, defender.getPlayer());
		Utils.knockback(creature, attacker, defender.getPlayer());
	}
	
	public void handleEntityAttackEntity(LivingEntity attacker, LivingEntity defender) {
		MCreature cAttacker = plugin.getMobManager().getCreature(attacker.getType().name());
		MCreature cDefender = plugin.getMobManager().getCreature(defender.getType().name());
		int damage = Utils.getTotalDamage(cAttacker, cDefender);
		cDefender.damage(damage, attacker, defender);
	}
	
	/*
	 * End of normal entity damage events
	 */
	
	/*
	 * All the shooting events
	 */
	public void handlePlayerShootPlayer(MPlayer attacker, MPlayer defender, Arrow shot) {
		shot.remove();
		int totalDamage = Utils.getTotalDamage(plugin.getItemManager().getItem(-1).getAttack(), 
				defender, 
				new ArrayList<EffectCollection>(attacker.getSkills()));
		attacker.getPlayer().sendMessage(ChatColor.BLUE + "You dealt " + ChatColor.GREEN + totalDamage + ChatColor.BLUE + " damage!");
		defender.damage(totalDamage, defender.getPlayer());
	}
	
	public void handlePlayerShootEntity(MPlayer attacker, LivingEntity defender, Arrow shot) {
		shot.remove();
		MCreature cDefender = plugin.getMobManager().getCreature(defender.getType().name());
		int totalDamage = Utils.getTotalDamage(plugin.getItemManager().getItem(-1).getAttack(), 
				cDefender,
				new ArrayList<EffectCollection>(attacker.getSkills()));
		attacker.getPlayer().sendMessage(ChatColor.BLUE + "You dealt " + ChatColor.GREEN + totalDamage + ChatColor.BLUE + " damage!");
		cDefender.damage(totalDamage, attacker.getPlayer(), defender);
	}
	
	public void handleEntityShootPlayer(LivingEntity attacker, MPlayer defender, Arrow shot) {
		shot.remove();
		MCreature cAttacker = plugin.getMobManager().getCreature(attacker.getType().name());
		int totalDamage = Utils.getTotalDamage(cAttacker, defender);
		defender.damage(totalDamage, defender.getPlayer());
	}
	
	public void handleEntityShootEntity(LivingEntity attacker, LivingEntity defender, Arrow shot) {
		shot.remove();
		MCreature cAttacker = plugin.getMobManager().getCreature(attacker.getType().name());
		MCreature cDefender = plugin.getMobManager().getCreature(attacker.getType().name());
		int totalDamage = Utils.getTotalDamage(cAttacker, cDefender);
		cDefender.damage(totalDamage,  attacker, defender);
	}
	
	/*
	 * End of Shooting events
	 */
	
	public void handleMiscDamage(LivingEntity entity, EntityDamageEvent event, DamageCause cause) {
		Damageable damaged = null;
		if (entity instanceof Player) {
			damaged = plugin.getPlayer((Player) entity);
		} else {
			damaged = plugin.getMobManager().getCreature(entity.getType().name());
		}
		
		if (damaged == null) {
			return;
		}
		
		if (cause == DamageCause.CONTACT) {
			if (entity.getNoDamageTicks() == 0) {
				damaged.damage(Settings.CACTUS_DAMAGE, entity);
				entity.setNoDamageTicks(10);
			}
		} else if (cause == DamageCause.BLOCK_EXPLOSION) { 
			EntityDamageByBlockEvent nEvent = (EntityDamageByBlockEvent) event;
			int distance = (int) nEvent.getDamager().getLocation().distance(entity.getLocation());
			// Every block you lose 10 damage, 10 blocks away is safe.
			int damage = 150 - (distance * 15);
			damaged.damage(damage, entity);
		} else if (cause == DamageCause.DROWNING) {
			damaged.damage(Settings.DROWNING_DAMAGE, entity);
		} else if (cause == DamageCause.FALL) {
			damaged.damage((int) (Settings.FALL_DAMAGE * (Utils.positive(entity.getFallDistance() - 3))), entity);
		} else if (cause == DamageCause.FIRE) {
			damaged.damage(Settings.FIRE_DAMAGE, entity);
		} else if (cause == DamageCause.LAVA) {
			damaged.damage(Settings.LAVA_DAMAGE, entity);
		} else if (cause == DamageCause.LIGHTNING) {
			damaged.damage(Settings.LIGHTNING_DAMAGE, entity);
		} else if (cause == DamageCause.MAGIC) {
			damaged.damage(Settings.POTION_DAMAGE, entity);
		} else if (cause == DamageCause.MELTING) {
			damaged.damage(Settings.MELT_DAMAGE, entity);
		} else if (cause == DamageCause.POISON) {
			damaged.damage(Settings.POISON_DAMAGE, entity);
		} else if (cause == DamageCause.STARVATION) {
			damaged.damage(Settings.STARVATION_DAMAGE, entity);
		} else if (cause == DamageCause.SUFFOCATION) {
			damaged.damage(Settings.SUFFOCATION_DAMAGE, entity);
		} else if (cause == DamageCause.SUICIDE) {
			// We let him die :)
			entity.setHealth(0);
		} else if (cause == DamageCause.VOID) {
			// If you're in the void, you die.
			entity.setHealth(0);
		} else if (cause == DamageCause.CUSTOM) {
			damaged.damage(event.getDamage() * 5, entity);
		} else if (cause == DamageCause.FIRE_TICK) {
			damaged.damage(Settings.FIRE_DAMAGE, entity);
		}
	}
	
}
