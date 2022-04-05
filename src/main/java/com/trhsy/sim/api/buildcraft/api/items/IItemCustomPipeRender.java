package com.trhsy.sim.api.buildcraft.api.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

public interface IItemCustomPipeRender extends INamedItem {
    float getPipeRenderScale(ItemStack var1);

    @SideOnly(Side.CLIENT)
    boolean renderItemInPipe(ItemStack var1, double var2, double var4, double var6);
}
