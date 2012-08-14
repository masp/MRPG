package masp.plugins.mlight.config;

import java.io.File;
import java.io.IOException;

import masp.plugins.mlight.MLight;

import org.bukkit.configuration.file.YamlConfiguration;

public abstract class Configuration {
	
	private MLight plugin;
	private YamlConfiguration config;
	private String name;
	private File dir;
	
	public Configuration(String name, MLight plugin, File dir) {
		this.plugin = plugin;
		this.dir = dir;
		this.name = name;
		
		config = YamlConfiguration.loadConfiguration(getFile());
	}
	
	public String getName() {
		return name;
	}
	
	public MLight getPlugin() {
		return plugin;
	}
	
	public File getFile() {
		File file = new File(dir, name + ".yml");
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			try {
				file.createNewFile();
				config = YamlConfiguration.loadConfiguration(file);
				onCreate();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	public YamlConfiguration getConfig() {
		return config;
	}
	
	public File getDirectory() {
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;
		if (!(o instanceof Configuration)) {
			return false;
		}
		return ((Configuration) o).getName().equalsIgnoreCase(getName());
	}
	
	@Override
	public int hashCode() {
		return getName().hashCode();
	}
	
	public abstract void onCreate();
	
	public abstract void onRead();
	
}
