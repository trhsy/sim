package com.trhsy.buildcraft.api.transport.pluggable;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.buildcraft.api.core.render.ITextureStates;
import com.trhsy.buildcraft.api.transport.IPipe;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName IPipePluggableRenderer
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:05
 * ========================================
 **/
public interface IPipePluggableRenderer {
    void renderPluggable(RenderBlocks var1, IPipe var2, ForgeDirection var3, PipePluggable var4, ITextureStates var5, int var6, int var7, int var8, int var9);
}
