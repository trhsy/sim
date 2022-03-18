package com.trhsy.sim.api.cofh.api.energy;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

/**
 * ========================================
 *
 * @ClassName IEnergyStorage
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:00
 * ========================================
 **/
public interface IEnergyStorage {
    int receiveEnergy(int var1, boolean var2);

    int extractEnergy(int var1, boolean var2);

    int getEnergyStored();

    int getMaxEnergyStored();
}
