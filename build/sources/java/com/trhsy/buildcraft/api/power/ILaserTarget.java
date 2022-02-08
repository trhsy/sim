package com.trhsy.buildcraft.api.power;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

/**
 * ========================================
 *
 * @ClassName ILaserTarget
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:39
 * ========================================
 **/
public interface ILaserTarget {
    boolean requiresLaserEnergy();

    void receiveLaserEnergy(int var1);

    boolean isInvalidTarget();

    double getXCoord();

    double getYCoord();

    double getZCoord();
}
