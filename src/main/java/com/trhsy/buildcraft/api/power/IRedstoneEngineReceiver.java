package com.trhsy.buildcraft.api.power;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.cofh.api.energy.IEnergyReceiver;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName IRedstoneEngineReceiver
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:40
 * ========================================
 **/
public interface IRedstoneEngineReceiver extends IEnergyReceiver {
    boolean canConnectRedstoneEngine(ForgeDirection var1);
}
