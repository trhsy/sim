package com.trhsy.buildcraft.api.statements;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName ITriggerExternal
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:53
 * ========================================
 **/
public interface ITriggerExternal extends IStatement {
    boolean isTriggerActive(TileEntity var1, ForgeDirection var2, IStatementContainer var3, IStatementParameter[] var4);
}{
}
