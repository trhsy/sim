package com.trhsy.sim.api.buildcraft.api.crops;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public interface ICropHandler {
    boolean isSeed(ItemStack var1);

    boolean canSustainPlant(World var1, ItemStack var2, int var3, int var4, int var5);

    boolean plantCrop(World var1, EntityPlayer var2, ItemStack var3, int var4, int var5, int var6);

    boolean isMature(IBlockAccess var1, Block var2, int var3, int var4, int var5, int var6);

    boolean harvestCrop(World var1, int var2, int var3, int var4, List<ItemStack> var5);
}
