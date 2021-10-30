package me.vilmu.waterwars.utilities;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.vilmu.waterwars.WaterWars;

public class ConfigManager {
	
	private WaterWars plugin = WaterWars.getPlugin(WaterWars.class);

	// Files & File Configs Here
	public FileConfiguration cfg;
	public File configfile;
	// --------------------------

	public void setup() {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}

		configfile = new File(plugin.getDataFolder(), "config.yml");

		if (!configfile.exists()) {
			try {
				configfile.createNewFile();
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "....The config.yml file has been created");
			} catch (IOException e) {
				Bukkit.getServer().getConsoleSender()
						.sendMessage(ChatColor.RED + "Could not create the config.yml file");
			}
		}

		cfg = YamlConfiguration.loadConfiguration(configfile);
	}

	public FileConfiguration loadConfig() {
		return cfg;
	}

	public void saveConfig() {
		try {
			cfg.save(configfile);
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "The config.yml file has been saved");

		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not save the config.yml file");
		}
	}
}
