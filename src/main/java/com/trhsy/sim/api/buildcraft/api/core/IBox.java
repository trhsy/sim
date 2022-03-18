package com.trhsy.sim.api.buildcraft.api.core;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

/**
 * ========================================
 *
 * @ClassName IBox
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:00
 * ========================================
 **/
public interface IBox extends IZone {
    IBox expand(int var1);

    IBox contract(int var1);

    Position pMin();

    Position pMax();

    void createLaserData();
}
