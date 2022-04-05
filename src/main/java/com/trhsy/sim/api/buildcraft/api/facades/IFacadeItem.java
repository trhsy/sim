package com.trhsy.sim.api.buildcraft.api.facades;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */


import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

/**
 * ========================================
 *
 * @ClassName IFacadeItem
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:25
 * ========================================
 **/
public interface IFacadeItem {
    FacadeType getFacadeType(ItemStack var1);

    ItemStack getFacadeForBlock(Block var1, int var2);

    Block[] getBlocksForFacade(ItemStack var1);

    int[] getMetaValuesForFacade(ItemStack var1);
}
