package com.trhsy.cofh.api.energy;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName IEnergyConnection
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 12:57
 * ========================================
 **/
public interface IEnergyConnection {
    boolean canConnectEnergy(ForgeDirection var1);
}