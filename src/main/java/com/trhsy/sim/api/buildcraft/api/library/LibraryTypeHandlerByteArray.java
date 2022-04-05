package com.trhsy.sim.api.buildcraft.api.library;

import net.minecraft.item.ItemStack;

public abstract class LibraryTypeHandlerByteArray extends LibraryTypeHandler {
    public LibraryTypeHandlerByteArray(String extension) {
        super(extension);
    }

    public abstract ItemStack load(ItemStack var1, byte[] var2);

    public abstract byte[] store(ItemStack var1);
}
