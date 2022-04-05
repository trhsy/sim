package com.trhsy.sim.api.buildcraft.api.transport;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.api.buildcraft.api.gates.IGate;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName IPipe
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:59
 * ========================================
 **/
public interface IPipe {
    IPipeTile getTile();

    IGate getGate(ForgeDirection var1);

    boolean hasGate(ForgeDirection var1);

    boolean isWired(PipeWire var1);

    boolean isWireActive(PipeWire var1);
}
