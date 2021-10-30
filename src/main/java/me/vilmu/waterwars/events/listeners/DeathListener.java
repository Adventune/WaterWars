package me.vilmu.waterwars.events.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageEvent;

import me.vilmu.waterwars.PlayerMessages;
import me.vilmu.waterwars.arena.Arena;
import me.vilmu.waterwars.arena.ArenaManager;
import me.vilmu.waterwars.arena.ArenaUtilities;

public class DeathListener implements Listener{
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {		
			Player p = (Player) e.getEntity();

		
			if(p.getWorld().getName().startsWith("waterwars_")) {
				Arena a = ArenaUtilities.getArenaWith(p);
				
				if(a.isValid()) {
					if(p.getHealth() - e.getFinalDamage() <= 0) {

						a.addDeadPlayer(p);
						
						int place = a.getAlivePlayers().size() + 1;
						
						PlayerMessages.killed(p, place, a.getAlivePlayers().size() + a.getDeadPlayers().size(), a.getPlayers());
						
						p.setGameMode(GameMode.SPECTATOR);
						
						PlayerMessages.backHome(p);
						a.getWorld().strikeLightningEffect(p.getLocation());
						
						ArenaManager.updateArena(a);
						p.setFoodLevel(20);
						p.setHealth(20);
						e.setCancelled(true);
					}
					
					
				}
			}
			
		}

	}
}
