package com.trhsy.sim.api.buildcraft.api.power;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName IEngine
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:39
 * ========================================
 **/
public interface IEngine {
    boolean canReceiveFromEngine(ForgeDirection var1);

    int receiveEnergyFromEngine(ForgeDirection var1, int var2, boolean var3);
}
