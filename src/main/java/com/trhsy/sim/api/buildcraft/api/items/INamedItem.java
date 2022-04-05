package com.trhsy.sim.api.buildcraft.api.items;

import net.minecraft.item.ItemStack;

public interface INamedItem {
    String getName(ItemStack var1);

    boolean setName(ItemStack var1, String var2);
}
