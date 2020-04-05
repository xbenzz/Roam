package me.roam.Listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.roam.Utils.Freecam;
import me.roam.Utils.Roamers;
import net.md_5.bungee.api.ChatColor;

public class RoamListener implements Listener {
	
	 Roamers roam = new Roamers();
	
	 @EventHandler(priority=EventPriority.LOW)
	 public void onLeave(PlayerQuitEvent e) {
		 Player p = e.getPlayer();
		 if (roam.getRoamers().contains(p.getUniqueId())) {
			 roam.toggleRoam(p);
		 }
	 }
	 
	 @EventHandler(priority=EventPriority.LOW)
	 public void onLeave(PlayerJoinEvent e) {
		 Player p = e.getPlayer();
		 if (p.hasPermission("Roam.Bypass"))
			 return;
		 if (p.getGameMode() == GameMode.SPECTATOR)
			 p.setGameMode(GameMode.SURVIVAL);
	 }
	 
	 @EventHandler(priority=EventPriority.LOW)
	 public void onEntityCombust(EntityCombustEvent event) {
		 if (event.getEntity() instanceof Freecam) {
			 event.setCancelled(true);
		 }
	 }
	 
	 @EventHandler(priority=EventPriority.LOW)
	 public void playerDropItem(PlayerDropItemEvent event) {
	   if (roam.getRoamers().contains(event.getPlayer().getUniqueId())) {
	     event.setCancelled(true);
	   }
	 }

	 @EventHandler(priority=EventPriority.LOW)
	 public void blockInventory(InventoryOpenEvent event) {
	   if (roam.getRoamers().contains(event.getPlayer().getUniqueId())) {
	     event.setCancelled(true);
	   }
	 }
	 
	 @EventHandler(priority=EventPriority.LOW)
	 public void interacting(PlayerInteractEvent event) {
	   if (roam.getRoamers().contains(event.getPlayer().getUniqueId())) {
	     event.setCancelled(true);
	   }
	 }
	 
	 @EventHandler(priority=EventPriority.LOW)
	 public void onDamage(EntityDamageEvent e) {
		 if (e.getEntity() instanceof Zombie) {
			 Entity zombie = e.getEntity();
			 if (zombie.getCustomName().contains(ChatColor.translateAlternateColorCodes('&', "&8[&7Roaming&8] &e"))) {
				e.setCancelled(true);
				Player p = Bukkit.getPlayer(zombie.getCustomName().substring(18));
				if (zombie.getUniqueId() == roam.getPlayerZombie(p.getUniqueId())) {
					roam.toggleRoam(p);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Freecam> &cForce removed due to combat tagged!"));
				}
			 }
		 }	
	 }
	 
	 @EventHandler(priority=EventPriority.LOW)
	 public void onClick(PlayerTeleportEvent e) {
		 UUID uuid = e.getPlayer().getUniqueId();
		 if (roam.getRoamers().contains(uuid)) {
			 e.setCancelled(true);
			 e.getPlayer().setSpectatorTarget(null);
		 }
	 }
	 
	  @EventHandler(priority=EventPriority.LOW)
	  public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		  String message = event.getMessage().toLowerCase();
		  Player p = event.getPlayer();
		  if (roam.getRoamers().contains(p.getUniqueId())) {
			  if (!message.equalsIgnoreCase("/roam")) {
				  event.setCancelled(true);
			      p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Freecam> &cYou cannot use commands in freecam mode!"));
			  }
		  }
	  }

}
