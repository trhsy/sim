package com.trhsy.cofh.api.energy;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName IEnergyProvider
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 12:59
 * ========================================
 **/
public interface IEnergyProvider extends IEnergyConnection {
    int extractEnergy(ForgeDirection var1, int var2, boolean var3);

    int getEnergyStored(ForgeDirection var1);

    int getMaxEnergyStored(ForgeDirection var1);
}