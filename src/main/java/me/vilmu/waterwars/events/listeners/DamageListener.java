package me.vilmu.waterwars.events.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.vilmu.waterwars.PlayerMessages;
import me.vilmu.waterwars.arena.Arena;
import me.vilmu.waterwars.arena.ArenaUtilities;
import me.vilmu.waterwars.utilities.Utils;


public class DamageListener implements Listener{

	@EventHandler
	public void playerDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			String worldName = p.getWorld().getName();
			Arena arena = ArenaUtilities.getArenaWith(p);
			
			if(worldName.startsWith("waterwars") && arena.hasStarted() && e.getCause() != DamageCause.DROWNING) return;
			if(e.getDamage() > 100000000) return;
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void damagedPlayer (EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			PlayerMessages.damaged((Player) e.getDamager(), p, Utils.round(p.getHealth() - e.getFinalDamage(), 1));
		}
	}
}
