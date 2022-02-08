package com.trhsy.cofh.api.energy;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.item.ItemStack;

/**
 * ========================================
 *
 * @ClassName IEnergyContainerItem
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 12:57
 * ========================================
 **/
public interface IEnergyContainerItem {

    int receiveEnergy(ItemStack var1, int var2, boolean var3);

    int extractEnergy(ItemStack var1, int var2, boolean var3);

    int getEnergyStored(ItemStack var1);

    int getMaxEnergyStored(ItemStack var1);
}
