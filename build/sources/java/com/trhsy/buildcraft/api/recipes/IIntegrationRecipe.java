package com.trhsy.buildcraft.api.recipes;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.item.ItemStack;

/**
 * ========================================
 *
 * @ClassName IIntegrationRecipe
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:44
 * ========================================
 **/
public interface IIntegrationRecipe extends IFlexibleRecipe<ItemStack> {
    boolean isValidInputA(ItemStack var1);

    boolean isValidInputB(ItemStack var1);
}
