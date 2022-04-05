package com.trhsy.sim.api.buildcraft.api.lists;

import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class ListMatchHandler {
    public ListMatchHandler() {
    }

    public abstract boolean matches(Type var1, ItemStack var2, ItemStack var3, boolean var4);

    public abstract boolean isValidSource(Type var1, ItemStack var2);

    public List<ItemStack> getClientExamples(Type type, ItemStack stack) {
        return null;
    }

    public static enum Type {
        TYPE,
        MATERIAL,
        CLASS;

        private Type() {
        }
    }
}
