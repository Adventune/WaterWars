package me.vilmu.waterwars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

public class Storage {

	static List<HashMap<String, Integer>> commonLoot = new ArrayList<HashMap<String, Integer>>();
	static List<HashMap<String, Integer>> rareLoot = new ArrayList<HashMap<String, Integer>>();	
	static List<HashMap<String, Integer>> epicLoot = new ArrayList<HashMap<String, Integer>>();	
	static List<HashMap<String, Integer>> foodLoot = new ArrayList<HashMap<String, Integer>>();
	
	static Map<String, Integer> lootWeights = new HashMap<String, Integer>();
	
	static List<Location> lootLoc = new ArrayList<Location>();
	static List<Location> foodLoc = new ArrayList<Location>();
	static List<Location> spawnLocs = new ArrayList<Location>();
	
	static Material lootCont = null;
	static Material foodCont = null;
	static Material locationTool = null;
	
	static Location arenaCenter = null;
	
	static int arenaSize = 0;

	static boolean queuedOnJoin;
	static Material queueItem;
	static int minPlayers;
	static int maxPlayers;
	static int maxQueueTime;
	private static int hotzone;
	private static List<String> arenaNames;
	private static int timeBeforeDrowning;
	private static int drowningSpeed;
	private static int exponentialDrowning;
	private static int drownedCount;
	private static int minBorder;
	private static int timeAfterBorder;
	private static int timeBeforeBorder;
	private static int drownedHotzone;
	private static int timeBeforeDrowned;


	// Setters -------------------------------------------------------
	
	// Common loot
	public static void setCommonLoot(List<HashMap<String, Integer>> data){
		commonLoot = data;
	}
	

	// Rare loot
	public static void setRareLoot(List<HashMap<String, Integer>> data) {
		rareLoot = data;
	}

	// Epic loot
	public static void setEpicLoot(List<HashMap<String, Integer>> data) {
		epicLoot = data;
	}

	// Food loot
	public static void setFoodLoot(List<HashMap<String, Integer>> data) {
		foodLoot = data;
	}

	// Loot weights
	public static void setLootWeights(HashMap<String, Integer> data) {
		lootWeights = data;
	}
	
	// Food locations
	public static void setLootCont(Material lootCont2) {
		lootCont = lootCont2;
	}
	
	// Food locations
	public static void setFoodCont(Material data) {
		foodCont = data;
	}
	
	// Map center
	public static void setArenaCenter(Location data) {
		arenaCenter = data;
	}
	
	// Map size 
	public static void setArenaSize(int data)	{
		arenaSize = data;
	}
	
	// Queue method
	public static void setQueuedOnJoin(boolean data) {
		queuedOnJoin = data;	
	}
	
	// Queue item
	public static void setQueueItem(Material queueItem2) {
		queueItem = queueItem2;	
	}
	
	// Min players
	public static void setMinPlayers(int data) {
		minPlayers = data;
	}
	
	// Max players
	public static void setMaxPlayers(int data) {
		maxPlayers = data;		
	}
	
	// Max queue time
	public static void setMaxQueueTime(int data) {
		maxQueueTime = data;		
	}

	public static void setHotZone(int data) {
		hotzone = data;		
	}
	
	public static void setSpawnLoc(List<Location> data) {
		spawnLocs = data;		
	}
	
	public static void setArenaNames(List<String> data) {
		arenaNames = data;		
	}
	
	public static void setTimeBeforeDrowning(int i) {
		timeBeforeDrowning = i;
	}	
	public static void setDrowningSpeed(int i) {
		drowningSpeed = i;
	}
	public static void setExponentialDrowning(int i) {
		exponentialDrowning = i;
	}	
	public static void setDrownedCount(int i) {
		drownedCount = i;
	}
	
	public static void setTimeBeforeDrowned(int i) {
		timeBeforeDrowned = i;
	}


	public static void setDrownedHotzone(int i) {
		drownedHotzone = i;
	}


	public static void setTimeBeforeBorder(int i) {
		timeBeforeBorder = i;
	}


	public static void setTimeToShrink(int i) {
		timeAfterBorder = i;
	}


	public static void setMinBorder(int i) {
		minBorder = i;
	}
	// Getters -------------------------------------------------------
	
	// Common loot
	public static List<HashMap<String, Integer>> getCommonLoot(){
		return commonLoot;
	}
	
	// Rare loot
	public static List<HashMap<String, Integer>> getRareLoot(){
		return rareLoot;
	}
	
	// Epic loot
	public static List<HashMap<String, Integer>> getEpicLoot(){
		return epicLoot;
	}
	
	// Food loot
	public static List<HashMap<String, Integer>> getFoodLoot(){
		return foodLoot;
	}
	
	// Loot weights
	public static Map<String, Integer> getLootWeights(){
		return lootWeights;
	}
	
	// Loot containers
	public static Material getLootCont(){
		return lootCont;
	}
	
	// Food containers	
	public static Material getFoodCont(){
		return foodCont;
	}
	
	// Map center
	public static Location getArenaCenter() {
		return arenaCenter;
	}
	
	// Map size
	public static int getArenaSize() {
		return arenaSize;
	}
	
	// Queue method
	public static boolean getQueuedOnJoin() {
		return queuedOnJoin;
	}
	
	// Queue item
	public static Material getQueueItem() {
		return queueItem;
	}

	// Min players
	public static int getMinPlayers() {
		return minPlayers;		
	}
	
	// Max players
	public static int getMaxPlayers() {
		return maxPlayers;		
	}

	// Max queue time
	public static int getMaxQueueTime() {
		return maxQueueTime;
	}
	
	// Queue item name
	public static String getQueueItemName() {
		return ChatColor.AQUA + "Queue to a new game! " + ChatColor.GRAY + "(Right Click)";
	}
	
	public static int getHotZone() {
		return hotzone;	
	}

	public static List<Location> getSpawnLoc() {
		return spawnLocs;		
	}


	public static List<String> getArenaNames() {
		return arenaNames;		
	}


	// Queue item name
	public static String getQueueItemBackName() {
		return ChatColor.AQUA + "Go back to lobby! " + ChatColor.GRAY + "(Right Click)";
	}


	public static int getTimeBeforeDrowning() {
		return timeBeforeDrowning;
	}
	public static int getDrowningSpeed() {
		return drowningSpeed;
	}
	public static int getExponentialDrowning() {
		return exponentialDrowning;
	}


	public static int getDrownedCount() {
		return drownedCount;
	}

	public static int getTimeBeforeDrowned() {
		return timeBeforeDrowned;
	}


	public static int getDrownedHotzone() {
		return drownedHotzone;
	}


	public static int getTimeBeforeBorder() {
		return timeBeforeBorder;
	}


	public static int getTimeToShrink() {
		return timeAfterBorder;
	}


	public static int getMinBorder() {
		return minBorder;
	}

}
