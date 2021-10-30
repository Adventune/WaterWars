package me.vilmu.waterwars.events.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.vilmu.waterwars.arena.Arena;
import me.vilmu.waterwars.arena.ArenaUtilities;


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
}
