package com.trhsy.sim.api.buildcraft.api.statements;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Collection;

/**
 * ========================================
 *
 * @ClassName ITriggerProvider
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:53
 * ========================================
 **/
public interface ITriggerProvider {
    Collection<ITriggerInternal> getInternalTriggers(IStatementContainer var1);

    Collection<ITriggerExternal> getExternalTriggers(ForgeDirection var1, TileEntity var2);
}
