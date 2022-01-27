package com.trhsy.buildcraft.api.core.render;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName ICullable
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:15
 * ========================================
 **/
public interface ICullable {
    void setRenderSide(ForgeDirection var1, boolean var2);

    void setRenderAllSides();

    boolean shouldSideBeRendered(IBlockAccess var1, int var2, int var3, int var4, int var5);

    void setRenderMask(int var1);
}
