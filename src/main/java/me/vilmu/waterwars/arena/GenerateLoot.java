package me.vilmu.waterwars.arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.vilmu.waterwars.Storage;
import me.vilmu.waterwars.utilities.Utils;

public class GenerateLoot {
	
	private static Random rand = new Random();
	
	public static void generateLoot(Inventory inv, Location loc) {
				
			// Choose loot type
			
			Map<String, Integer> weightMap = Storage.getLootWeights();
			
			float commonWeight = (float)weightMap.get("common") / 100;
			float rareWeight = (float)weightMap.get("rare") / 100;
			float epicWeight = (float)weightMap.get("epic") / 100;
			
			float arenaSize = Storage.getArenaSize();
			
			double x = Math.abs(loc.getX());
			double z = Math.abs(loc.getZ());
			
			double distance = Math.sqrt(x*x + z*z);
			int divider = Storage.getHotZone();
			
			if(distance < arenaSize/divider) {
				distance /= divider;
				float prob = (float) (1 - distance / arenaSize);
				
				if(prob > rareWeight + epicWeight) {
					rareWeight = prob*(rareWeight/(rareWeight+epicWeight));
					epicWeight = prob*(epicWeight/(rareWeight+epicWeight));
					commonWeight = 1 - (rareWeight + epicWeight);
					
					if(commonWeight < 0.01) {
						float tempC = (float) 0.01;
						commonWeight = tempC - commonWeight;
						epicWeight = epicWeight - commonWeight;
						commonWeight = tempC;
					}
					getLootTier(commonWeight, rareWeight, epicWeight, inv);
				}
				
				else {
					getLootTier(commonWeight, rareWeight, epicWeight, inv);
				}
			}
			else {
				
				List<HashMap<String, Integer>> hash = new ArrayList<HashMap<String, Integer>>();
				hash = Storage.getCommonLoot();
				
				generate(inv, hash);
			}
			
			
	}

	private static void getLootTier(float commonWeight, float rareWeight, float epicWeight, Inventory inv) {
		List<HashMap<String, Integer>> hash = new ArrayList<HashMap<String, Integer>>();
		if(Utils.getBooleanFromProbability(commonWeight)) {
			
			
			hash = Storage.getCommonLoot();
			
			generate(inv, hash);		
		} else {
			
			rareWeight = rareWeight + (commonWeight * (rareWeight / (rareWeight + epicWeight)));
			
			if(Utils.getBooleanFromProbability(rareWeight)) {				
				hash = Storage.getRareLoot();
				
				generate(inv, hash);
				
			}
			else {				
				hash = Storage.getEpicLoot();
				
				generate(inv, hash);
			}
		}
		
	}

	private static void generate(Inventory inv, List<HashMap<String, Integer>> hash) {
		
		// Prepare container
		
		inv.clear();
		
		// Generate Loot
		
		List<Integer> usedInts = new ArrayList<Integer>();
		

		for(HashMap<String,Integer> map : hash) {
			for(String key : map.keySet()) {
				float prob = (float)map.get(key)/100;
				if(Utils.getBooleanFromProbability(prob)) {
					int i = rand.nextInt(27);
					while(usedInts.contains(i)) i = rand.nextInt(27);
					usedInts.add(i);
					
					Material mat = Material.getMaterial(key);
					ItemStack is = new ItemStack(mat);
					
					inv.setItem(i, is);
					
				}
			}
		}
		
	}
	

	public static void generateFood(Inventory inv) {
		// Prepare container
		
		inv.clear();
		
		// Generate Loot
		
		List<HashMap<String, Integer>> hash = new ArrayList<HashMap<String, Integer>>();		
		hash = Storage.getFoodLoot();
		
		List<Integer> usedInts = new ArrayList<Integer>();
		
		for(HashMap<String,Integer> map : hash) {
			for(String key : map.keySet()) {
				float prob = (float)map.get(key)/100;
				if(Utils.getBooleanFromProbability(prob)) {
					int i = rand.nextInt(27);
					while(usedInts.contains(i)) i = rand.nextInt(27);
					usedInts.add(i);
					
					Material mat = Material.getMaterial(key);
					ItemStack is = new ItemStack(mat, rand.nextInt(6));
					
					inv.setItem(i, is);
					
				}
			}
		}
	}
}
