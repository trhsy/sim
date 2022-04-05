package com.trhsy.sim.api.buildcraft.api.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public interface IDebuggable {
    void getDebugInfo(List<String> var1, ForgeDirection var2, ItemStack var3, EntityPlayer var4);
}
