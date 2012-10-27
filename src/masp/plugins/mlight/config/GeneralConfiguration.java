package masp.plugins.mlight.config;

import java.io.File;
import java.io.IOException;

import masp.plugins.mlight.MRPG;

import org.bukkit.configuration.file.YamlConfiguration;

public class GeneralConfiguration extends Configuration {

	public GeneralConfiguration(MRPG plugin, File dir) {
		super("general", plugin, dir);
	}

	@Override
	public void onCreate() {
		YamlConfiguration config = getConfig();
		config.set("theme", "modern");
		try {
			config.save(getFile());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onRead() {
	}

}
