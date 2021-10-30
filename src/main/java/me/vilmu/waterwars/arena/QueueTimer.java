package me.vilmu.waterwars.arena;

import java.util.concurrent.TimeUnit;

import me.vilmu.waterwars.Storage;

public class QueueTimer {
	
	private static long timeout;
	private static long start;
	
	private static boolean paused = false;
	private static long timeElapsed;

	public static void start(int duration) {
		if(paused) {
			resume();
		}
		else {
	        timeout = TimeUnit.SECONDS.toMillis(duration);
	        start = System.currentTimeMillis();
		}

	}
	
	public static void pause() {
		if(!paused) {
			timeElapsed = System.currentTimeMillis() - start;
			paused = true;
		}
	}
	
	public static void resume() {
		timeout = timeout - timeElapsed;
        start = System.currentTimeMillis();
        paused = false;
	}
	
	public static void stop() {
		timeout = 0;
		start = 0;
		paused = false;
		timeElapsed = 0;
		
	}
	
	public static int test() {
		if(paused) return 0;
		if(start == 0) return 0;
		
		if(System.currentTimeMillis() - start >= timeout) return 2;
		if(ArenaManager.queuedPlayers.size() >= Storage.getMaxPlayers()) return 2;
		return 1;
		
	}
	
	public static String getTimeRemaining() {
		long milliseconds = timeout - (System.currentTimeMillis() - start);
		
		if(milliseconds <= 0 ) milliseconds = 0; 
		
        long minutes = (milliseconds / 1000) / 60;
        
        long seconds = (milliseconds / 1000) % 60;
		
		return minutes + ":" + seconds;
		
	}
	
}
