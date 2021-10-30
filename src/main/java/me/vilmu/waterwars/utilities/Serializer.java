package me.vilmu.waterwars.utilities;

import org.bukkit.inventory.ItemStack;

import me.vilmu.waterwars.utilities.exceptions.SerializationException;

public interface Serializer {
    public byte [] serializeItemStacks(ItemStack[] inv) throws SerializationException;
    public ItemStack[] deserializeItemStacks(byte[] b) throws SerializationException;
}
