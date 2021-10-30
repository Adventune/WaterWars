package me.vilmu.waterwars.events.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import me.vilmu.waterwars.Storage;
import me.vilmu.waterwars.arena.Arena;
import me.vilmu.waterwars.arena.ArenaManager;
import me.vilmu.waterwars.arena.ArenaUtilities;
import me.vilmu.waterwars.arena.GenerateLoot;

public class GameContainerListener implements Listener {
	
	@EventHandler
    public void OpenContainer(PlayerInteractEvent e)
    {
		if(e.getPlayer().getLocation().getWorld().getName().startsWith("waterwars_") && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Material mat = Storage.getLootCont();
			Material mat2 = Storage.getFoodCont();

	        Arena arena = ArenaUtilities.getArenaWith((Player) e.getPlayer());
	        
	        Block block = e.getClickedBlock();
	        
	        
	        
	        if(arena.isValidLoot(e.getClickedBlock().getLocation())) {
	        	if(arena.hasStarted()) {
		        	if (e.getClickedBlock() != null && block.getType().equals(mat)) {
						Container cont = (Container) block.getState();
						Inventory inv = cont.getInventory();
						
				        GenerateLoot.generateLoot(inv, cont.getLocation());
				        
				        arena.addInvalidLoot(block.getLocation());
				        
				        ArenaManager.updateArena(arena);
				       
					}
					else if (e.getClickedBlock() != null && e.getClickedBlock().getType().equals(mat2)) {
						Container cont = (Container) block.getState();
						Inventory inv = cont.getInventory();
						
				        GenerateLoot.generateFood(inv);
				        
				        arena.addInvalidLoot(block.getLocation());
				        
				        ArenaManager.updateArena(arena);
				        
					}
	        	}
	        }
		}
    }
	@EventHandler
    public void PlacedBlock(BlockPlaceEvent e){

		if(e.getPlayer().getLocation().getWorld().getName().startsWith("waterwars_")) {
			Material mat = Storage.getLootCont();
			Material mat2 = Storage.getFoodCont();
			Material block = e.getBlockPlaced().getType();

	        Arena arena = ArenaUtilities.getArenaWith((Player) e.getPlayer());
	        if(block == mat || block == mat2) {			        
	        	arena.addInvalidLoot(e.getBlockPlaced().getLocation());
	        
	        	ArenaManager.updateArena(arena);
	        	
	        }
		}
    }
}
