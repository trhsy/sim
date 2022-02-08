package com.trhsy.buildcraft.api.gates;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.buildcraft.api.transport.IPipe;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName IGate
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:34
 * ========================================
 **/
public interface IGate {
    /** @deprecated */
    @Deprecated
    void setPulsing(boolean var1);

    ForgeDirection getSide();

    IPipe getPipe();
}
