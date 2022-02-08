package com.trhsy.buildcraft.api.recipes;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraftforge.fluids.FluidStack;

import java.util.Collection;

/**
 * ========================================
 *
 * @ClassName IRefineryRecipeManager
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:44
 * ========================================
 **/
public interface IRefineryRecipeManager {
    void addRecipe(String var1, FluidStack var2, FluidStack var3, int var4, int var5);

    void addRecipe(String var1, FluidStack var2, FluidStack var3, FluidStack var4, int var5, int var6);

    void removeRecipe(String var1);

    void removeRecipe(IFlexibleRecipe<FluidStack> var1);

    Collection<IFlexibleRecipe<FluidStack>> getRecipes();

    IFlexibleRecipe<FluidStack> getRecipe(String var1);
}
