package com.trhsy.buildcraft.api.statements;/**
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
 * @ClassName IActionProvider
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:51
 * ========================================
 **/
public interface IActionProvider {
    Collection<IActionInternal> getInternalActions(IStatementContainer var1);

    Collection<IActionExternal> getExternalActions(ForgeDirection var1, TileEntity var2);
}
