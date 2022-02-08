package com.trhsy.buildcraft.api.transport;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName IPipeConnection
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:00
 * ========================================
 **/
public interface IPipeConnection {
    ConnectOverride overridePipeConnection(PipeType var1, ForgeDirection var2);
}
