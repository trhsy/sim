package com.trhsy.sim.api.buildcraft.api.transport;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public interface IStripesActivator {
    void sendItem(ItemStack var1, ForgeDirection var2);

    void dropItem(ItemStack var1, ForgeDirection var2);
}
