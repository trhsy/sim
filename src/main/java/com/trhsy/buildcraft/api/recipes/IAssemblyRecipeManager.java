package com.trhsy.buildcraft.api.recipes;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.item.ItemStack;

/**
 * ========================================
 *
 * @ClassName IAssemblyRecipeManager
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:42
 * ========================================
 **/
public interface IAssemblyRecipeManager {
    void addRecipe(String var1, int var2, ItemStack var3, Object... var4);

    void addRecipe(IFlexibleRecipe<ItemStack> var1);

    void removeRecipe(String var1);

    void removeRecipe(IFlexibleRecipe<ItemStack> var1);

    Collection<IFlexibleRecipe<ItemStack>> getRecipes();
}
