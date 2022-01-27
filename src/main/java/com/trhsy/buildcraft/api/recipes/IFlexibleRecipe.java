package com.trhsy.buildcraft.api.recipes;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.item.ItemStack;

/**
 * ========================================
 *
 * @ClassName IFlexibleRecipe
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:43
 * ========================================
 **/
public interface IFlexibleRecipe<T> {
    boolean canBeCrafted(IFlexibleCrafter var1);

    CraftingResult<T> craft(IFlexibleCrafter var1, boolean var2);

    CraftingResult<T> canCraft(ItemStack var1);

    String getId();
}
