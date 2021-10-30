package me.vilmu.waterwars.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.vilmu.waterwars.PlayerMessages;
import me.vilmu.waterwars.Storage;
import me.vilmu.waterwars.WaterWars;

public class WWCommandBuild {

	public static void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			// Console sent the command
			WaterWars.commandErrorConsole(0);
			return;
		}
		else {
			// Player sent the command
			Player p = (Player) sender;
			if(args.length == 2) {
				if (Bukkit.getWorld(args[1]) == null) { PlayerMessages.worldNotFound(p); return; }
				
				Location loc = Storage.getArenaCenter();
				loc.setWorld(Bukkit.getWorld(args[1]));
				
				loc.setY(loc.getWorld().getHighestBlockYAt(loc));
				p.teleport(loc);
				return;
			}
			else {
				PlayerMessages.buildUsage(p);
			}
	
		}
	}
}
