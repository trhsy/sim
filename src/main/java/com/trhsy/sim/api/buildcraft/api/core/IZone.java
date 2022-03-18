package com.trhsy.sim.api.buildcraft.api.core;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import java.util.Random;

/**
 * ========================================
 *
 * @ClassName IZone
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:04
 * ========================================
 **/
public interface IZone {
    double distanceTo(BlockIndex var1);

    double distanceToSquared(BlockIndex var1);

    boolean contains(double var1, double var3, double var5);

    BlockIndex getRandomBlockIndex(Random var1);
}
