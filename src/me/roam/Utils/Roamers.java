package me.roam.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.World;

public class Roamers {
	
	public static ArrayList<UUID> roamers = new ArrayList<UUID>();
	public static HashMap<UUID, Location> locations = new HashMap<UUID, Location>();
	public static HashMap<UUID, UUID> zombies = new HashMap<UUID, UUID>();
	
	public Location getLastLocation(UUID uuid) {
		return locations.get(uuid);
	}
	
	public ArrayList<UUID> getRoamers() {
		return roamers;
	}
	
	public UUID getPlayerZombie(UUID uuid) {
		return zombies.get(uuid);
	}
	
	public HashMap<UUID, UUID> getAllZombies() {
		return zombies;
	}
	
	public HashMap<UUID, Location> getAllLocations() {
		return locations;
	}
	
	public void toggleRoam(Player p) {
		UUID uuid = p.getUniqueId();
		if (roamers.contains(uuid)) {
			roamers.remove(uuid);
			Bukkit.getOnlinePlayers().stream().filter(o -> o != p).forEach(o -> p.showPlayer(o));
			Bukkit.getOnlinePlayers().stream().filter(o -> o != p).forEach(o -> o.showPlayer(p));
			p.teleport(locations.get(uuid));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Freecam> &7Roaming mode has been &eDisabled&7."));
			p.setGameMode(GameMode.SURVIVAL);
			destroyEntity(p);
			locations.remove(uuid);
			zombies.remove(uuid);
		} else {
			Bukkit.getOnlinePlayers().stream().filter(o -> o != p).forEach(o -> p.hidePlayer(o));
			Bukkit.getOnlinePlayers().stream().filter(o -> o != p).forEach(o -> o.hidePlayer(p));
			spawnEntity(p);
			roamers.add(uuid);
			locations.put(uuid, p.getLocation());
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Freecam> &7Roaming mode has been &eEnabled&7."));
			p.setGameMode(GameMode.SPECTATOR);
		}
	}
	
	public void spawnEntity(Player p) {
        Location loc = p.getLocation();
        World world = ((CraftWorld) p.getLocation().getWorld()).getHandle();
		Freecam entity = new Freecam(((CraftWorld) p.getLocation().getWorld()).getHandle());
		
	    ItemStack title = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.PLAYER.ordinal());
	    SkullMeta titleMeta = (SkullMeta)title.getItemMeta();
	    titleMeta.setOwner(p.getName());
	    title.setItemMeta(titleMeta);
		
		entity.setCustomName(ChatColor.translateAlternateColorCodes('&', "&8[&7Roaming&8] &e" + p.getName()));
		entity.setCustomNameVisible(true);
		entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        entity.setEquipment(4, CraftItemStack.asNMSCopy(title));
		
        ((CraftLivingEntity) entity.getBukkitEntity()).setRemoveWhenFarAway(false); 
        world.addEntity(entity, SpawnReason.CUSTOM);
        zombies.put(p.getUniqueId(), entity.getUniqueID());
	}
	
	public void destroyEntity(Player p) {
        for (Entity e : p.getWorld().getLivingEntities()) {
        	if (e.getUniqueId() == zombies.get(p.getUniqueId())) {
        		e.remove();
        	}
        }
	}
	
	public void removeAllRoamers() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (getRoamers().contains(p.getUniqueId())) {
				toggleRoam(p);
			}
		}
        for (org.bukkit.World w : Bukkit.getWorlds()) {
        	for (Entity e : w.getEntities()) {
        		if (e instanceof Freecam)
        			e.remove();
        	}
        }
		roamers.clear();
		locations.clear();
		zombies.clear();
	}

}
