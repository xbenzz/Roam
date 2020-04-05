package me.roam;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.roam.Commands.DebugCommands;
import me.roam.Commands.RoamCommand;
import me.roam.Listeners.RoamListener;
import me.roam.Utils.CustomEntity;
import me.roam.Utils.Freecam;
import me.roam.Utils.Roamers;

public class Roam extends JavaPlugin {
	
	public static Roam instance;
	Roamers roam = new Roamers();
	
	public void onEnable() {
		instance = this;
		CustomEntity.addToMaps(Freecam.class, "Freecam", 54);
		
		registerCommands();
		registerEvents();
		loadConfig();
	}
	
	public void onDisable() {
		roam.removeAllRoamers();
	}
	
	private void registerCommands() {
	     getCommand("roam").setExecutor(new RoamCommand());
	     getCommand("debug").setExecutor(new DebugCommands());
	}
	   
	private void registerEvents() {
	    PluginManager manager = Bukkit.getPluginManager();
	    manager.registerEvents(new RoamListener(), this);
	}
	
	private void loadConfig() {
		FileConfiguration cfg = getConfig();
		cfg.options().copyDefaults(true);
		saveDefaultConfig();
	}
	
	public static Roam getInstance() {
		return instance;
	}

}
