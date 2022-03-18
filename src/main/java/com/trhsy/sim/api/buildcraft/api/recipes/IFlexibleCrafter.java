package com.trhsy.sim.api.buildcraft.api.recipes;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * ========================================
 *
 * @ClassName IFlexibleCrafter
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:43
 * ========================================
 **/
public interface IFlexibleCrafter {
    int getCraftingItemStackSize();

    ItemStack getCraftingItemStack(int var1);

    ItemStack decrCraftingItemStack(int var1, int var2);

    FluidStack getCraftingFluidStack(int var1);

    FluidStack decrCraftingFluidStack(int var1, int var2);

    int getCraftingFluidStackSize();
}
