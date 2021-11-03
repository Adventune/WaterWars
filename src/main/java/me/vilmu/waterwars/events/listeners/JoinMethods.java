package me.vilmu.waterwars.events.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.vilmu.waterwars.PlayerMessages;
import me.vilmu.waterwars.Storage;
import me.vilmu.waterwars.arena.ArenaManager;
import me.vilmu.waterwars.arena.ArenaUtilities;

public class JoinMethods implements Listener{
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
	
		Player p = e.getPlayer();
		boolean bool = Storage.getQueuedOnJoin();
		
		Inventory inv = p.getInventory();
		inv.clear();
		ItemStack i = new ItemStack(Storage.getQueueItem());
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(Storage.getQueueItemName());
		i.setItemMeta(im);
		
		p.setHealth(20);
		p.setFoodLevel(20);
		p.teleport(Storage.getLobby().getSpawnLocation().add(0.5, 0, 0.5));
		
		if(bool) {
			ArenaUtilities.queuePlayer(p);
		}
		inv.addItem(i);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void rightClick(PlayerInteractEvent e) {
	    Player p = e.getPlayer();
	    Action a = e.getAction();
	    ItemStack i = p.getItemInHand();
	    
	    if(p.getWorld().getName().startsWith("waterwars")) return;
	    
	    if(i == null) return;
	    if(i.getType() != Storage.getQueueItem() || !i.hasItemMeta()) return;
	    if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
		    if(i.getItemMeta().getDisplayName().equals(Storage.getQueueItemName())) {
		    	e.setCancelled(true); 
		    	queue(p);
		    }
	    }

	    
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void eating(PlayerItemConsumeEvent e) {
		Player p = e.getPlayer();
	    ItemStack i = p.getItemInHand();
	    
	    if(p.getWorld().getName().startsWith("waterwars_")) return;	
	    if(i == null) return;
	    if(i.getType() == Material.AIR) return;
	    if(i.getType() != Storage.getQueueItem()) return;
	    if(i.hasItemMeta()) if(i.getItemMeta().getDisplayName().equals(Storage.getQueueItemName())) e.setCancelled(true);
	    
	}
	
	@EventHandler
	public void droppedItem(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
	    ItemStack i = e.getItemDrop().getItemStack();
	    
	    if(p.getWorld().getName().startsWith("waterwars_")) return;
	    
	    if(i == null) return;
	    if(i.getType() != Storage.getQueueItem()) return;
	    if(i.hasItemMeta()) if(i.getItemMeta().getDisplayName().equals(Storage.getQueueItemName())) e.setCancelled(true);
	}
	
	@EventHandler
	public void onItemMove(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
	    ItemStack i = e.getCurrentItem();
	    
		if(p.getGameMode() == GameMode.CREATIVE) return;
	    if(p.getWorld().getName().startsWith("waterwars_")) return;
	    
	    if(i == null) return;
	    if(i.getType() != Storage.getQueueItem()) return;
	    if(i.hasItemMeta()) if(i.getItemMeta().getDisplayName().equals(Storage.getQueueItemName())) {
	    	
	    	queue(p);
	    	e.setCancelled(true);
	    }
	}
	

	public void queue(Player p) {
    	if(ArenaManager.queuedPlayers.contains(p)) {
    		ArenaUtilities.removePlayerFromQueue(p);
    		return;
    	} else if (ArenaUtilities.getPrivateGameWith(p).isValid()) {
    		if(ArenaUtilities.isPGOwner(p)) {
    			PlayerMessages.cantUseAsPGOwner(p);
    		} else {
    			ArenaUtilities.removePlayerFromPrivateGame(p);
    		}
    	}
    	
    	ArenaUtilities.queuePlayer(p);
	}
	
	
}
