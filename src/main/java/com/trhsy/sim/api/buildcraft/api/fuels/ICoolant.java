package com.trhsy.sim.api.buildcraft.api.fuels;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraftforge.fluids.Fluid;

/**
 * ========================================
 *
 * @ClassName ICoolant
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:30
 * ========================================
 **/
public interface ICoolant {
    Fluid getFluid();

    float getDegreesCoolingPerMB(float var1);
}
