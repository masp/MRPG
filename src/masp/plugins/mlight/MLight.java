package masp.plugins.mlight;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import masp.plugins.mlight.config.Configuration;
import masp.plugins.mlight.config.DatabaseConfiguration;
import masp.plugins.mlight.config.ItemConfiguration;
import masp.plugins.mlight.config.MobConfiguration;
import masp.plugins.mlight.data.effects.EffectManager;
import masp.plugins.mlight.data.player.MPlayer;
import masp.plugins.mlight.listeners.DamageListener;
import masp.plugins.mlight.managers.DataManager;
import masp.plugins.mlight.managers.ItemManager;
import masp.plugins.mlight.managers.MobManager;
import masp.plugins.mlight.managers.PlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MLight extends JavaPlugin implements Listener {
	
	private DatabaseConfiguration dataConfig;
	
	private DataManager dManager;
	private PlayerManager pManager;
	private ItemManager iManager;
	private MobManager mManager;
	private EffectManager eManager;
	
	private List<Configuration> configs = new ArrayList<Configuration>();
	
	@Override
	public void onLoad() {
		instance = this;
		
		this.dataConfig = new DatabaseConfiguration(this, getDataFolder());
		addConfig(dataConfig);
		
		dManager = new DataManager(this);
		pManager = new PlayerManager();
		iManager = new ItemManager();
		mManager = new MobManager();
		eManager = new EffectManager();
	}
	
	@Override
	public void onEnable() {
		
		this.getServer().getPluginManager().registerEvents(new DamageListener(this), this);
		
		addConfig(new ItemConfiguration(this, this.getDataFolder()));
		addConfig(new MobConfiguration(this, this.getDataFolder()));
		
		this.getServer().getPluginManager()
				.registerEvents(this, this);
		
		for (Configuration config : configs) {
			config.onRead();
		}
		
		dManager.init();
		
		getLogger().info("Plugin was successfully enabled!");
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Plugin was successfully disabled!");
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		try {
			MPlayer player = getDataManager().loadPlayer(event.getPlayer());
			this.getPlayerManager().addPlayer(player);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		try {
			this.getDataManager().savePlayer(getPlayer(event.getPlayer()));
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public EffectManager getEffectManager() {
		return eManager;
	}
	
	public DataManager getDataManager() {
		return dManager;
	}
	
	public PlayerManager getPlayerManager() {
		return pManager;
	}
	
	public MobManager getMobManager() {
		return mManager;
	}
	
	public ItemManager getItemManager() {
		return iManager;
	}
	
	public MPlayer getPlayer(String name) {
		return pManager.getPlayer(name);
	}
	
	public MPlayer getPlayer(Player player) {
		return pManager.getPlayer(player);
	}
	
	public void addConfig(Configuration config) {
		if (!configs.contains(config)) {
			configs.add(config);
		}
	}
	
	public DatabaseConfiguration getDataConfig() {
		return dataConfig;
	}
	
	private static MLight instance;
	public static MLight getInstance() {
		return instance;
	}
	
}
