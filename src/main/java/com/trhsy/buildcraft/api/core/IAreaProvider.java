package com.trhsy.buildcraft.api.core;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

/**
 * ========================================
 *
 * @ClassName IAreaProvider
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:00
 * ========================================
 **/
public interface IAreaProvider {
    int xMin();

    int yMin();

    int zMin();

    int xMax();

    int yMax();

    int zMax();

    void removeFromWorld();
}
