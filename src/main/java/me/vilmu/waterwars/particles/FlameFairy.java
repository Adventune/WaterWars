package me.vilmu.waterwars.particles;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class FlameFairy {
	static double t = 0;
	static double r = 2;
	static boolean up = true;
	static double y = 0.1;

	public static void flames(Player p) {


			Location loc = p.getLocation();
			
			t = t + Math.PI/8;
			
			
			if (up == true) {
				if(y < 2) {
					y = y+0.1;
				}
				else {
					y = y-0.1;
					up = false;
				}

				
			}
			else {
				if(y > 0.1 ) {
					y = y-0.1;
				}
				else {
					y = y+0.1;
					up = true;
				}

			}

			
			double x = r*cos(t)/2;
			double z = r*sin(t)/2;
			loc.add(x, y, z);
					
			loc.getWorld().spawnParticle(Particle.FLAME, loc, 1, 0, 0, 0, 0);
			loc.subtract(x, y, z);
			
				
	}
}
