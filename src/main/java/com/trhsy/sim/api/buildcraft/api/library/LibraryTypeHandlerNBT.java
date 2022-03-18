package com.trhsy.sim.api.buildcraft.api.library;

import com.trhsy.sim.api.buildcraft.api.library.LibraryTypeHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

abstract class LibraryTypeHandlerNBT extends LibraryTypeHandler {
    public LibraryTypeHandlerNBT(String extension) {
        super(extension);
    }

    public abstract ItemStack load(ItemStack var1, NBTTagCompound var2);

    public abstract boolean store(ItemStack var1, NBTTagCompound var2);
}