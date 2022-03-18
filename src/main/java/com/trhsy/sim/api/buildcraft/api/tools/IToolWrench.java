package com.trhsy.sim.api.buildcraft.api.tools;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.entity.player.EntityPlayer;

/**
 * ========================================
 *
 * @ClassName IToolWrench
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:58
 * ========================================
 **/
public interface IToolWrench {
    boolean canWrench(EntityPlayer var1, int var2, int var3, int var4);

    void wrenchUsed(EntityPlayer var1, int var2, int var3, int var4);
}
