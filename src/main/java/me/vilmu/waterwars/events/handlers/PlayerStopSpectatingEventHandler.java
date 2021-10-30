package me.vilmu.waterwars.events.handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import me.vilmu.waterwars.events.events.PlayerStopSpectatingEvent;

public class PlayerStopSpectatingEventHandler implements Listener{
	
	
	// If someone reads this i know that this is nowhere near an the most efficient way of detecting player stopping spectating. 
	// I just wanted to make a custom event to simplify the code i work with
	@EventHandler
	public void onSneak(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		if(p.getSpectatorTarget() != null) {
			handlePlayerStopSpectating(p, p.getSpectatorTarget(), e);
		}
	}
	
	public void handlePlayerStopSpectating(Player p, Entity e, PlayerToggleSneakEvent e2) {
		
		PlayerStopSpectatingEvent event = new PlayerStopSpectatingEvent(p, e);
		Bukkit.getPluginManager().callEvent(event);
		
		if(event.isCancelled()) {
			e2.setCancelled(true);
		}
	}
}
