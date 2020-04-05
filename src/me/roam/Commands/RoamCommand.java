package me.roam.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.roam.Utils.Roamers;
import net.md_5.bungee.api.ChatColor;

public class RoamCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commmandLabel, String[] args) {
		if (!(sender instanceof Player)) {
		   sender.sendMessage("Players only!");
		   return false;
		}
		
		Player p = (Player)sender;
		Roamers roam = new Roamers();
		
		if (!p.hasPermission("Roam.Use")) {
			p.sendMessage(ChatColor.RED + "You do not have permission!");
			return false;
		}
		
		roam.toggleRoam(p);
		return false;
	}

}
