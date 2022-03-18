package com.trhsy.sim.api.buildcraft.api.statements;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */


import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName IActionExternal
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:50
 * ========================================
 **/
public interface IActionExternal extends IStatement {
    void actionActivate(TileEntity var1, ForgeDirection var2, IStatementContainer var3, IStatementParameter[] var4);
}
