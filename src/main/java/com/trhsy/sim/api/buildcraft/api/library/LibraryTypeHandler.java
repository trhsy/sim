package com.trhsy.sim.api.buildcraft.api.library;

import net.minecraft.item.ItemStack;

public abstract class LibraryTypeHandler {
    private final String extension;

    public LibraryTypeHandler(String extension) {
        this.extension = extension;
    }

    public abstract boolean isHandler(ItemStack var1, HandlerType var2);

    public boolean isInputExtension(String ext) {
        return this.extension.equals(ext);
    }

    public String getOutputExtension() {
        return this.extension;
    }

    public abstract int getTextColor();

    public abstract String getName(ItemStack var1);

    public static enum HandlerType {
        LOAD,
        STORE;

        private HandlerType() {
        }
    }
}
