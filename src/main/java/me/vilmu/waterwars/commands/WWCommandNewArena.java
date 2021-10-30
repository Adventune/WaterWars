package me.vilmu.waterwars.commands;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.vilmu.waterwars.PlayerMessages;
import me.vilmu.waterwars.Storage;
import me.vilmu.waterwars.WaterWars;

public class WWCommandNewArena {

	public static void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			WaterWars.commandErrorConsole(0);
			return;
		}
		
		Player p = (Player) sender;
		if(args.length == 2) {
			String worldName = null;
			if(args[1].equalsIgnoreCase("this")) {
				
				worldName = p.getWorld().getName();
				Pattern pat = Pattern.compile("(waterwars_)+([0-9]+)");
			    Matcher m = pat.matcher(worldName);
			    boolean b = m.find();
				if(b) {
					PlayerMessages.cantBeArena(p);	
				}
				
			} else if (args[1].equalsIgnoreCase("create")){
				worldName = createNewWorld();
				
			} else {
				PlayerMessages.newArenaUsage(p);
			}
			
			if(worldName != null) {
				List<String> newArenas = Storage.getArenaNames();
				if(!newArenas.contains(worldName)) {
					newArenas.add(worldName);
					Storage.setArenaNames(newArenas);
					if(WaterWars.modifiedAttributes.contains(8)) return;
					
					WaterWars.modifiedAttributes.add(8);
				}

			}
			

		} else {
			PlayerMessages.newArenaUsage(p);
		}
		
	}

	private static String createNewWorld() {
		String newWorldName = "waterwars0";
		int i = 0;
		while(Bukkit.getWorld(newWorldName) != null) {
			i++;
			newWorldName = "waterwars" + i;
		}
		
        Bukkit.getServer().createWorld(new WorldCreator(newWorldName).environment(World.Environment.NORMAL).type(WorldType.FLAT));

		return newWorldName;
	}

}
