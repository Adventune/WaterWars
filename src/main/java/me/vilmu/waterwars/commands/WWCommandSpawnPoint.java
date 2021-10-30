package me.vilmu.waterwars.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.vilmu.waterwars.PlayerMessages;
import me.vilmu.waterwars.Storage;
import me.vilmu.waterwars.WaterWars;

public class WWCommandSpawnPoint {

	public static boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			// Console sent the command
			WaterWars.commandErrorConsole(0);
			return true;
		}
		else {
			// Player sent the command
			Player p = (Player) sender;
			Location loc = p.getLocation();
			List<Location> locs = new ArrayList<Location>();
			locs = Storage.getSpawnLoc();
			locs.add(loc);
			Storage.setSpawnLoc(locs);
			PlayerMessages.createdSpawnPoint(p);
			if(!WaterWars.modifiedAttributes.contains(12)) WaterWars.modifiedAttributes.add(12);
			return true;
	
		}
	}
}
