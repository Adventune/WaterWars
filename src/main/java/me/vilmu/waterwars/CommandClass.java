package me.vilmu.waterwars;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class CommandClass implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)){
			// Console sent the command
			
			return true;
		}
		else {
			// Player sent the command
			Player p = (Player) sender;
			if(args[0].equals("m")) {
				for(ArmorStand stand : p.getWorld().getEntitiesByClass(ArmorStand.class)) {
					WaterWars.debug(stand);
				}
			}

		}
		return false;
	}
}
