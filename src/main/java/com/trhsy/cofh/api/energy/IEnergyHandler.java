package com.trhsy.cofh.api.energy;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName IEnergyHandler
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 12:58
 * ========================================
 **/
public interface IEnergyHandler extends IEnergyProvider, IEnergyReceiver {
    @Override
    int receiveEnergy(ForgeDirection var1, int var2, boolean var3);

    int extractEnergy(ForgeDirection var1, int var2, boolean var3);

    @Override
    int getEnergyStored(ForgeDirection var1);

    @Override
    int getMaxEnergyStored(ForgeDirection var1);
}

