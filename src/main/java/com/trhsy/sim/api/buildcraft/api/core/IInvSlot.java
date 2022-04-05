package com.trhsy.sim.api.buildcraft.api.core;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.item.ItemStack;

/**
 * ========================================
 *
 * @ClassName IInvSlot
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:03
 * ========================================
 **/
public interface IInvSlot {
    int getIndex();

    boolean canPutStackInSlot(ItemStack var1);

    boolean canTakeStackFromSlot(ItemStack var1);

    boolean isItemValidForSlot(ItemStack var1);

    ItemStack decreaseStackInSlot(int var1);

    ItemStack getStackInSlot();

    void setStackInSlot(ItemStack var1);
}
