package me.vilmu.waterwars.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import me.vilmu.waterwars.utilities.exceptions.SerializationException;

public class Utils {

    public static byte[] serializeItemStacks(ItemStack[] inv) throws SerializationException {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            BukkitObjectOutputStream bos = new BukkitObjectOutputStream(os);
            bos.writeObject(inv);
            return os.toByteArray();
        } catch (Exception ex) {
            throw new SerializationException(inv, ex);
        }
    }
    
    public static ItemStack[] deserializeItemStacks(byte[] b) throws SerializationException {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(b);
            BukkitObjectInputStream bois = new BukkitObjectInputStream(bais);
            return (ItemStack[]) bois.readObject();
        } catch (Exception ex) {
            throw new SerializationException(new String(b), ex);
        }
    }
    
	public static boolean getBooleanFromProbability(float p){
		Random rand = new Random();
		return rand.nextFloat() < p;
	}
	
	public final static float getAngle(Vector point1, Vector point2) {
        double dx = point2.getX() - point1.getX();
        double dz = point2.getZ() - point1.getZ();
        float angle = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90;
        if (angle < 0) {
            angle += 360.0F;
        }
        return angle;
    }
    
}