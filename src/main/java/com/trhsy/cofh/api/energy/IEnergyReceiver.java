package com.trhsy.cofh.api.energy;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName IEnergyReceiver
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 12:59
 * ========================================
 **/
public interface IEnergyReceiver extends IEnergyConnection {
    int receiveEnergy(ForgeDirection var1, int var2, boolean var3);

    int getEnergyStored(ForgeDirection var1);

    int getMaxEnergyStored(ForgeDirection var1);
}
