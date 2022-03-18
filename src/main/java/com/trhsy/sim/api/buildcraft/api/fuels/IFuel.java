package com.trhsy.sim.api.buildcraft.api.fuels;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraftforge.fluids.Fluid;

/**
 * ========================================
 *
 * @ClassName IFuel
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:31
 * ========================================
 **/
public interface IFuel {
    Fluid getFluid();

    int getTotalBurningTime();

    int getPowerPerCycle();
}
