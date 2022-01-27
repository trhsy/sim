package com.trhsy.buildcraft.api.core;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.world.World;

/**
 * ========================================
 *
 * @ClassName IWorldProperty
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:04
 * ========================================
 **/
public interface IWorldProperty {
    boolean get(World var1, int var2, int var3, int var4);

    void clear();
}
