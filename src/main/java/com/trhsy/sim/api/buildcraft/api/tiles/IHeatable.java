package com.trhsy.sim.api.buildcraft.api.tiles;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

/**
 * ========================================
 *
 * @ClassName IHeatable
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:57
 * ========================================
 **/
public interface IHeatable {
    double getMinHeatValue();

    double getIdealHeatValue();

    double getMaxHeatValue();

    double getCurrentHeatValue();

    double setHeatValue(double var1);
}
